package me.zipestudio.keeperwisps.compat;

import java.util.function.BiConsumer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface AccessoryCompat {

	String getModId();

	void collect(LivingEntity entity, BiConsumer<ItemStack, AccessorySlot> consumer);

	boolean tryEquip(LivingEntity entity, AccessorySlot slot, ItemStack stack);

}
