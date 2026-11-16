package me.zipestudio.keeperwisps.registry;

import java.util.*;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

import me.zipestudio.keeperwisps.KeeperWisps;

public final class KeeperWispsSounds {

	private static final Map<Identifier, SoundEvent> SOUNDS = new LinkedHashMap<>();

	public static final SoundEvent WISP_AMBIENT = register("entity.wisp.ambient");
	public static final SoundEvent WISP_AMBIENT_EMPTY = register("entity.wisp.ambient_empty");
	public static final SoundEvent WISP_APPEAR = register("entity.wisp.appear");
	public static final SoundEvent WISP_EYE_CONTACT = register("entity.wisp.eye_contact");
	public static final SoundEvent WISP_RETRIEVE = register("entity.wisp.retrieve");
	public static final SoundEvent WISP_DEATH = register("entity.wisp.death");

	private KeeperWispsSounds() {
	}

	public static Map<Identifier, SoundEvent> getSounds() {
		return Collections.unmodifiableMap(SOUNDS);
	}

	private static SoundEvent register(String path) {
		Identifier id = KeeperWisps.id(path);
		SoundEvent sound = SoundEvent.createVariableRangeEvent(id);
		SOUNDS.put(id, sound);
		return sound;
	}

}
