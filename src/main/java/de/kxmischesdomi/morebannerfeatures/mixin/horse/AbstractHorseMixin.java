package de.kxmischesdomi.morebannerfeatures.mixin.horse;

import de.kxmischesdomi.morebannerfeatures.core.accessor.InventoryBannerable;
import de.kxmischesdomi.morebannerfeatures.core.accessor.SideBannerable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin extends Animal implements SideBannerable, InventoryBannerable {

	@Shadow protected SimpleContainer inventory;

	@Shadow public abstract boolean isSaddled();

	private static final EntityDataAccessor<ItemStack> BANNER_ITEM = SynchedEntityData.defineId(AbstractHorse.class, EntityDataSerializers.ITEM_STACK);

	protected AbstractHorseMixin(EntityType<? extends Animal> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "getInventorySize", at = @At(value = "RETURN"), cancellable = true)
	private void getInventorySize(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(cir.getReturnValue() + 1);
	}

	@Inject(method = "containerChanged", at = @At(value = "HEAD"), cancellable = true)
	private void onInventoryChanged(Container sender, CallbackInfo ci) {

		ItemStack newStack = sender.getItem(getSlot());
		ItemStack oldStack = this.entityData.get(BANNER_ITEM);
		this.entityData.set(BANNER_ITEM, newStack);

		if (newStack != oldStack && newStack.getItem() instanceof BannerItem) {
			this.level().playSound(null, this, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.5F, 1.0F);
		}

	}

	@Override
	public ItemStack getBannerItem() {
		return this.entityData.get(BANNER_ITEM);
	}

	@Override
	public float getYOffset() {
		return isSaddled() ? 0.07F : 0;
	}

	@Override
	public float getXOffset() {
		return isSaddled() ? 0.06F : 0;
	}

	@Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
	private void defineSynchedData(CallbackInfo ci) {
		this.entityData.define(BANNER_ITEM, ItemStack.EMPTY);
	}

	@Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
	private void write(CompoundTag nbt, CallbackInfo ci) {
		if (!this.inventory.getItem(getSlot()).isEmpty()) {
			nbt.put("BannerItem", this.inventory.getItem(getSlot()).save(new CompoundTag()));
		}
	}

	@Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
	private void read(CompoundTag nbt, CallbackInfo ci) {
		if (nbt.contains("BannerItem", 10)) {
			ItemStack itemStack = ItemStack.of(nbt.getCompound("BannerItem"));
			if (!itemStack.isEmpty() && itemStack.getItem() instanceof BannerItem) {
				this.entityData.set(BANNER_ITEM, itemStack);
				this.inventory.setItem(getSlot(), itemStack);
			}

		}
	}

}
