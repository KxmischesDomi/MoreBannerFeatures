package de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.cloak;

import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.BannerSlot;
import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.Bannerable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.4
 */
public class PlayerCloakSlot extends BannerSlot {

	private final PlayerEntity entity;

	public PlayerCloakSlot(PlayerEntity entity, Inventory inventory, int index, int x, int y) {
		super(entity, inventory, index, x, y);
		this.entity = entity;
	}

	@Override
	public ItemStack getStack() {
		return ((Bannerable) entity).getBannerItem();
	}

	@Override
	public void setStack(ItemStack stack) {
		PlayerInventory inventory = entity.getInventory();
		if (inventory instanceof CloakInventory cloakInventory) {
			cloakInventory.setCloak(stack);
		}
	}

	@Override
	public ItemStack takeStack(int amount) {
		ItemStack stack = getStack();
		ItemStack copy = stack.copy();
		stack.decrement(amount);
		copy.setCount(amount);
		return copy;
	}

	@Override
	public void onQuickTransfer(ItemStack newItem, ItemStack original) {
		super.onQuickTransfer(newItem, original);
	}

}
