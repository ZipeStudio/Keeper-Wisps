package me.zipestudio.keeperwisps.entity.ai;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;

import me.zipestudio.keeperwisps.entity.WispEntity;

public class WispWanderGoal extends Goal {

	private static final double FLY_SPEED = 0.12D;
	private static final int WANDER_INTERVAL = 60;

	private final WispEntity wisp;

	public WispWanderGoal(WispEntity wisp) {
		this.wisp = wisp;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.wisp.getHomePos() == null || this.wisp.getMoveControl().hasWanted()) {
			return false;
		}
		return this.wisp.isWanderPending()
				|| this.wisp.isOutsideHome()
				|| this.wisp.getRandom().nextInt(reducedTickDelay(WANDER_INTERVAL)) == 0;
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	@Override
	public void start() {
		this.wisp.onWanderStarted();
		BlockPos target = this.wisp.findWanderTarget();
		this.wisp.getMoveControl().setWantedPosition(target.getX() + 0.5D, target.getY() + 0.25D, target.getZ() + 0.5D, FLY_SPEED);
	}

}
