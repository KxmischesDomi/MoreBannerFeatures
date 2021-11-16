package de.kxmischesdomi.morebannerfeatures.core;

import de.kxmischesdomi.morebannerfeatures.core.accessor.Bannerable;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class BannerSlot extends Slot {

	protected final Entity entity;

	public BannerSlot(Entity entity, Container inventory, int index, int x, int y) {
		super(inventory, index, x, y);
		this.entity = entity;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.getItem() instanceof BannerItem && !this.hasItem() && entity instanceof Bannerable;
	}

	@Override
	public boolean isActive() {
		return entity instanceof Bannerable;
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

}
    