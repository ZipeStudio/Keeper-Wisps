package me.zipestudio.keeperwisps.entity;

import java.util.*;
import lombok.Getter;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.ItemStack;
//? if >=26.3 {
import net.minecraft.util.Prediction;
//?}
//? if >=1.21.6 {
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.level.storage.*;
//?} else {
/*import net.minecraft.nbt.*;
*///?}

import me.zipestudio.keeperwisps.compat.*;
import me.zipestudio.keeperwisps.death.InventoryRow;

public class WispInventory {

	private static final String ITEMS_KEY = "Items";
	private static final String SLOT_KEY = "Slot";
	private static final String ROW_KEY = "Row";
	private static final String EXPERIENCE_KEY = "Experience";
	private static final String ACCESSORY_SLOTS_KEY = "AccessorySlots";
	private static final String ACCESSORY_ITEM_KEY = "Item";

	private final WispEntity wisp;
	private final Map<Integer, AccessorySlot> accessorySlots = new HashMap<>();

	private NonNullList<ItemStack> items = NonNullList.withSize(InventoryRow.SIZE, ItemStack.EMPTY);
	@Getter
	private InventoryRow row = InventoryRow.HOTBAR;
	@Getter
	private int experience;
	private int displayIndex = -1;

	public WispInventory(WispEntity wisp) {
		this.wisp = wisp;
	}

	public void setup(InventoryRow row, List<ItemStack> items, int experience) {
		this.row = row;
		this.experience = experience;
		this.items = NonNullList.withSize(Math.max(InventoryRow.SIZE, items.size()), ItemStack.EMPTY);
		for (int i = 0; i < items.size(); i++) {
			this.items.set(i, items.get(i));
		}
		this.onChanged();
	}

	public void setAccessorySlots(Map<Integer, AccessorySlot> accessorySlots) {
		this.accessorySlots.clear();
		this.accessorySlots.putAll(accessorySlots);
	}

	public int count() {
		int count = 0;
		for (ItemStack stack : this.items) {
			if (!stack.isEmpty()) {
				count++;
			}
		}
		return count;
	}

	public boolean isEmpty() {
		return this.count() == 0;
	}

	public void giveTo(Player player) {
		Inventory inventory = player.getInventory();
		for (int i = 0; i < this.items.size(); i++) {
			ItemStack stack = this.items.get(i);
			if (stack.isEmpty()) {
				continue;
			}
			WispEffects.itemTrail(this.wisp, player, stack);
			this.items.set(i, ItemStack.EMPTY);
			AccessorySlot accessorySlot = this.accessorySlots.remove(i);
			if (i < this.row.size() && this.row.get(inventory, i).isEmpty()) {
				this.row.set(inventory, i, stack);
			} else if (accessorySlot == null || !AccessoryCompats.tryEquip(player, accessorySlot, stack)) {
				//? if >=26.3 {
				inventory.placeItemBackInInventory(stack, Prediction.SERVER_ONLY);
				//?} else {
				/*inventory.placeItemBackInInventory(stack);
				*///?}
			}
		}
		inventory.setChanged();
		this.onChanged();
	}

	public void dropAll(ServerLevel level) {
		for (int i = 0; i < this.items.size(); i++) {
			ItemStack stack = this.items.get(i);
			if (!stack.isEmpty()) {
				//? if >=1.21.2 {
				this.wisp.spawnAtLocation(level, stack);
				//?} else {
				/*this.wisp.spawnAtLocation(stack);
				*///?}
				this.items.set(i, ItemStack.EMPTY);
			}
		}
		if (this.experience > 0) {
			ExperienceOrb.award(level, this.wisp.position(), this.experience);
			this.experience = 0;
		}
	}

	public void cycleDisplayItem() {
		for (int step = 1; step <= this.items.size(); step++) {
			int index = Math.floorMod(this.displayIndex + step, this.items.size());
			if (!this.items.get(index).isEmpty()) {
				this.displayIndex = index;
				this.wisp.setDisplayItem(this.items.get(index).copy());
				return;
			}
		}
		this.displayIndex = -1;
		this.wisp.setDisplayItem(ItemStack.EMPTY);
	}

	//? if >=1.21.6 {
	public void save(ValueOutput output) {
		ContainerHelper.saveAllItems(output, this.items);
		output.putString(ROW_KEY, this.row.getSerializedName());
		output.putInt(EXPERIENCE_KEY, this.experience);
		ValueOutput.ValueOutputList slots = output.childrenList(ACCESSORY_SLOTS_KEY);
		this.accessorySlots.forEach((item, slot) -> {
			ValueOutput slotOutput = slots.addChild();
			slot.save(slotOutput);
			slotOutput.putInt(ACCESSORY_ITEM_KEY, item);
		});
	}

	public void load(ValueInput input) {
		this.items = NonNullList.withSize(savedSize(input), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(input, this.items);
		this.row = InventoryRow.byName(input.getStringOr(ROW_KEY, ""));
		this.experience = input.getIntOr(EXPERIENCE_KEY, 0);
		this.accessorySlots.clear();
		for (ValueInput slotInput : input.childrenListOrEmpty(ACCESSORY_SLOTS_KEY)) {
			AccessorySlot slot = AccessorySlot.load(slotInput);
			if (slot != null) {
				this.accessorySlots.put(slotInput.getIntOr(ACCESSORY_ITEM_KEY, 0), slot);
			}
		}
		this.onChanged();
	}
	//?} else {
	/*public void save(CompoundTag tag) {
		//? if >=1.21 {
		ContainerHelper.saveAllItems(tag, this.items, this.wisp.registryAccess());
		//?} else {
		/^ContainerHelper.saveAllItems(tag, this.items);
		^///?}
		tag.putString(ROW_KEY, this.row.getSerializedName());
		tag.putInt(EXPERIENCE_KEY, this.experience);
		ListTag slots = new ListTag();
		this.accessorySlots.forEach((item, slot) -> {
			CompoundTag slotTag = slot.save();
			slotTag.putInt(ACCESSORY_ITEM_KEY, item);
			slots.add(slotTag);
		});
		tag.put(ACCESSORY_SLOTS_KEY, slots);
	}

	public void load(CompoundTag tag) {
		this.items = NonNullList.withSize(savedSize(tag), ItemStack.EMPTY);
		//? if >=1.21 {
		ContainerHelper.loadAllItems(tag, this.items, this.wisp.registryAccess());
		//?} else {
		/^ContainerHelper.loadAllItems(tag, this.items);
		^///?}
		this.row = InventoryRow.byName(tag.getString(ROW_KEY));
		this.experience = tag.getInt(EXPERIENCE_KEY);
		this.accessorySlots.clear();
		ListTag slots = tag.getList(ACCESSORY_SLOTS_KEY, Tag.TAG_COMPOUND);
		for (int i = 0; i < slots.size(); i++) {
			CompoundTag slotTag = slots.getCompound(i);
			AccessorySlot slot = AccessorySlot.load(slotTag);
			if (slot != null) {
				this.accessorySlots.put(slotTag.getInt(ACCESSORY_ITEM_KEY), slot);
			}
		}
		this.onChanged();
	}
	*///?}

	private void onChanged() {
		this.wisp.setVariant(WispVariant.byItemCount(this.count()));
		this.displayIndex = -1;
		this.cycleDisplayItem();
	}

	//? if >=1.21.6 {
	private static int savedSize(ValueInput input) {
		int size = InventoryRow.SIZE;
		for (ItemStackWithSlot item : input.listOrEmpty(ITEMS_KEY, ItemStackWithSlot.CODEC)) {
			size = Math.max(size, item.slot() + 1);
		}
		return size;
	}
	//?} else {
	/*private static int savedSize(CompoundTag tag) {
		int size = InventoryRow.SIZE;
		ListTag list = tag.getList(ITEMS_KEY, Tag.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {
			size = Math.max(size, (list.getCompound(i).getByte(SLOT_KEY) & 255) + 1);
		}
		return size;
	}
	*///?}

}
