package me.zipestudio.keeperwisps.entrypoint;

//? if fabric {

import me.zipestudio.keeperwisps.client.KeeperWispsClient;
import me.zipestudio.keeperwisps.client.model.WispModel;
import me.zipestudio.keeperwisps.client.particle.*;
import me.zipestudio.keeperwisps.client.renderer.WispRenderer;
import me.zipestudio.keeperwisps.entity.WispVariant;
import me.zipestudio.keeperwisps.registry.*;
import net.fabricmc.api.ClientModInitializer;
//? if >=26.1 {
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
//?} else {
/*import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
*///?}
import net.fabricmc.fabric.api.client.rendering.v1.*;

public class ClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		KeeperWispsClient.onInitializeClient();
		//? if >=26.1 {
		ModelLayerRegistry.registerModelLayer(WispModel.LAYER, WispModel::createBodyLayer);
		//?} else {
		/*EntityModelLayerRegistry.registerModelLayer(WispModel.LAYER, WispModel::createBodyLayer);
		*///?}
		EntityRendererRegistry.register(KeeperWispsEntities.WISP, WispRenderer::new);
		for (WispVariant variant : WispVariant.values()) {
			//? if >=26.1 {
			ParticleProviderRegistry particles = ParticleProviderRegistry.getInstance();
			//?} else {
			/*ParticleFactoryRegistry particles = ParticleFactoryRegistry.getInstance();
			*///?}
			particles.register(KeeperWispsParticles.glint(variant), sprites -> new GlintParticle.Provider(sprites, variant.getColor()));
			particles.register(KeeperWispsParticles.spark(variant), sprites -> new SparkParticle.Provider(sprites, variant.getColor()));
		}
	}

}

//?} elif neoforge {
/*import me.zipestudio.keeperwisps.KeeperWisps;
import me.zipestudio.keeperwisps.client.KeeperWispsClient;
import me.zipestudio.keeperwisps.client.model.WispModel;
import me.zipestudio.keeperwisps.client.particle.*;
import me.zipestudio.keeperwisps.client.renderer.WispRenderer;
import me.zipestudio.keeperwisps.entity.WispVariant;
import me.zipestudio.keeperwisps.registry.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.*;

@Mod(value = KeeperWisps.MOD_ID, dist = Dist.CLIENT)
public class ClientEntrypoint {

	public ClientEntrypoint(IEventBus modBus) {
		KeeperWispsClient.onInitializeClient();
		modBus.addListener(EntityRenderersEvent.RegisterLayerDefinitions.class, event ->
				event.registerLayerDefinition(WispModel.LAYER, WispModel::createBodyLayer));
		modBus.addListener(EntityRenderersEvent.RegisterRenderers.class, event ->
				event.registerEntityRenderer(KeeperWispsEntities.WISP, WispRenderer::new));
		modBus.addListener(RegisterParticleProvidersEvent.class, event -> {
			for (WispVariant variant : WispVariant.values()) {
				event.registerSpriteSet(KeeperWispsParticles.glint(variant), sprites -> new GlintParticle.Provider(sprites, variant.getColor()));
				event.registerSpriteSet(KeeperWispsParticles.spark(variant), sprites -> new SparkParticle.Provider(sprites, variant.getColor()));
			}
		});
	}

}

*///?} elif forge {
/*import me.zipestudio.keeperwisps.client.KeeperWispsClient;
import me.zipestudio.keeperwisps.client.model.WispModel;
import me.zipestudio.keeperwisps.client.particle.*;
import me.zipestudio.keeperwisps.client.renderer.WispRenderer;
import me.zipestudio.keeperwisps.entity.WispVariant;
import me.zipestudio.keeperwisps.registry.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientEntrypoint {

	public static void onInitializeClient() {
		KeeperWispsClient.onInitializeClient();
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(EventPriority.NORMAL, false, EntityRenderersEvent.RegisterLayerDefinitions.class, event ->
				event.registerLayerDefinition(WispModel.LAYER, WispModel::createBodyLayer));
		modBus.addListener(EventPriority.NORMAL, false, EntityRenderersEvent.RegisterRenderers.class, event ->
				event.registerEntityRenderer(KeeperWispsEntities.WISP, WispRenderer::new));
		modBus.addListener(EventPriority.NORMAL, false, RegisterParticleProvidersEvent.class, event -> {
			for (WispVariant variant : WispVariant.values()) {
				event.registerSpriteSet(KeeperWispsParticles.glint(variant), sprites -> new GlintParticle.Provider(sprites, variant.getColor()));
				event.registerSpriteSet(KeeperWispsParticles.spark(variant), sprites -> new SparkParticle.Provider(sprites, variant.getColor()));
			}
		});
	}

}

*///?}
