package de.kxmischesdomi.morebannerfeatures.mixin.donkey;

import de.kxmischesdomi.morebannerfeatures.core.accessor.SideBannerable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.level.Level;
import de.kxmischesdomi.morebannerfeatures.core.accessor.InventoryBannerable;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(Donkey.class)
public abstract class DonkeyMixin extends AbstractChestedHorse implements SideBannerable, InventoryBannerable {

	protected DonkeyMixin(EntityType<? extends AbstractChestedHorse> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public int getSlot() {
		return 1;
	}

	@Override
	public int getTransferIndex() {
		return hasChest() ? 53 : 38;
	}

	@Override
	public float getZOffset() {
		return hasChest() ? -0.7F : 0;
	}

}
