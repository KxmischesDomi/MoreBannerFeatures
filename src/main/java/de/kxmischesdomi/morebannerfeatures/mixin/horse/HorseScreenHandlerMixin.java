package de.kxmischesdomi.morebannerfeatures.mixin.horse;

import de.kxmischesdomi.morebannerfeatures.core.BannerSlot;
import de.kxmischesdomi.morebannerfeatures.core.accessor.InventoryBannerable;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(HorseScreenHandler.class)
public abstract class HorseScreenHandlerMixin extends ScreenHandler {

	@Shadow @Final private Inventory inventory;
	private InventoryBannerable bannerable;

	protected HorseScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
		super(type, syncId);
	}

	@Inject(method = "<init>", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void init(int syncId, PlayerInventory playerInventory, Inventory inventory, HorseBaseEntity entity, CallbackInfo ci) {

		if (entity instanceof InventoryBannerable bannerable) {
			this.bannerable = bannerable;

			int x = 8;
			int y = 54;
			int slot = bannerable.getSlot();

			this.addSlot(new BannerSlot(entity, inventory, slot, x, y));
		}

	}

	@ModifyArgs(method = "transferSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/HorseScreenHandler;insertItem(Lnet/minecraft/item/ItemStack;IIZ)Z"))
	public void transferSlot(Args args) {
		int endIndex = args.get(2);
		if (endIndex == this.slots.size()) {
			args.set(2, endIndex - 1);
		}

	}

	@Inject(method = "transferSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;size()I", shift = At.Shift.AFTER), cancellable = true)
	public void transferSlotToBanner(PlayerEntity player, int index, CallbackInfoReturnable<ItemStack> cir) {
		Slot slot = this.slots.get(index);
		ItemStack itemStack2 = slot.getStack();

		int transferIndex = bannerable.getTransferIndex();
		if (this.getSlot(transferIndex).canInsert(itemStack2) && !this.getSlot(transferIndex).hasStack()) {
			ItemStack newStack = itemStack2.split(1);

			if (!this.insertItem(newStack, transferIndex, transferIndex + 1, false)) {
				cir.setReturnValue(ItemStack.EMPTY);
			} else {
				cir.setReturnValue(ItemStack.EMPTY);
			}
		} else if (index == transferIndex) {
			if (!this.insertItem(itemStack2, inventory.size(), this.slots.size() - 1, true)) {

				cir.setReturnValue(ItemStack.EMPTY);
			} else {
				Slot bannerSlot = this.slots.get(transferIndex);
				bannerSlot.setStack(ItemStack.EMPTY);
			}
		}
	}

}
