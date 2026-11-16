package me.zipestudio.keeperwisps.entity.ai;

import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;

import me.zipestudio.keeperwisps.entity.WispEntity;

public class WispRandomLookAroundGoal extends RandomLookAroundGoal {

	private final WispEntity wisp;

	public WispRandomLookAroundGoal(WispEntity wisp) {
		super(wisp);
		this.wisp = wisp;
	}

	@Override
	public boolean canUse() {
		return !this.wisp.getMoveControl().hasWanted() && super.canUse();
	}

}
