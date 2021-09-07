package de.kxmischesdomi.morebannerfeatures.mixin.horse;

import de.kxmischesdomi.morebannerfeatures.core.accessor.SideBannerable;
import de.kxmischesdomi.morebannerfeatures.core.accessor.InventoryBannerable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
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
@Mixin(HorseBaseEntity.class)
public abstract class HorseBaseEntityMixin extends AnimalEntity implements SideBannerable, InventoryBannerable {

	@Shadow protected SimpleInventory items;

	@Shadow public abstract boolean isSaddled();

	private static final TrackedData<ItemStack> BANNER_ITEM = DataTracker.registerData(HorseBaseEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

	protected HorseBaseEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "getInventorySize", at = @At(value = "RETURN"), cancellable = true)
	private void getInventorySize(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(cir.getReturnValue() + 1);
	}

	@Inject(method = "onInventoryChanged", at = @At(value = "HEAD"), cancellable = true)
	private void onInventoryChanged(Inventory sender, CallbackInfo ci) {

		ItemStack newStack = sender.getStack(getSlot());
		ItemStack oldStack = this.dataTracker.get(BANNER_ITEM);
		this.dataTracker.set(BANNER_ITEM, newStack);

		if (newStack != oldStack && newStack.getItem() instanceof BannerItem) {
			this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1, 1);
		}

	}

	@Override
	public ItemStack getBannerItem() {
		return this.dataTracker.get(BANNER_ITEM);
	}

	@Override
	public float getYOffset() {
		return isSaddled() ? 0.07F : 0;
	}

	@Override
	public float getXOffset() {
		return isSaddled() ? 0.06F : 0;
	}

	@Inject(method = "initDataTracker", at = @At(value = "TAIL"))
	private void initDataTracker(CallbackInfo ci) {
		this.dataTracker.startTracking(BANNER_ITEM, ItemStack.EMPTY);
	}

	@Inject(method = "writeCustomDataToNbt", at = @At(value = "TAIL"))
	private void write(NbtCompound nbt, CallbackInfo ci) {
		if (!this.items.getStack(getSlot()).isEmpty()) {
			nbt.put("BannerItem", this.items.getStack(getSlot()).writeNbt(new NbtCompound()));
		}
	}

	@Inject(method = "readCustomDataFromNbt", at = @At(value = "TAIL"))
	private void read(NbtCompound nbt, CallbackInfo ci) {
		if (nbt.contains("BannerItem", 10)) {
			ItemStack itemStack = ItemStack.fromNbt(nbt.getCompound("BannerItem"));
			if (!itemStack.isEmpty() && itemStack.getItem() instanceof BannerItem) {
				this.dataTracker.set(BANNER_ITEM, itemStack);
				this.items.setStack(getSlot(), itemStack);
			}

		}
	}

}
