package de.kxmischesdomi.morebannerfeatures.utils;

import de.kxmischesdomi.morebannerfeatures.MoreBannerFeatures;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Optional;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.4
 */
public class MBFUtils {

	public static ItemStack getCloakItem(Player player) {
		if (MoreBannerFeatures.isTrinketsInstalled()) {
			try {
				Optional<TrinketComponent> component = dev.emi.trinkets.api.TrinketsApi.getTrinketComponent(player);

				if (component.isPresent()) {
					dev.emi.trinkets.api.TrinketComponent trinketComponent = component.get();
					Map<String, Map<String, TrinketInventory>> inventory = trinketComponent.getInventory();
					dev.emi.trinkets.api.TrinketInventory trinketInventory = inventory.get("chest").get("cape");
					return trinketInventory.getItem(0);
				}
			} catch (Exception exception) {
				// TRINKETS IS PROBABLY NOT INSTALLED ON SERVER
			}

		}
		return player.getInventory().getArmor(2);
	}

}
