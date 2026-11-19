package me.zipestudio.keeperwisps.entity;

import java.util.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import me.zipestudio.keeperwisps.config.KeeperWispsConfig;
import me.zipestudio.keeperwisps.registry.KeeperWispsSounds;

public class WispEyeContact {

	public static final int REQUIRED_TICKS = 20;

	private static final double RANGE = 6.0D;
	private static final double WISP_LOOK_DOT = 0.7071D;
	private static final double LOOK_MARGIN = 0.2D;
	private static final int LOST_GRACE_TICKS = 10;
	private static final int GLINTS_ON_START = 4;
	private static final int GLINT_INTERVAL = 5;
	//? if <1.21 {
	/*private static final double ENTITY_REACH = 3.0D;
	*///?}

	private final WispEntity wisp;
	private final Map<UUID, Integer> ticks = new HashMap<>();
	private final Map<UUID, Integer> lostTicks = new HashMap<>();

	public WispEyeContact(WispEntity wisp) {
		this.wisp = wisp;
	}

	public void tick() {
		boolean eyeContact = KeeperWispsConfig.getInstance().isRequireEyeContact();
		int requiredTicks = requiredTicks();
		Map<UUID, Integer> updated = new HashMap<>();
		Map<UUID, Integer> updatedLost = new HashMap<>();
		for (Player player : this.wisp.level().getEntitiesOfClass(Player.class, this.wisp.getBoundingBox().inflate(RANGE))) {
			if (player.isSpectator()) {
				continue;
			}
			UUID uuid = player.getUUID();
			boolean looking = this.canReach(player) && this.isPlayerLooking(player) && (!eyeContact || this.isWispFacing(player));
			if (!looking) {
				int lost = this.lostTicks.getOrDefault(uuid, 0) + 1;
				if (this.ticks.containsKey(uuid) && lost <= LOST_GRACE_TICKS) {
					updated.put(uuid, this.ticks.get(uuid));
					updatedLost.put(uuid, lost);
				}
				continue;
			}

			int held = this.ticks.getOrDefault(uuid, 0) + 1;
			updated.put(uuid, held);
			if (held < requiredTicks || !this.wisp.hasItems() || !(player instanceof ServerPlayer serverPlayer)) {
				continue;
			}
			if (held == requiredTicks) {
				this.wisp.level().playSound(null, this.wisp.getX(), this.wisp.getY(), this.wisp.getZ(), KeeperWispsSounds.WISP_EYE_CONTACT, this.wisp.getSoundSource(), 1.0F, 1.0F);
				WispEffects.sparks(this.wisp, this.wisp.getVariant(), GLINTS_ON_START, serverPlayer);
			} else if (held % GLINT_INTERVAL == 0) {
				WispEffects.sparks(this.wisp, this.wisp.getVariant(), 1, serverPlayer);
			}
		}
		this.ticks.clear();
		this.ticks.putAll(updated);
		this.lostTicks.clear();
		this.lostTicks.putAll(updatedLost);
		this.wisp.setReady(this.wisp.hasItems() && updated.values().stream().anyMatch(held -> held >= requiredTicks));
	}

	public boolean isHeldBy(Player player) {
		return this.ticks.getOrDefault(player.getUUID(), 0) >= requiredTicks();
	}

	private static int requiredTicks() {
		return KeeperWispsConfig.getInstance().isRequireEyeContact() ? REQUIRED_TICKS : 1;
	}

	private boolean isWispFacing(Player player) {
		float yaw = this.wisp.getYHeadRot() * Mth.DEG_TO_RAD;
		Vec3 view = new Vec3(-Mth.sin(yaw), 0.0D, Mth.cos(yaw));
		Vec3 toPlayer = new Vec3(player.getX() - this.wisp.getX(), 0.0D, player.getZ() - this.wisp.getZ());
		if (toPlayer.lengthSqr() < 1.0E-4D) {
			return false;
		}
		return view.dot(toPlayer.normalize()) > WISP_LOOK_DOT;
	}

	private boolean canReach(Player player) {
		//? if >=1.21.11 {
		return player.isWithinEntityInteractionRange(this.wisp, 0.0D);
		//?} else if >=1.21 {
		/*return player.canInteractWithEntity(this.wisp, 0.0D);
		*///?} else {
		/*return this.wisp.getBoundingBox().distanceToSqr(player.getEyePosition()) < ENTITY_REACH * ENTITY_REACH;
		*///?}
	}

	private boolean isPlayerLooking(Player player) {
		Vec3 eye = player.getEyePosition();
		Vec3 end = eye.add(player.getViewVector(1.0F).scale(RANGE));
		return this.wisp.getBoundingBox().inflate(LOOK_MARGIN).clip(eye, end).isPresent();
	}

}
