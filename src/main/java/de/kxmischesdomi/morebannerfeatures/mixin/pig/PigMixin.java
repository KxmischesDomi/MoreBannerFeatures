package de.kxmischesdomi.morebannerfeatures.mixin.pig;

import de.kxmischesdomi.morebannerfeatures.core.accessor.SideBannerable;
import de.kxmischesdomi.morebannerfeatures.core.config.MBFOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
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
@Mixin(Pig.class)
public abstract class PigMixin extends Animal implements SideBannerable {

	private static final EntityDataAccessor<ItemStack> BANNER;

	public PigMixin(EntityType<? extends Animal> entityType, Level world) {
		super(entityType, world);
	}

	@Shadow
	public abstract boolean isSaddled();

	@Override
	public float getXOffset() {
		return isSaddled() ? 0.39F : 0.29F;
	}

	@Override
	public float getYOffset() {
		return isSaddled() ? -1.46F : -1.46F - 0.1F;
	}

	@Override
	public float getZOffset() {
		return -0.09f;
	}

	@Override
	public Vec3 getScaleOffset() {
		return new Vec3(0.7, 0.7, 0.7);
	}

	@Override
	public ItemStack getBannerItem() {
		if (!MBFOptions.PIG_BANNERS.getBooleanValue()) {
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

	@Inject(method = "dropEquipment", at = @At(value = "HEAD"))
	private void dropEquipment(CallbackInfo ci) {
		if (!getBannerItem().isEmpty()) spawnAtLocation(getBannerItem());
	}

	@Inject(method = "mobInteract", at = @At(value = "HEAD"), cancellable = true)
	private void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {

		if (!MBFOptions.PIG_BANNERS.getBooleanValue()) {
			return;
		}

		if (player.isSecondaryUseActive()) return;
		if (isBaby()) return;

		ItemStack itemStack = player.getItemInHand(hand);
		if (itemStack.getItem() instanceof BannerItem) {
			if (ItemStack.isSameItem(getBannerItem(), itemStack)) return;

			this.level().playSound(null, this, SoundEvents.HORSE_STEP_WOOD, SoundSource.PLAYERS, 1.0F, 1.0F);

			if (!getBannerItem().isEmpty()) {
				spawnAtLocation(getBannerItem());
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

			this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
			this.gameEvent(GameEvent.SHEAR, player);
			itemStack.hurtAndBreak(1, player, (playerx) -> {
				playerx.broadcastBreakEvent(hand);
			});

			spawnAtLocation(getBannerItem());
			setBannerItem(ItemStack.EMPTY);
			cir.setReturnValue(InteractionResult.SUCCESS);
			cir.cancel();
		}

	}

	@Override
	public boolean requiresCustomPersistence() {
		return !getBannerItem().isEmpty() || super.requiresCustomPersistence();
	}

	static {
		BANNER = SynchedEntityData.defineId(Pig.class, EntityDataSerializers.ITEM_STACK);
	}

}
