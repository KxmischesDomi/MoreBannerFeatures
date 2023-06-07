package de.kxmischesdomi.morebannerfeatures.mixin.camel;

import de.kxmischesdomi.morebannerfeatures.core.accessor.InventoryBannerable;
import de.kxmischesdomi.morebannerfeatures.core.accessor.SideBannerable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(Camel.class)
public abstract class CamelMixin extends AbstractHorse implements SideBannerable, InventoryBannerable {

	@Shadow public abstract boolean isCamelSitting();

	public CamelMixin(EntityType<? extends AbstractChestedHorse> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public boolean useStaticSwing() {
		return isCamelSitting();
	}

	@Override
	public float getStaticSwing() {
		return 1;
	}

	@Override
	public float getXOffset() {
		return 0.35f + (isCamelSitting() ? -0.05f : 0f);
	}

	@Override
	public float getYOffset() {
		return 1.53f + (isCamelSitting() ? -2.8f : 0f);
	}

	@Override
	public int getTransferIndex() {
		return 38;
	}

	@Override
	public int getSlot() {
		return 1;
	}

	@Override
	public float getZOffset() {
		return 0;
	}

}
