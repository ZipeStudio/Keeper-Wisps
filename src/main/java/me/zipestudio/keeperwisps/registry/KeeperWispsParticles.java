package me.zipestudio.keeperwisps.registry;

import java.util.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.Identifier;

import me.zipestudio.keeperwisps.KeeperWisps;
import me.zipestudio.keeperwisps.entity.WispVariant;

//? if fabric {
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
//?}

public final class KeeperWispsParticles {

	private static final Map<WispVariant, SimpleParticleType> GLINTS = new EnumMap<>(WispVariant.class);
	private static final Map<WispVariant, SimpleParticleType> SPARKS = new EnumMap<>(WispVariant.class);

	static {
		for (WispVariant variant : WispVariant.values()) {
			//? if fabric {
			GLINTS.put(variant, FabricParticleTypes.simple());
			SPARKS.put(variant, FabricParticleTypes.simple());
			//?} else {
			/*GLINTS.put(variant, new SimpleParticleType(false));
			SPARKS.put(variant, new SimpleParticleType(false));
			*///?}
		}
	}

	private KeeperWispsParticles() {
	}

	public static SimpleParticleType glint(WispVariant variant) {
		return GLINTS.get(variant);
	}

	public static Identifier glintId(WispVariant variant) {
		return KeeperWisps.id("glint_" + variant.getSerializedName());
	}

	public static SimpleParticleType spark(WispVariant variant) {
		return SPARKS.get(variant);
	}

	public static Identifier sparkId(WispVariant variant) {
		return KeeperWisps.id("spark_" + variant.getSerializedName());
	}

}
