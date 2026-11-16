package me.zipestudio.keeperwisps.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

import me.zipestudio.keeperwisps.entity.WispEntity;

public class WispMoveControl extends MoveControl {

	private static final double ACCELERATION = 0.05D;
	private static final float TURN_SPEED = 8.0F;

	private final WispEntity wisp;

	public WispMoveControl(WispEntity wisp) {
		super(wisp);
		this.wisp = wisp;
	}

	@Override
	public void tick() {
		if (this.operation != Operation.MOVE_TO) {
			return;
		}
		Vec3 delta = new Vec3(this.wantedX - this.wisp.getX(), this.wantedY - this.wisp.getY(), this.wantedZ - this.wisp.getZ());
		double distance = delta.length();
		if (distance < this.wisp.getBoundingBox().getSize()) {
			this.operation = Operation.WAIT;
			this.wisp.setDeltaMovement(this.wisp.getDeltaMovement().scale(0.5D));
			return;
		}
		this.wisp.setDeltaMovement(this.wisp.getDeltaMovement().add(delta.scale(this.speedModifier * ACCELERATION / distance)));
		Vec3 motion = this.wisp.getDeltaMovement();
		float yaw = -((float) Mth.atan2(motion.x, motion.z)) * Mth.RAD_TO_DEG;
		this.wisp.setYRot(Mth.approachDegrees(this.wisp.getYRot(), yaw, TURN_SPEED));
		this.wisp.yBodyRot = this.wisp.getYRot();
	}

}
