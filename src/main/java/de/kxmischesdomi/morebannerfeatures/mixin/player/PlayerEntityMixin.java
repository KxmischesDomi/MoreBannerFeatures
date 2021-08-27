package de.kxmischesdomi.morebannerfeatures.mixin.player;

import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.InventoryBannerable;
import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.cloak.CloakInventory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements InventoryBannerable {



	@Shadow public abstract PlayerInventory getInventory();

	public PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public ItemStack getBannerItem() {
		return getInventory() instanceof CloakInventory ? ((CloakInventory) getInventory()).getCloak() : ItemStack.EMPTY;
	}

	@Override
	public int getSlot() {
		return 41;
	}

}
