package me.zipestudio.keeperwisps.entity;

import java.util.*;
import lombok.Getter;
import net.minecraft.core.BlockPos;
//? if <1.21.6 {
/*import net.minecraft.nbt.CompoundTag;
*///?}
import net.minecraft.network.syncher.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
//? if >=1.21.6 {
import net.minecraft.world.level.storage.*;
//?}
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import me.zipestudio.keeperwisps.compat.AccessorySlot;
import me.zipestudio.keeperwisps.death.InventoryRow;
import me.zipestudio.keeperwisps.entity.ai.*;
import me.zipestudio.keeperwisps.registry.KeeperWispsSounds;

public class WispEntity extends PathfinderMob {

	public static final float EYE_HEIGHT = 0.3F;
	public static final int DEATH_TICKS = 10;
	public static final int RETRIEVE_PULSE_TICKS = 10;

	private static final double HORIZONTAL_RANGE = 8.0D;
	private static final double VERTICAL_RANGE = 3.0D;
	private static final int WANDER_TARGET_ATTEMPTS = 16;
	private static final float LOOK_DISTANCE = 4.0F;
	private static final float LOOK_PROBABILITY = 0.5F;
	private static final int DISPLAY_CYCLE_TICKS = 40;
	private static final int RETRIEVE_GLINTS = 8;
	private static final int DEATH_GLINT_TICKS = 6;
	private static final int AMBIENT_SOUND_INTERVAL_MIN = 200;
	private static final int AMBIENT_SOUND_INTERVAL_RANDOM = 400;
	private static final float READY_LERP = 0.15F;

	private static final String VARIANT_KEY = "Variant";
	private static final String HOME_KEY = "Home";

	private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(WispEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DATA_READY = SynchedEntityData.defineId(WispEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<ItemStack> DATA_DISPLAY_ITEM = SynchedEntityData.defineId(WispEntity.class, EntityDataSerializers.ITEM_STACK);

	public final AnimationState idleAnimationState = new AnimationState();

	private final WispInventory inventory = new WispInventory(this);
	private final WispEyeContact eyeContact = new WispEyeContact(this);

	@Getter
	@Nullable
	private BlockPos homePos;
	@Getter
	private boolean wanderPending = true;
	@Getter
	private int dyingTicks;
	@Getter
	private int retrievePulseTicks;
	@Nullable
	private WispVariant lastSyncedVariant;
	private float readyProgress;
	private float readyProgressO;

	public WispEntity(EntityType<? extends WispEntity> type, Level level) {
		super(type, level);
		this.moveControl = new WispMoveControl(this);
		this.lookControl = new WispLookControl(this);
		this.xpReward = 0;
		this.setNoGravity(true);
		this.setPersistenceRequired();
		this.ambientSoundTime = -this.getAmbientSoundInterval();
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 1.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.07D)
				.add(Attributes.FLYING_SPEED, 0.07D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
	}

	public void setup(BlockPos homePos, InventoryRow row, List<ItemStack> items, int experience) {
		this.homePos = homePos;
		this.inventory.setup(row, items, experience);
	}

	public void setAccessorySlots(Map<Integer, AccessorySlot> accessorySlots) {
		this.inventory.setAccessorySlots(accessorySlots);
	}

	//? if >=1.21 {
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_VARIANT, WispVariant.GREEN.ordinal());
		builder.define(DATA_DISPLAY_ITEM, ItemStack.EMPTY);
		builder.define(DATA_READY, false);
	}
	//?} else {
	/*@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_VARIANT, WispVariant.GREEN.ordinal());
		this.entityData.define(DATA_DISPLAY_ITEM, ItemStack.EMPTY);
		this.entityData.define(DATA_READY, false);
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
		return EYE_HEIGHT;
	}
	*///?}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new WispWanderGoal(this));
		this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, LOOK_DISTANCE, LOOK_PROBABILITY));
		this.goalSelector.addGoal(3, new WispRandomLookAroundGoal(this));
	}

	@Override
	public void tick() {
		this.noPhysics = true;
		super.tick();
		this.noPhysics = false;
		this.setNoGravity(true);

		if (this.level().isClientSide()) {
			this.idleAnimationState.startIfStopped(this.tickCount);
			if (this.retrievePulseTicks > 0) {
				this.retrievePulseTicks--;
			}
			this.readyProgressO = this.readyProgress;
			this.readyProgress = Mth.lerp(READY_LERP, this.readyProgress, this.isReady() ? 1.0F : 0.0F);
			return;
		}
		if (this.homePos == null) {
			this.homePos = this.blockPosition();
		}
		if (this.tickCount % DISPLAY_CYCLE_TICKS == 0) {
			this.inventory.cycleDisplayItem();
		}
		this.eyeContact.tick();
	}

	public WispVariant getVariant() {
		return WispVariant.byId(this.entityData.get(DATA_VARIANT));
	}

	void setVariant(WispVariant variant) {
		this.entityData.set(DATA_VARIANT, variant.ordinal());
	}

	public ItemStack getDisplayItem() {
		return this.entityData.get(DATA_DISPLAY_ITEM);
	}

	void setDisplayItem(ItemStack stack) {
		this.entityData.set(DATA_DISPLAY_ITEM, stack);
	}

	public boolean isReady() {
		return this.entityData.get(DATA_READY);
	}

	void setReady(boolean ready) {
		this.entityData.set(DATA_READY, ready);
	}

	public float getReadyProgress(float partialTick) {
		return Mth.lerp(partialTick, this.readyProgressO, this.readyProgress);
	}

	public boolean hasItems() {
		if (this.level().isClientSide()) {
			return this.getVariant() != WispVariant.GREEN;
		}
		return !this.inventory.isEmpty();
	}

	public void onWanderStarted() {
		this.wanderPending = false;
	}

	public boolean isOutsideHome() {
		if (this.homePos == null) {
			return false;
		}
		double dx = this.getX() - (this.homePos.getX() + 0.5D);
		double dz = this.getZ() - (this.homePos.getZ() + 0.5D);
		double dy = this.getY() - this.homePos.getY();
		return dx * dx + dz * dz > HORIZONTAL_RANGE * HORIZONTAL_RANGE || Math.abs(dy) > VERTICAL_RANGE;
	}

	public BlockPos findWanderTarget() {
		BlockPos home = this.homePos != null ? this.homePos : this.blockPosition();
		for (int i = 0; i < WANDER_TARGET_ATTEMPTS; i++) {
			BlockPos candidate = home.offset(
					Mth.floor(Mth.nextDouble(this.random, -HORIZONTAL_RANGE, HORIZONTAL_RANGE)),
					Mth.floor(Mth.nextDouble(this.random, -VERTICAL_RANGE, VERTICAL_RANGE + 1)),
					Mth.floor(Mth.nextDouble(this.random, -HORIZONTAL_RANGE, HORIZONTAL_RANGE))
			);
			if (this.level().isEmptyBlock(candidate) && this.level().isEmptyBlock(candidate.above())) {
				return candidate;
			}
		}
		return home;
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (!this.hasItems() || !this.eyeContact.isWispFacing(player)) {
			return InteractionResult.PASS;
		}
		if (!this.level().isClientSide()) {
			if (!this.eyeContact.isHeldBy(player)) {
				return InteractionResult.PASS;
			}
			WispEffects.glints(this, this.getVariant(), RETRIEVE_GLINTS);
			this.inventory.giveTo(player);
			this.playSound(KeeperWispsSounds.WISP_RETRIEVE, 1.0F, 1.0F);
		}
		//? if >=1.21.2 {
		return InteractionResult.SUCCESS;
		//?} else {
		/*return InteractionResult.sidedSuccess(this.level().isClientSide());
		*///?}
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		super.onSyncedDataUpdated(key);
		if (!DATA_VARIANT.equals(key) || !this.level().isClientSide()) {
			return;
		}
		WispVariant variant = this.getVariant();
		if (variant == WispVariant.GREEN && this.lastSyncedVariant != null && this.lastSyncedVariant != WispVariant.GREEN) {
			this.retrievePulseTicks = RETRIEVE_PULSE_TICKS;
		}
		this.lastSyncedVariant = variant;
	}

	//? if >=1.21.2 {
	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			return super.hurtServer(level, source, amount);
		}
		if (!(source.getDirectEntity() instanceof Player player) || player.isSpectator()) {
			return false;
		}
		return super.hurtServer(level, source, Float.MAX_VALUE);
	}
	//?} else {
	/*@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			return super.hurt(source, amount);
		}
		if (!(source.getDirectEntity() instanceof Player player) || player.isSpectator()) {
			return false;
		}
		return super.hurt(source, Float.MAX_VALUE);
	}
	*///?}

	@Override
	public void handleDamageEvent(DamageSource source) {
		super.handleDamageEvent(source);
		this.hurtTime = 0;
	}

	@Override
	public void die(DamageSource source) {
		boolean wasDead = this.dead;
		super.die(source);
		if (!wasDead && this.dead && this.level() instanceof ServerLevel serverLevel) {
			this.inventory.dropAll(serverLevel);
		}
	}

	@Override
	protected void tickDeath() {
		this.dyingTicks++;
		this.setDeltaMovement(Vec3.ZERO);
		if (this.level().isClientSide() || this.isRemoved()) {
			return;
		}
		if (this.dyingTicks <= DEATH_GLINT_TICKS) {
			WispVariant[] variants = WispVariant.values();
			WispEffects.glints(this, variants[this.dyingTicks % variants.length], 1);
		}
		if (this.dyingTicks >= DEATH_TICKS) {
			this.remove(RemovalReason.KILLED);
		}
	}

	@Override
	public void travel(Vec3 input) {
		//? if >=1.21.5 {
		boolean local = this.isLocalInstanceAuthoritative();
		//?} else {
		/*boolean local = this.isControlledByLocalInstance();
		*///?}
		if (local) {
			this.moveRelative(this.getSpeed(), input);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.91F));
		}
		this.calculateEntityAnimation(false);
	}

	@Override
	protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
	}

	@Override
	public boolean canBeAffected(MobEffectInstance effect) {
		return false;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected void doPush(Entity entity) {
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
	}

	//? if >=1.21 {
	@Override
	public boolean canBeLeashed() {
		return false;
	}
	//?} else {
	/*@Override
	public boolean canBeLeashed(Player player) {
		return false;
	}
	*///?}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public void playAmbientSound() {
		if (!this.isReady()) {
			super.playAmbientSound();
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.hasItems() ? KeeperWispsSounds.WISP_AMBIENT : KeeperWispsSounds.WISP_AMBIENT_EMPTY;
	}

	@Override
	public int getAmbientSoundInterval() {
		return AMBIENT_SOUND_INTERVAL_MIN + this.random.nextInt(AMBIENT_SOUND_INTERVAL_RANDOM);
	}

	@Override
	protected SoundEvent getDeathSound() {
		return KeeperWispsSounds.WISP_DEATH;
	}

	@Override
	@Nullable
	protected SoundEvent getHurtSound(DamageSource source) {
		return null;
	}

	//? if >=1.21.6 {
	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		this.inventory.save(output);
		output.putString(VARIANT_KEY, this.getVariant().getSerializedName());
		if (this.homePos != null) {
			output.putIntArray(HOME_KEY, new int[]{this.homePos.getX(), this.homePos.getY(), this.homePos.getZ()});
		}
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.inventory.load(input);
		input.getIntArray(HOME_KEY)
				.filter(home -> home.length == 3)
				.ifPresent(home -> this.homePos = new BlockPos(home[0], home[1], home[2]));
		WispVariant variant = WispVariant.byName(input.getStringOr(VARIANT_KEY, ""));
		if (variant != null) {
			this.setVariant(variant);
		}
	}
	//?} else {
	/*@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		this.inventory.save(tag);
		tag.putString(VARIANT_KEY, this.getVariant().getSerializedName());
		if (this.homePos != null) {
			tag.putIntArray(HOME_KEY, new int[]{this.homePos.getX(), this.homePos.getY(), this.homePos.getZ()});
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.inventory.load(tag);
		int[] home = tag.getIntArray(HOME_KEY);
		if (home.length == 3) {
			this.homePos = new BlockPos(home[0], home[1], home[2]);
		}
		WispVariant variant = WispVariant.byName(tag.getString(VARIANT_KEY));
		if (variant != null) {
			this.setVariant(variant);
		}
	}
	*///?}

}
