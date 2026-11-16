package me.zipestudio.keeperwisps.compat;

//? if accessories {
/*import io.wispforest.accessories.api.*;
import java.util.function.BiConsumer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class AccessoriesCompat implements AccessoryCompat {

	@Override
	public String getModId() {
		return "accessories";
	}

	@Override
	public void collect(LivingEntity entity, BiConsumer<ItemStack, AccessorySlot> consumer) {
		AccessoriesCapability capability = AccessoriesCapability.get(entity);
		if (capability == null) {
			return;
		}
		capability.getContainers().forEach((name, container) -> {
			Container accessories = container.getAccessories();
			for (int i = 0; i < accessories.getContainerSize(); i++) {
				consumer.accept(accessories.getItem(i), new AccessorySlot(this.getModId(), name, i));
			}
		});
	}

	@Override
	public boolean tryEquip(LivingEntity entity, AccessorySlot slot, ItemStack stack) {
		AccessoriesCapability capability = AccessoriesCapability.get(entity);
		if (capability == null) {
			return false;
		}
		AccessoriesContainer container = capability.getContainers().get(slot.slot());
		if (container == null) {
			return false;
		}
		Container accessories = container.getAccessories();
		if (slot.index() >= accessories.getContainerSize() || !accessories.getItem(slot.index()).isEmpty()) {
			return false;
		}
		accessories.setItem(slot.index(), stack);
		return true;
	}

}
*///?}
