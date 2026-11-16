package me.zipestudio.keeperwisps.entity;

import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
//? if >=26.1 {
import net.minecraft.world.item.ItemStackTemplate;
//?}
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import me.zipestudio.keeperwisps.registry.KeeperWispsParticles;

public final class WispEffects {

	private static final double GLINT_SPEED = 0.09D;
	private static final double GLINT_START_RADIUS = 0.45D;
	private static final int ITEM_PARTICLES_PER_STACK = 4;
	private static final double SPARK_RISE = 0.035D;
	private static final double SPARK_SPREAD = 0.012D;
	private static final double SPARK_RADIUS = 0.35D;

	private WispEffects() {
	}

	public static void glints(WispEntity wisp, WispVariant variant, int count) {
		glints(wisp, variant, count, null);
	}

	public static void glints(WispEntity wisp, WispVariant variant, int count, @Nullable ServerPlayer target) {
		if (!(wisp.level() instanceof ServerLevel level)) {
			return;
		}
		Vec3 center = center(wisp);
		for (int i = 0; i < count; i++) {
			Vec3 direction = new Vec3(wisp.getRandom().nextGaussian(), wisp.getRandom().nextGaussian(), wisp.getRandom().nextGaussian()).normalize();
			Vec3 velocity = direction.scale(GLINT_SPEED * (0.5D + wisp.getRandom().nextDouble() * 0.5D));
			Vec3 from = center.add(direction.scale(GLINT_START_RADIUS));
			SimpleParticleType particle = KeeperWispsParticles.glint(variant);
			if (target != null) {
				//? if >=1.21.4 {
				level.sendParticles(target, particle, false, false, from.x, from.y, from.z, 0, velocity.x, velocity.y, velocity.z, 1.0D);
				//?} else {
				/*level.sendParticles(target, particle, false, from.x, from.y, from.z, 0, velocity.x, velocity.y, velocity.z, 1.0D);
				*///?}
			} else {
				level.sendParticles(particle, from.x, from.y, from.z, 0, velocity.x, velocity.y, velocity.z, 1.0D);
			}
		}
	}

	public static void sparks(WispEntity wisp, WispVariant variant, int count, @Nullable ServerPlayer target) {
		if (!(wisp.level() instanceof ServerLevel level)) {
			return;
		}
		Vec3 center = center(wisp);
		for (int i = 0; i < count; i++) {
			double angle = wisp.getRandom().nextDouble() * Math.PI * 2.0D;
			double radius = SPARK_RADIUS * (0.6D + wisp.getRandom().nextDouble() * 0.4D);
			Vec3 from = center.add(Math.cos(angle) * radius, (wisp.getRandom().nextDouble() - 0.5D) * 0.5D, Math.sin(angle) * radius);
			Vec3 velocity = new Vec3(Math.cos(angle) * SPARK_SPREAD, SPARK_RISE * (0.7D + wisp.getRandom().nextDouble() * 0.6D), Math.sin(angle) * SPARK_SPREAD);
			SimpleParticleType particle = KeeperWispsParticles.spark(variant);
			if (target != null) {
				//? if >=1.21.4 {
				level.sendParticles(target, particle, false, false, from.x, from.y, from.z, 0, velocity.x, velocity.y, velocity.z, 1.0D);
				//?} else {
				/*level.sendParticles(target, particle, false, from.x, from.y, from.z, 0, velocity.x, velocity.y, velocity.z, 1.0D);
				*///?}
			} else {
				level.sendParticles(particle, from.x, from.y, from.z, 0, velocity.x, velocity.y, velocity.z, 1.0D);
			}
		}
	}

	public static void itemTrail(WispEntity wisp, Player player, ItemStack stack) {
		if (!(wisp.level() instanceof ServerLevel level)) {
			return;
		}
		Vec3 from = center(wisp);
		Vec3 toPlayer = player.position().add(0.0D, player.getBbHeight() * 0.6D, 0.0D).subtract(from);
		//? if >=26.1 {
		ItemParticleOption particle = new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(stack));
		//?} else {
		/*ItemParticleOption particle = new ItemParticleOption(ParticleTypes.ITEM, stack);
		*///?}
		for (int i = 0; i < ITEM_PARTICLES_PER_STACK; i++) {
			Vec3 velocity = toPlayer.scale(0.1D + wisp.getRandom().nextDouble() * 0.04D)
					.add(wisp.getRandom().nextGaussian() * 0.02D, 0.1D + wisp.getRandom().nextDouble() * 0.05D, wisp.getRandom().nextGaussian() * 0.02D);
			level.sendParticles(particle, from.x, from.y, from.z, 0, velocity.x, velocity.y, velocity.z, 1.0D);
		}
	}

	private static Vec3 center(WispEntity wisp) {
		return wisp.position().add(0.0D, wisp.getBbHeight() / 2.0D, 0.0D);
	}

}
