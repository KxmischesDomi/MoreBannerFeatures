package de.kxmischesdomi.morebannerfeatures.mixin.boat;

import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.Bannerable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
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
@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity implements Bannerable {

	@Shadow protected abstract void initDataTracker();

	@Shadow public abstract ActionResult interact(PlayerEntity player, Hand hand);

	@Shadow protected abstract void readCustomDataFromNbt(NbtCompound nbt);

	private static final TrackedData<ItemStack> BANNER;

	public BoatEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	public ItemStack getBannerItem() {
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


	@Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"))
	private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (getBannerItem() != null && !getBannerItem().isEmpty()) {
			dropStack(getBannerItem());
			setBannerItem(ItemStack.EMPTY);
		}
	}

	@Inject(method = "interact", at = @At(value = "HEAD"), cancellable = true)
	private void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		if (player.shouldCancelInteraction()) {
			cir.setReturnValue(ActionResult.PASS);
			return;
		}

		ItemStack itemStack = player.getStackInHand(hand);
		if (itemStack.getItem() instanceof BannerItem) {
			if (getBannerItem().isItemEqualIgnoreDamage(itemStack)) return;

			if (!getBannerItem().isEmpty()) {
				dropStack(getBannerItem());
				setBannerItem(ItemStack.EMPTY);
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
			dropStack(getBannerItem());
			setBannerItem(ItemStack.EMPTY);
			cir.setReturnValue(ActionResult.SUCCESS);
			cir.cancel();
		}

	}

	static {
		BANNER = DataTracker.registerData(EndermanEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	}

}
