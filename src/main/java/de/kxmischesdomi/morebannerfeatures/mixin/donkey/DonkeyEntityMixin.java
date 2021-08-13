package de.kxmischesdomi.morebannerfeatures.mixin.donkey;

import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.SideBannerable;
import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.InventoryBannerable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(DonkeyEntity.class)
public abstract class DonkeyEntityMixin extends AbstractDonkeyEntity implements SideBannerable, InventoryBannerable {

	protected DonkeyEntityMixin(EntityType<? extends AbstractDonkeyEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public int getSlot() {
		return 1;
	}

	@Override
	public float getZOffset() {
		return hasChest() ? -0.7F : 0;
	}

}
