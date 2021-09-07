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
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(HorseScreenHandler.class)
public abstract class HorseScreenHandlerMixin extends ScreenHandler {

	@Shadow public abstract ItemStack transferSlot(PlayerEntity player, int index);

	protected HorseScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
		super(type, syncId);
	}

	@Inject(method = "<init>", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void init(int syncId, PlayerInventory playerInventory, Inventory inventory, HorseBaseEntity entity, CallbackInfo ci) {

		if (entity instanceof InventoryBannerable bannerable) {

			int x = 8;
			int y = 54;
			int slot = bannerable.getSlot();

			this.addSlot(new BannerSlot(entity, inventory, slot, x, y));

		}

	}

//	@Inject(method = "transferSlot", at = @At(value = "HEAD"), cancellable = true)
//	private void transferSlot(PlayerEntity player, int index, CallbackInfoReturnable<ItemStack> cir) {
//		Slot slot = this.slots.get(index);
//
//		if (slot != null && slot.hasStack()) {
//			ItemStack itemStack2 = slot.getStack();
//			ItemStack itemStack = itemStack2.copy();
//			int i = this.inventory.size();
//			if (index >= i) {
//				if (this.getSlot(38).canInsert(itemStack2)) {
//					if (!this.insertItem(itemStack2, 38, 39, false)) {
//						cir.setReturnValue(ItemStack.EMPTY);
//					}
//					cir.setReturnValue(itemStack);
//				}
//			}
//
//		}
//	}
}
