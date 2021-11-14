package de.kxmischesdomi.morebannerfeatures.mixin.player;

import de.kxmischesdomi.morebannerfeatures.MoreBannerFeatures;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0.4
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "getPreferredEquipmentSlot", at = @At(value = "HEAD"), cancellable = true)
	private static void getPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir) {
		if (MoreBannerFeatures.isTrinketsInstalled()) return;
		Item item = stack.getItem();

		if (item instanceof BannerItem) {
			cir.setReturnValue(EquipmentSlot.CHEST);
		}

	}

}
