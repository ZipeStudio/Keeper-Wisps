package me.zipestudio.keeperwisps.compat;

import org.jetbrains.annotations.Nullable;

//? if >=1.21.6 {
import net.minecraft.world.level.storage.*;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}

public record AccessorySlot(String mod, String slot, int index) {

	private static final String MOD_KEY = "Mod";
	private static final String SLOT_KEY = "Slot";
	private static final String INDEX_KEY = "Index";

	//? if >=1.21.6 {
	public void save(ValueOutput output) {
		output.putString(MOD_KEY, this.mod);
		output.putString(SLOT_KEY, this.slot);
		output.putInt(INDEX_KEY, this.index);
	}

	@Nullable
	public static AccessorySlot load(ValueInput input) {
		String mod = input.getStringOr(MOD_KEY, "");
		String slot = input.getStringOr(SLOT_KEY, "");
		if (mod.isEmpty() || slot.isEmpty()) {
			return null;
		}
		return new AccessorySlot(mod, slot, input.getIntOr(INDEX_KEY, 0));
	}
	//?} else {
	/*public CompoundTag save() {
		CompoundTag tag = new CompoundTag();
		tag.putString(MOD_KEY, this.mod);
		tag.putString(SLOT_KEY, this.slot);
		tag.putInt(INDEX_KEY, this.index);
		return tag;
	}

	@Nullable
	public static AccessorySlot load(CompoundTag tag) {
		if (!tag.contains(MOD_KEY) || !tag.contains(SLOT_KEY)) {
			return null;
		}
		return new AccessorySlot(tag.getString(MOD_KEY), tag.getString(SLOT_KEY), tag.getInt(INDEX_KEY));
	}
	*///?}

}
