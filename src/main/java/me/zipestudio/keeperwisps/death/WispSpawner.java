package me.zipestudio.keeperwisps.death;

import java.util.*;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
//? if >=1.21.11 {
import net.minecraft.world.level.gamerules.GameRules;
//?}
import org.jetbrains.annotations.Nullable;

import me.zipestudio.keeperwisps.compat.*;
import me.zipestudio.keeperwisps.config.KeeperWispsConfig;
import me.zipestudio.keeperwisps.entity.WispEntity;
import me.zipestudio.keeperwisps.registry.*;

public final class WispSpawner {

	private static final double SPAWN_SPREAD = 0.5D;

	@Nullable
	private static DeathCapture capture;
	private static int capturedXp;

	private WispSpawner() {
	}

	public static void beginDeath(ServerPlayer player) {
		if (capture != null || !canSpawnWisps(player)) {
			return;
		}
		int experienceLevel = player.experienceLevel;
		float experienceProgress = player.experienceProgress;
		int wisps = wispCount(experienceLevel);
		if (wisps <= 0) {
			return;
		}

		//? if >=1.21.6 {
		ServerLevel level = player.level();
		//?} else {
		/*ServerLevel level = player.serverLevel();
		*///?}
		int excessXp = WispExperience.excess(experienceLevel, experienceProgress);
		capturedXp = 0;
		capture = new DeathCapture(player, wisps, getHomePos(level, player), excessXp, snapshotSlots(player.getInventory()), AccessoryCompats.collect(player), new ArrayList<>());
	}

	public static boolean captureDrop(Entity entity) {
		if (capture == null || !(entity instanceof ItemEntity item)) {
			return false;
		}
		capture.drops().add(item);
		return true;
	}

	public static int takeExperience(LivingEntity entity, int amount) {
		if (capture == null || capture.player() != entity) {
			return amount;
		}
		capturedXp = amount;
		return WispExperience.floor(amount, capture.wisps());
	}

	public static void finishDeath(ServerPlayer player) {
		DeathCapture finished = capture;
		if (finished == null || finished.player() != player) {
			return;
		}
		capture = null;

		InventoryRow[] rows = InventoryRow.values();
		List<List<ItemStack>> contents = new ArrayList<>();
		for (int i = 0; i < finished.wisps(); i++) {
			contents.add(new ArrayList<>(Collections.nCopies(rows[i].size(), ItemStack.EMPTY)));
		}
		boolean hasEquipmentWisp = finished.wisps() > InventoryRow.EQUIPMENT.ordinal();
		List<ItemStack> equipment = hasEquipmentWisp ? contents.get(InventoryRow.EQUIPMENT.ordinal()) : List.of();
		Map<Integer, AccessorySlot> accessorySlots = new HashMap<>();
		List<ItemEntity> remaining = new ArrayList<>();

		for (ItemEntity drop : finished.drops()) {
			ItemStack stack = drop.getItem();
			DeathCapture.SlotRef slot = finished.slots().get(stack);
			if (slot != null && slot.row().ordinal() < finished.wisps()) {
				contents.get(slot.row().ordinal()).set(slot.index(), stack);
			} else if (slot == null && hasEquipmentWisp) {
				AccessorySlot accessorySlot = finished.accessorySlots().get(stack);
				if (accessorySlot != null) {
					accessorySlots.put(equipment.size(), accessorySlot);
				}
				equipment.add(stack);
			} else {
				remaining.add(drop);
			}
		}

		//? if >=1.21.6 {
		ServerLevel level = player.level();
		//?} else {
		/*ServerLevel level = player.serverLevel();
		*///?}
		for (ItemEntity drop : remaining) {
			level.addFreshEntity(drop);
		}
		spawnWisps(level, finished, contents, accessorySlots);
	}

	private static void spawnWisps(ServerLevel level, DeathCapture capture, List<List<ItemStack>> contents, Map<Integer, AccessorySlot> accessorySlots) {
		InventoryRow[] rows = InventoryRow.values();
		BlockPos homePos = capture.homePos();
		level.playSound(null, homePos, KeeperWispsSounds.WISP_APPEAR, SoundSource.NEUTRAL, 1.0F, 1.0F);
		RandomSource random = level.getRandom();
		for (int i = 0; i < capture.wisps(); i++) {
			//? if >=1.21.2 {
			WispEntity wisp = KeeperWispsEntities.WISP.create(level, EntitySpawnReason.MOB_SUMMONED);
			//?} else {
			/*WispEntity wisp = KeeperWispsEntities.WISP.create(level);
			*///?}
			if (wisp == null) {
				return;
			}
			int experience = WispExperience.forWisp(capturedXp, i) + WispExperience.share(capture.excessXp(), capture.wisps(), i);
			wisp.setup(homePos, rows[i], contents.get(i), experience);
			if (rows[i] == InventoryRow.EQUIPMENT) {
				wisp.setAccessorySlots(accessorySlots);
			}
			//? if >=1.21.5 {
			wisp.snapTo(
			//?} else {
			/*wisp.moveTo(
			*///?}
					homePos.getX() + 0.5D + Mth.nextDouble(random, -SPAWN_SPREAD, SPAWN_SPREAD),
					homePos.getY() + Mth.nextDouble(random, 0.0D, SPAWN_SPREAD),
					homePos.getZ() + 0.5D + Mth.nextDouble(random, -SPAWN_SPREAD, SPAWN_SPREAD),
					random.nextFloat() * 360.0F,
					0.0F
			);
			level.addFreshEntity(wisp);
		}
	}

	private static int wispCount(int experienceLevel) {
		KeeperWispsConfig config = KeeperWispsConfig.getInstance();
		int first = Math.max(0, config.getFirstWispLevel());
		int step = Math.max(0, config.getLevelsPerWisp());
		if (experienceLevel < first) {
			return 0;
		}
		if (step == 0) {
			return InventoryRow.values().length;
		}
		return Math.min(InventoryRow.values().length, (experienceLevel - first) / step + 1);
	}

	private static boolean canSpawnWisps(ServerPlayer player) {
		//? if >=1.21.11 {
		boolean keepInventory = player.level().getGameRules().get(GameRules.KEEP_INVENTORY);
		//?} else if >=1.21.6 {
		/*boolean keepInventory = player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
		*///?} else {
		/*boolean keepInventory = player.serverLevel().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
		*///?}
		return player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL && !keepInventory;
	}

	private static BlockPos getHomePos(ServerLevel level, ServerPlayer player) {
		BlockPos eyePos = BlockPos.containing(player.getEyePosition());
		//? if >=1.21.2 {
		int y = Mth.clamp(eyePos.getY(), level.getMinY() + 1, level.getMaxY() - 1);
		//?} else {
		/*int y = Mth.clamp(eyePos.getY(), level.getMinBuildHeight() + 1, level.getMaxBuildHeight() - 1);
		*///?}
		return new BlockPos(eyePos.getX(), y, eyePos.getZ());
	}

	private static Map<ItemStack, DeathCapture.SlotRef> snapshotSlots(Inventory inventory) {
		Map<ItemStack, DeathCapture.SlotRef> slots = new IdentityHashMap<>();
		for (InventoryRow row : InventoryRow.values()) {
			for (int i = 0; i < row.size(); i++) {
				ItemStack stack = row.get(inventory, i);
				if (!stack.isEmpty()) {
					slots.put(stack, new DeathCapture.SlotRef(row, i));
				}
			}
		}
		return slots;
	}

}
