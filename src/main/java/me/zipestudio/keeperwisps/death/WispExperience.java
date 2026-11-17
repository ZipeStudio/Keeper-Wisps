package me.zipestudio.keeperwisps.death;

import net.minecraft.util.Mth;

import me.zipestudio.keeperwisps.config.KeeperWispsConfig;

public final class WispExperience {

	private WispExperience() {
	}

	public static int forLevel(int level) {
		if (level >= 30) {
			return 112 + (level - 30) * 9;
		}
		if (level >= 15) {
			return 37 + (level - 15) * 5;
		}
		return 7 + level * 2;
	}

	public static int forWisp(int vanillaXp, int index) {
		int left = vanillaXp;
		for (int i = 0; i < index; i++) {
			left -= forLevel(i);
		}
		return Mth.clamp(left, 0, forLevel(index));
	}

	public static int floor(int vanillaXp, int wisps) {
		int xp = vanillaXp;
		for (int i = 0; i < wisps; i++) {
			xp -= forWisp(vanillaXp, i);
		}
		return xp;
	}

	public static int lastWispLevel() {
		KeeperWispsConfig config = KeeperWispsConfig.getInstance();
		int first = Math.max(0, config.getFirstWispLevel());
		int step = Math.max(0, config.getLevelsPerWisp());
		return first + (InventoryRow.values().length - 1) * step;
	}

	public static int excess(int experienceLevel, float experienceProgress) {
		int threshold = lastWispLevel();
		if (experienceLevel < threshold) {
			return 0;
		}
		int xp = 0;
		for (int level = threshold; level < experienceLevel; level++) {
			xp += forLevel(level);
		}
		xp += Math.round(experienceProgress * forLevel(experienceLevel));
		float refund = Mth.clamp(KeeperWispsConfig.getInstance().getExcessXpRefund(), 0.0F, 1.0F);
		return Math.round(xp * refund);
	}

	public static int share(int xp, int wisps, int index) {
		return xp / wisps + (index < xp % wisps ? 1 : 0);
	}

}
