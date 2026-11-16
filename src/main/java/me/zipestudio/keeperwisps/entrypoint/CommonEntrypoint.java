package me.zipestudio.keeperwisps.entrypoint;

//? if fabric {

import me.zipestudio.keeperwisps.KeeperWisps;
import me.zipestudio.keeperwisps.entity.*;
import me.zipestudio.keeperwisps.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class CommonEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		KeeperWisps.onInitialize();
		Registry.register(BuiltInRegistries.ENTITY_TYPE, KeeperWispsEntities.WISP_ID, KeeperWispsEntities.WISP);
		FabricDefaultAttributeRegistry.register(KeeperWispsEntities.WISP, WispEntity.createAttributes());
		for (WispVariant variant : WispVariant.values()) {
			Registry.register(BuiltInRegistries.PARTICLE_TYPE, KeeperWispsParticles.glintId(variant), KeeperWispsParticles.glint(variant));
			Registry.register(BuiltInRegistries.PARTICLE_TYPE, KeeperWispsParticles.sparkId(variant), KeeperWispsParticles.spark(variant));
		}
		KeeperWispsSounds.getSounds().forEach((id, sound) -> Registry.register(BuiltInRegistries.SOUND_EVENT, id, sound));
	}
}

//?} elif neoforge {
/*import me.zipestudio.keeperwisps.KeeperWisps;
import me.zipestudio.keeperwisps.entity.*;
import me.zipestudio.keeperwisps.registry.*;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(KeeperWisps.MOD_ID)
public class CommonEntrypoint {

	public CommonEntrypoint(IEventBus modBus) {
		KeeperWisps.onInitialize();
		modBus.addListener(RegisterEvent.class, event -> {
			event.register(Registries.ENTITY_TYPE, helper -> helper.register(KeeperWispsEntities.WISP_ID, KeeperWispsEntities.WISP));
			event.register(Registries.PARTICLE_TYPE, helper -> {
				for (WispVariant variant : WispVariant.values()) {
					helper.register(KeeperWispsParticles.glintId(variant), KeeperWispsParticles.glint(variant));
					helper.register(KeeperWispsParticles.sparkId(variant), KeeperWispsParticles.spark(variant));
				}
			});
			event.register(Registries.SOUND_EVENT, helper -> KeeperWispsSounds.getSounds().forEach(helper::register));
		});
		modBus.addListener(EntityAttributeCreationEvent.class, event ->
				event.put(KeeperWispsEntities.WISP, WispEntity.createAttributes().build()));
	}

}

*///?} elif forge {
/*import me.zipestudio.keeperwisps.KeeperWisps;
import me.zipestudio.keeperwisps.entity.*;
import me.zipestudio.keeperwisps.registry.*;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

@Mod(KeeperWisps.MOD_ID)
public class CommonEntrypoint {

	public CommonEntrypoint() {
		KeeperWisps.onInitialize();
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(EventPriority.NORMAL, false, RegisterEvent.class, event -> {
			event.register(Registries.ENTITY_TYPE, helper -> helper.register(KeeperWispsEntities.WISP_ID, KeeperWispsEntities.WISP));
			event.register(Registries.PARTICLE_TYPE, helper -> {
				for (WispVariant variant : WispVariant.values()) {
					helper.register(KeeperWispsParticles.glintId(variant), KeeperWispsParticles.glint(variant));
					helper.register(KeeperWispsParticles.sparkId(variant), KeeperWispsParticles.spark(variant));
				}
			});
			event.register(Registries.SOUND_EVENT, helper -> KeeperWispsSounds.getSounds().forEach(helper::register));
		});
		modBus.addListener(EventPriority.NORMAL, false, EntityAttributeCreationEvent.class, event ->
				event.put(KeeperWispsEntities.WISP, WispEntity.createAttributes().build()));
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientEntrypoint::onInitializeClient);
	}

}

*///?}
