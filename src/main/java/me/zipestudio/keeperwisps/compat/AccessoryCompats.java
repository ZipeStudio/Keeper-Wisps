package me.zipestudio.keeperwisps.compat;

import java.util.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import me.zipestudio.keeperwisps.KeeperWisps;

//? if fabric {
import net.fabricmc.loader.api.FabricLoader;
//?} elif neoforge {
/*import net.neoforged.fml.ModList;
*///?} elif forge {
/*import net.minecraftforge.fml.ModList;
*///?}

public final class AccessoryCompats {

	private static final List<AccessoryCompat> COMPATS = new ArrayList<>();

	private AccessoryCompats() {
	}

	public static void init() {
		//? if accessories {
		/*if (isModLoaded("accessories")) {
			register(new AccessoriesCompat());
		}
		*///?}
		//? if fabric && trinkets {
		/*if (isModLoaded("trinkets")) {
			register(new TrinketsCompat());
		}
		*///?}
		//? if (forge || neoforge) && curios {
		/*if (isModLoaded("curios")) {
			register(new CuriosCompat());
		}
		*///?}
	}

	public static Map<ItemStack, AccessorySlot> collect(LivingEntity entity) {
		Map<ItemStack, AccessorySlot> slots = new IdentityHashMap<>();
		for (AccessoryCompat compat : COMPATS) {
			compat.collect(entity, (stack, slot) -> {
				if (!stack.isEmpty()) {
					slots.putIfAbsent(stack, slot);
				}
			});
		}
		return slots;
	}

	public static boolean tryEquip(LivingEntity entity, AccessorySlot slot, ItemStack stack) {
		for (AccessoryCompat compat : COMPATS) {
			if (compat.getModId().equals(slot.mod())) {
				return compat.tryEquip(entity, slot, stack);
			}
		}
		return false;
	}

	private static void register(AccessoryCompat compat) {
		COMPATS.add(compat);
		KeeperWisps.LOGGER.info("Enabled {} compat", compat.getModId());
	}

	private static boolean isModLoaded(String modId) {
		//? if fabric {
		return FabricLoader.getInstance().isModLoaded(modId);
		//?} else {
		/*return ModList.get().isLoaded(modId);
		*///?}
	}

}
