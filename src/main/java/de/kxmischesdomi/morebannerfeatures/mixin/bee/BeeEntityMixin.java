package de.kxmischesdomi.morebannerfeatures.mixin.bee;

import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.Bannerable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin extends AnimalEntity implements Bannerable {

	public BeeEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public ItemStack getBannerItem() {
		return ItemStack.EMPTY;
	}

}
