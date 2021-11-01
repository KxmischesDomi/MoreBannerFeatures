package de.kxmischesdomi.morebannerfeatures.utils;

import de.kxmischesdomi.morebannerfeatures.MoreBannerFeatures;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.Optional;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.4
 */
public class MBFUtils {

	public static ItemStack getCloakItem(PlayerEntity player) {
		if (MoreBannerFeatures.isTrinketsInstalled()) {
			try {
				Optional<dev.emi.trinkets.api.TrinketComponent> component = dev.emi.trinkets.api.TrinketsApi.getTrinketComponent(player);
				dev.emi.trinkets.api.TrinketComponent trinketComponent = component.orElse(null);

				if (trinketComponent != null) {
					Map<String, Map<String, dev.emi.trinkets.api.TrinketInventory>> inventory = trinketComponent.getInventory();
					dev.emi.trinkets.api.TrinketInventory trinketInventory = inventory.get("chest").get("cape");
					return trinketInventory.getStack(0);
				}
			} catch (Exception exception) {
				// TRINKETS IS PROBABLY NOT INSTALLED ON SERVER
			}

		}
		return player.getInventory().getArmorStack(2);
	}

}
