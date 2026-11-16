package me.zipestudio.keeperwisps.compat;

//? if fabric && trinkets {
/*import dev.emi.trinkets.api.*;
import java.util.Map;
import java.util.function.BiConsumer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class TrinketsCompat implements AccessoryCompat {

	private static final String SEPARATOR = "/";

	@Override
	public String getModId() {
		return "trinkets";
	}

	@Override
	public void collect(LivingEntity entity, BiConsumer<ItemStack, AccessorySlot> consumer) {
		TrinketsApi.getTrinketComponent(entity).ifPresent(component -> component.getInventory().forEach((group, slots) ->
				slots.forEach((name, inventory) -> {
					for (int i = 0; i < inventory.getContainerSize(); i++) {
						consumer.accept(inventory.getItem(i), new AccessorySlot(this.getModId(), group + SEPARATOR + name, i));
					}
				})
		));
	}

	@Override
	public boolean tryEquip(LivingEntity entity, AccessorySlot slot, ItemStack stack) {
		String[] path = slot.slot().split(SEPARATOR, 2);
		if (path.length != 2) {
			return false;
		}
		TrinketComponent component = TrinketsApi.getTrinketComponent(entity).orElse(null);
		if (component == null) {
			return false;
		}
		Map<String, TrinketInventory> group = component.getInventory().get(path[0]);
		TrinketInventory inventory = group != null ? group.get(path[1]) : null;
		if (inventory == null || slot.index() >= inventory.getContainerSize() || !inventory.getItem(slot.index()).isEmpty()) {
			return false;
		}
		inventory.setItem(slot.index(), stack);
		return true;
	}

}
*///?}
