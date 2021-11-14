package de.kxmischesdomi.morebannerfeatures.mixin.villager;

import de.kxmischesdomi.morebannerfeatures.core.accessor.Bannerable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.1
 */
@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntity implements Bannerable {

	@Shadow public abstract ActionResult interactMob(PlayerEntity player, Hand hand);

	@Shadow public abstract boolean isReadyToBreed();

	public VillagerEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public ItemStack getBannerItem() {
		return getEquippedStack(EquipmentSlot.HEAD);
	}

	public void setBannerItem(ItemStack itemStack) {
		((DefaultedList<ItemStack>) this.getArmorItems()).set(EquipmentSlot.HEAD.getEntitySlotId(), itemStack);
	}

	@Override
	protected void dropInventory() {
		if (!getBannerItem().isEmpty()) {
			dropStack(getBannerItem());
		}
		super.dropInventory();
	}

	@Inject(method = "interactMob", at = @At(value = "HEAD"), cancellable = true)
	private void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {

//		if (!MBFOptions.PIG_BANNERS.getBooleanValue()) {
//			return;
//		}

		if (player.shouldCancelInteraction()) return;

		ItemStack itemStack = player.getStackInHand(hand);
		ItemStack bannerItem = getBannerItem();
		if (itemStack.getItem() instanceof BannerItem) {
			if (bannerItem.isItemEqualIgnoreDamage(itemStack)) return;

			if (!bannerItem.isEmpty()) {
				dropStack(bannerItem);
			}

			ItemStack copy = itemStack.copy();
			copy.setCount(1);
			setBannerItem(copy);

			if (!player.getAbilities().creativeMode) {
				itemStack.decrement(1);
			}

			cir.setReturnValue(ActionResult.SUCCESS);
			cir.cancel();
		} else if (itemStack.getItem() instanceof ShearsItem) {

			this.world.playSoundFromEntity(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 1.0F, 1.0F);
			this.emitGameEvent(GameEvent.SHEAR, player);
			itemStack.damage(1, player, (playerx) -> {
				playerx.sendToolBreakStatus(hand);
			});

			dropStack(bannerItem);
			setBannerItem(ItemStack.EMPTY);
			cir.setReturnValue(ActionResult.SUCCESS);
			cir.cancel();
		}

	}

}
