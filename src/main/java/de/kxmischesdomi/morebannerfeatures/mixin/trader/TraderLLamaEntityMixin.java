package de.kxmischesdomi.morebannerfeatures.mixin.trader;

import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.InventoryBannerable;
import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.SideBannerable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.TraderLlamaEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

/**
 *
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(TraderLlamaEntity.class)
public abstract class TraderLLamaEntityMixin extends AbstractDonkeyEntity implements SideBannerable, InventoryBannerable {

	public TraderLLamaEntityMixin(EntityType<? extends AbstractDonkeyEntity> entityType, World world) {
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
