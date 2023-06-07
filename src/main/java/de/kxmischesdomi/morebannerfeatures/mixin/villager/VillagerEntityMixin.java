package de.kxmischesdomi.morebannerfeatures.mixin.villager;

import de.kxmischesdomi.morebannerfeatures.core.accessor.Bannerable;
import de.kxmischesdomi.morebannerfeatures.core.config.MBFOptions;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.1
 */
@Mixin(Villager.class)
public abstract class VillagerEntityMixin extends AbstractVillager implements Bannerable {

	@Shadow public abstract InteractionResult mobInteract(Player player, InteractionHand hand);

	@Shadow public abstract boolean canBreed();

	public VillagerEntityMixin(EntityType<? extends AbstractVillager> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public ItemStack getBannerItem() {
		return getItemBySlot(EquipmentSlot.HEAD);
	}

	public void setBannerItem(ItemStack itemStack) {
		((NonNullList<ItemStack>) this.getArmorSlots()).set(EquipmentSlot.HEAD.getIndex(), itemStack);
	}

	@Override
	protected void dropEquipment() {
		if (!getBannerItem().isEmpty()) {
			spawnAtLocation(getBannerItem());
		}
		super.dropEquipment();
	}

	@Inject(method = "mobInteract", at = @At(value = "HEAD"), cancellable = true)
	private void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {

		if (!MBFOptions.VILLAGER_BANNERS.getBooleanValue()) {
			return;
		}

		if (player.isSecondaryUseActive()) return;

		ItemStack itemStack = player.getItemInHand(hand);
		ItemStack bannerItem = getBannerItem();
		if (itemStack.getItem() instanceof BannerItem) {
			if (ItemStack.isSameItem(bannerItem, itemStack)) return;

			this.level().playSound(null, this, SoundEvents.HORSE_STEP_WOOD, SoundSource.PLAYERS, 1.0F, 1.0F);

			if (!bannerItem.isEmpty()) {
				spawnAtLocation(bannerItem);
			}

			ItemStack copy = itemStack.copy();
			copy.setCount(1);
			setBannerItem(copy);

			if (!player.getAbilities().instabuild) {
				itemStack.shrink(1);
			}

			cir.setReturnValue(InteractionResult.SUCCESS);
			cir.cancel();
		} else if (itemStack.getItem() instanceof ShearsItem) {

			this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
			this.gameEvent(GameEvent.SHEAR, player);
			itemStack.hurtAndBreak(1, player, (playerx) -> {
				playerx.broadcastBreakEvent(hand);
			});

			spawnAtLocation(bannerItem);
			setBannerItem(ItemStack.EMPTY);
			cir.setReturnValue(InteractionResult.SUCCESS);
			cir.cancel();
		}

	}

}
