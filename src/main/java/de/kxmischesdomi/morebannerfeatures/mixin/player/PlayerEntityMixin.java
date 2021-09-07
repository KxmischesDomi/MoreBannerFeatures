package de.kxmischesdomi.morebannerfeatures.mixin.player;

import de.kxmischesdomi.morebannerfeatures.core.accessor.Bannerable;
import de.kxmischesdomi.morebannerfeatures.utils.MBFUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Bannerable {

	public PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public ItemStack getBannerItem() {
		return MBFUtils.getCloakItem(((PlayerEntity) (Object) this));
	}

}
