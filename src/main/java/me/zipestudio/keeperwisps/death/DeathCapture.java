package me.zipestudio.keeperwisps.death;

import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import me.zipestudio.keeperwisps.compat.AccessorySlot;

record DeathCapture(ServerPlayer player, int wisps, BlockPos homePos, int excessXp, Map<ItemStack, SlotRef> slots, Map<ItemStack, AccessorySlot> accessorySlots, List<ItemEntity> drops) {

	record SlotRef(InventoryRow row, int index) {
	}

}
