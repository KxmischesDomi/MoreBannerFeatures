package de.kxmischesdomi.morebannerfeatures.mixin.boat;

import de.kxmischesdomi.morebannerfeatures.core.accessor.Bannerable;
import de.kxmischesdomi.morebannerfeatures.core.config.MBFOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
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
@Mixin(Boat.class)
public abstract class BoatMixin extends Entity implements Bannerable {

	@Shadow protected abstract void defineSynchedData();

	@Shadow public abstract InteractionResult interact(Player player, InteractionHand hand);

	@Shadow protected abstract void readAdditionalSaveData(CompoundTag nbt);

	private static final EntityDataAccessor<ItemStack> BANNER;

	public BoatMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	public ItemStack getBannerItem() {
		if (!MBFOptions.BOAT_BANNERS.getBooleanValue()) {
			return ItemStack.EMPTY;
		}
		return this.entityData.get(BANNER);
	}

	public void setBannerItem(ItemStack itemStack) {
		this.entityData.set(BANNER, itemStack);
	}

	@Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
	private void defineSynchedData(CallbackInfo ci) {
		this.entityData.define(BANNER, ItemStack.EMPTY);
	}

	@Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
	private void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
		setBannerItem(ItemStack.of(nbt.getCompound("Banner")));
	}

	@Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
	private void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
		ItemStack bannerItem = getBannerItem();
		if (bannerItem != null && !bannerItem.isEmpty()) {
			CompoundTag bannerNBT = bannerItem.save(nbt.getCompound("Banner"));
			nbt.put("Banner", bannerNBT);
		}
	}


	@Inject(method = "destroy", at = @At(value = "TAIL"))
	private void damage(DamageSource damageSource, CallbackInfo ci) {
		if (getBannerItem() != null && !getBannerItem().isEmpty()) {
			spawnAtLocation(getBannerItem());
			setBannerItem(ItemStack.EMPTY);
		}
	}

	@Inject(method = "interact", at = @At(value = "HEAD"), cancellable = true)
	private void interact(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {

		if (!MBFOptions.BOAT_BANNERS.getBooleanValue()) {
			return;
		}

		if (player.isSecondaryUseActive()) {
			cir.setReturnValue(InteractionResult.PASS);
			return;
		}

		ItemStack itemStack = player.getItemInHand(hand);
		if (itemStack.getItem() instanceof BannerItem) {
			if (ItemStack.isSameItem(getBannerItem(), itemStack)) return;

			if (!getBannerItem().isEmpty()) {
				spawnAtLocation(getBannerItem());
				setBannerItem(ItemStack.EMPTY);
			}

			ItemStack copy = itemStack.copy();
			copy.setCount(1);
			setBannerItem(copy);

			if (!player.getAbilities().instabuild) {
				itemStack.shrink(1);
			}

			cir.setReturnValue(InteractionResult.SUCCESS);
			cir.cancel();
		} else if (itemStack.getItem() instanceof ShearsItem && !getBannerItem().isEmpty()) {
			spawnAtLocation(getBannerItem());

			this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
			this.gameEvent(GameEvent.SHEAR, player);
			itemStack.hurtAndBreak(1, player, (playerx) -> {
				playerx.broadcastBreakEvent(hand);
			});

			setBannerItem(ItemStack.EMPTY);
			cir.setReturnValue(InteractionResult.SUCCESS);
			cir.cancel();
		}

	}

	static {
		BANNER = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.ITEM_STACK);
	}

}
