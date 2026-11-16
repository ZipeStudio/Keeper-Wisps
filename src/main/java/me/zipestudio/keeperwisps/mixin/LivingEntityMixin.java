package me.zipestudio.keeperwisps.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.zipestudio.keeperwisps.death.WispSpawner;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Inject(method = "dropAllDeathLoot", at = @At("HEAD"))
	private void keeperwisps$beginDeath(CallbackInfo ci) {
		if ((Object) this instanceof ServerPlayer player) {
			WispSpawner.beginDeath(player);
		}
	}

	@ModifyArg(method = "dropExperience", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"), index = 2)
	private int keeperwisps$takeExperience(int amount) {
		return WispSpawner.takeExperience((LivingEntity) (Object) this, amount);
	}

	@Inject(method = "dropAllDeathLoot", at = @At("TAIL"))
	private void keeperwisps$finishDeath(CallbackInfo ci) {
		if ((Object) this instanceof ServerPlayer player) {
			WispSpawner.finishDeath(player);
		}
	}

}
