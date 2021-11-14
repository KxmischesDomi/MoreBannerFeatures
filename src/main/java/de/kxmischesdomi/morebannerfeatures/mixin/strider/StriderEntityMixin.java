package de.kxmischesdomi.morebannerfeatures.mixin.strider;

import de.kxmischesdomi.morebannerfeatures.core.accessor.Bannerable;
import de.kxmischesdomi.morebannerfeatures.core.config.MBFOptions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(StriderEntity.class)
public abstract class StriderEntityMixin extends AnimalEntity implements Bannerable {

	@Shadow public abstract void readCustomDataFromNbt(NbtCompound nbt);

	private static final TrackedData<ItemStack> BANNER;

	protected StriderEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public ItemStack getBannerItem() {
		if (!MBFOptions.STRIDER_BANNERS.getBooleanValue()) {
			return ItemStack.EMPTY;
		}
		return this.dataTracker.get(BANNER);
	}

	public void setBannerItem(ItemStack itemStack) {
		this.dataTracker.set(BANNER, itemStack);
	}

	@Inject(method = "initDataTracker", at = @At(value = "TAIL"))
	private void initDataTracker(CallbackInfo ci) {
		this.dataTracker.startTracking(BANNER, ItemStack.EMPTY);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At(value = "TAIL"))
	private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		setBannerItem(ItemStack.fromNbt(nbt.getCompound("Banner")));
	}

	@Inject(method = "writeCustomDataToNbt", at = @At(value = "TAIL"))
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
		ItemStack bannerItem = getBannerItem();
		if (bannerItem != null && !bannerItem.isEmpty()) {
			NbtCompound bannerNBT = bannerItem.writeNbt(nbt.getCompound("Banner"));
			nbt.put("Banner", bannerNBT);
		}
	}

	@Inject(method = "dropInventory", at = @At(value = "HEAD"))
	private void dropInventory(CallbackInfo ci) {
		if (!getBannerItem().isEmpty()) dropStack(getBannerItem());
	}

	@Inject(method = "interactMob", at = @At(value = "HEAD"), cancellable = true)
	private void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {

		if (!MBFOptions.STRIDER_BANNERS.getBooleanValue()) {
			return;
		}

		if (player.shouldCancelInteraction()) return;
		if (isBaby()) return;

		ItemStack itemStack = player.getStackInHand(hand);
		if (itemStack.getItem() instanceof BannerItem) {
			if (getBannerItem().isItemEqualIgnoreDamage(itemStack)) return;

			if (!getBannerItem().isEmpty()) {
				dropStack(getBannerItem());
			}

			ItemStack copy = itemStack.copy();
			copy.setCount(1);
			setBannerItem(copy);

			if (!player.getAbilities().creativeMode) {
				itemStack.decrement(1);
			}

			cir.setReturnValue(ActionResult.SUCCESS);
			cir.cancel();
		} else if (itemStack.getItem() instanceof ShearsItem && !getBannerItem().isEmpty()) {

			this.world.playSoundFromEntity(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 1.0F, 1.0F);
			this.emitGameEvent(GameEvent.SHEAR, player);
			itemStack.damage(1, player, (playerx) -> {
				playerx.sendToolBreakStatus(hand);
			});

			dropStack(getBannerItem());
			setBannerItem(ItemStack.EMPTY);
			cir.setReturnValue(ActionResult.SUCCESS);
			cir.cancel();
		}

	}

	@Override
	public boolean cannotDespawn() {
		return !getBannerItem().isEmpty() || super.cannotDespawn();
	}

	static {
		BANNER = DataTracker.registerData(StriderEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	}

}
