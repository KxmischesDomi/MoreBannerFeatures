package de.kxmischesdomi.morebannerfeatures.mixin.player;

import de.kxmischesdomi.morebannerfeatures.MoreBannerFeatures;
import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.Bannerable;
import dev.emi.trinkets.api.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.Optional;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Bannerable {

	@Shadow public abstract PlayerInventory getInventory();

	public PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public ItemStack getBannerItem() {
		if (MoreBannerFeatures.isTrinketsInstalled()) {
			Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(this);
			TrinketComponent trinketComponent = component.orElse(null);

			if (trinketComponent != null) {
				Map<String, Map<String, TrinketInventory>> inventory = trinketComponent.getInventory();
				TrinketInventory trinketInventory = inventory.get("chest").get("cape");
				return trinketInventory.getStack(0);
			}

		}
		return getInventory().getArmorStack(2);
	}

}
