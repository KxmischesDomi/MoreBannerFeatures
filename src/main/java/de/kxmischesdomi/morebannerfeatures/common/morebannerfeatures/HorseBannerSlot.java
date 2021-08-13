package de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class HorseBannerSlot extends Slot {

	private final Entity entity;

	public HorseBannerSlot(Entity entity, Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
		this.entity = entity;
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		return stack.getItem() instanceof BannerItem && !this.hasStack() && entity instanceof Bannerable;
	}

	@Override
	public boolean isEnabled() {
		return entity instanceof Bannerable;
	}

	@Override
	public int getMaxItemCount() {
		return 1;
	}

}
    