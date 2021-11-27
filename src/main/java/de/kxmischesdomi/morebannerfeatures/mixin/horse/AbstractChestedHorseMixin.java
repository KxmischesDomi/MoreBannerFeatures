package de.kxmischesdomi.morebannerfeatures.mixin.horse;

import de.kxmischesdomi.morebannerfeatures.core.accessor.InventoryBannerable;
import de.kxmischesdomi.morebannerfeatures.core.accessor.SideBannerable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(AbstractChestedHorse.class)
public abstract class AbstractChestedHorseMixin extends AbstractHorse implements SideBannerable, InventoryBannerable {

	@Shadow public abstract boolean hasChest();

	protected AbstractChestedHorseMixin(EntityType<? extends AbstractChestedHorse> entityType, Level world) {
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
