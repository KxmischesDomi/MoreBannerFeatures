package de.kxmischesdomi.morebannerfeatures.mixin.trader;

import de.kxmischesdomi.morebannerfeatures.core.accessor.InventoryBannerable;
import de.kxmischesdomi.morebannerfeatures.core.accessor.SideBannerable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

/**
 *
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(TraderLlama.class)
public abstract class TraderLLamaEntityMixin extends AbstractChestedHorse implements SideBannerable, InventoryBannerable {

	public TraderLLamaEntityMixin(EntityType<? extends AbstractChestedHorse> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public float getXOffset() {
		return 0.2F;
	}

	@Override
	public float getYOffset() {
		return 0.21F;
	}

	@Override
	public float getZOffset() {
		return 0F;
	}
}
