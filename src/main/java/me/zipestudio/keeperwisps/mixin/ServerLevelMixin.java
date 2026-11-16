package me.zipestudio.keeperwisps.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.zipestudio.keeperwisps.death.WispSpawner;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

	@Inject(method = "addFreshEntity", at = @At("HEAD"), cancellable = true)
	private void keeperwisps$captureDeathDrop(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (WispSpawner.captureDrop(entity)) {
			cir.setReturnValue(true);
		}
	}

}
