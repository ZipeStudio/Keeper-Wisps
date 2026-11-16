package me.zipestudio.keeperwisps.compat;

//? if (forge || neoforge) && curios {
/*import java.util.Optional;
import java.util.function.BiConsumer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.*;

public class CuriosCompat implements AccessoryCompat {

	@Override
	public String getModId() {
		return "curios";
	}

	@Override
	public void collect(LivingEntity entity, BiConsumer<ItemStack, AccessorySlot> consumer) {
		getInventory(entity).ifPresent(inventory -> inventory.getCurios().forEach((name, handler) -> {
			IDynamicStackHandler stacks = handler.getStacks();
			for (int i = 0; i < stacks.getSlots(); i++) {
				consumer.accept(stacks.getStackInSlot(i), new AccessorySlot(this.getModId(), name, i));
			}
		}));
	}

	@Override
	public boolean tryEquip(LivingEntity entity, AccessorySlot slot, ItemStack stack) {
		Optional<ICuriosItemHandler> inventory = getInventory(entity);
		if (inventory.isEmpty()) {
			return false;
		}
		ICurioStacksHandler handler = inventory.get().getCurios().get(slot.slot());
		if (handler == null) {
			return false;
		}
		IDynamicStackHandler stacks = handler.getStacks();
		if (slot.index() >= stacks.getSlots() || !stacks.getStackInSlot(slot.index()).isEmpty()) {
			return false;
		}
		stacks.setStackInSlot(slot.index(), stack);
		return true;
	}

	private static Optional<ICuriosItemHandler> getInventory(LivingEntity entity) {
		//? if forge {
		/^return CuriosApi.getCuriosInventory(entity).resolve();
		^///?} else {
		return CuriosApi.getCuriosInventory(entity);
		//?}
	}

}
*///?}
