package me.zipestudio.keeperwisps.death;

import java.util.Locale;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public enum InventoryRow {

	HOTBAR(0),
	EQUIPMENT(-1),
	BOTTOM(27),
	MIDDLE(18),
	TOP(9);

	public static final int SIZE = 9;

	private static final int ARMOR_SLOTS = 4;
	private static final int ARMOR_FIRST_SLOT = 36;

	private final int firstSlot;

	InventoryRow(int firstSlot) {
		this.firstSlot = firstSlot;
	}

	public int size() {
		return this == EQUIPMENT ? ARMOR_SLOTS + 1 : SIZE;
	}

	public ItemStack get(Inventory inventory, int index) {
		return inventory.getItem(this.slot(index));
	}

	public void set(Inventory inventory, int index, ItemStack stack) {
		inventory.setItem(this.slot(index), stack);
	}

	public String getSerializedName() {
		return this.name().toLowerCase(Locale.ROOT);
	}

	public static InventoryRow byName(String name) {
		for (InventoryRow row : values()) {
			if (row.getSerializedName().equalsIgnoreCase(name)) {
				return row;
			}
		}
		return HOTBAR;
	}

	private int slot(int index) {
		if (this == EQUIPMENT) {
			return index < ARMOR_SLOTS ? ARMOR_FIRST_SLOT + index : Inventory.SLOT_OFFHAND;
		}
		return this.firstSlot + index;
	}

}
