package me.zipestudio.keeperwisps.entity;

import java.util.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import me.zipestudio.keeperwisps.registry.KeeperWispsSounds;

public class WispEyeContact {

	public static final int REQUIRED_TICKS = 20;

	private static final double RANGE = 6.0D;
	private static final double WISP_LOOK_DOT = 0.7071D;
	private static final double PLAYER_LOOK_DOT = 0.8D;
	private static final int GLINTS_ON_START = 4;
	private static final int GLINT_INTERVAL = 5;

	private final WispEntity wisp;
	private final Map<UUID, Integer> ticks = new HashMap<>();

	public WispEyeContact(WispEntity wisp) {
		this.wisp = wisp;
	}

	public void tick() {
		Map<UUID, Integer> updated = new HashMap<>();
		for (Player player : this.wisp.level().getEntitiesOfClass(Player.class, this.wisp.getBoundingBox().inflate(RANGE))) {
			if (player.isSpectator() || !this.isWispFacing(player) || !this.isPlayerLooking(player)) {
				continue;
			}
			int held = this.ticks.getOrDefault(player.getUUID(), 0) + 1;
			updated.put(player.getUUID(), held);
			if (held < REQUIRED_TICKS || !this.wisp.hasItems() || !(player instanceof ServerPlayer serverPlayer)) {
				continue;
			}
			if (held == REQUIRED_TICKS) {
				this.wisp.level().playSound(null, this.wisp.getX(), this.wisp.getY(), this.wisp.getZ(), KeeperWispsSounds.WISP_EYE_CONTACT, this.wisp.getSoundSource(), 1.0F, 1.0F);
				WispEffects.sparks(this.wisp, this.wisp.getVariant(), GLINTS_ON_START, serverPlayer);
			} else if (held % GLINT_INTERVAL == 0) {
				WispEffects.sparks(this.wisp, this.wisp.getVariant(), 1, serverPlayer);
			}
		}
		this.ticks.clear();
		this.ticks.putAll(updated);
		this.wisp.setReady(this.wisp.hasItems() && updated.values().stream().anyMatch(held -> held >= REQUIRED_TICKS));
	}

	public boolean isHeldBy(Player player) {
		return this.ticks.getOrDefault(player.getUUID(), 0) >= REQUIRED_TICKS;
	}

	public boolean isWispFacing(Player player) {
		float yaw = this.wisp.getYHeadRot() * Mth.DEG_TO_RAD;
		Vec3 view = new Vec3(-Mth.sin(yaw), 0.0D, Mth.cos(yaw));
		Vec3 toPlayer = new Vec3(player.getX() - this.wisp.getX(), 0.0D, player.getZ() - this.wisp.getZ());
		if (toPlayer.lengthSqr() < 1.0E-4D) {
			return false;
		}
		return view.dot(toPlayer.normalize()) > WISP_LOOK_DOT;
	}

	private boolean isPlayerLooking(Player player) {
		Vec3 toWisp = this.wisp.position().add(0.0D, this.wisp.getBbHeight() / 2.0D, 0.0D).subtract(player.getEyePosition()).normalize();
		return player.getViewVector(1.0F).dot(toWisp) > PLAYER_LOOK_DOT;
	}

}
