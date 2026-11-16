package me.zipestudio.keeperwisps.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.LookControl;

import me.zipestudio.keeperwisps.entity.WispEntity;

public class WispLookControl extends LookControl {

	private static final float HEAD_TURN_SPEED = 10.0F;
	private static final float BODY_TURN_SPEED = 8.0F;

	private final WispEntity wisp;

	public WispLookControl(WispEntity wisp) {
		super(wisp);
		this.wisp = wisp;
	}

	@Override
	public void tick() {
		if (this.wisp.getMoveControl().hasWanted() && !this.isLookingAtTarget()) {
			this.wisp.setYHeadRot(Mth.approachDegrees(this.wisp.getYHeadRot(), this.wisp.getYRot(), HEAD_TURN_SPEED));
			this.wisp.setXRot(0.0F);
			return;
		}
		super.tick();
		this.wisp.setYRot(Mth.approachDegrees(this.wisp.getYRot(), this.wisp.getYHeadRot(), BODY_TURN_SPEED));
		this.wisp.yBodyRot = this.wisp.getYRot();
	}

}
