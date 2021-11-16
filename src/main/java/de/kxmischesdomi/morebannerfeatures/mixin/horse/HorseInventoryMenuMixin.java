package de.kxmischesdomi.morebannerfeatures.mixin.horse;

import de.kxmischesdomi.morebannerfeatures.core.BannerSlot;
import de.kxmischesdomi.morebannerfeatures.core.accessor.InventoryBannerable;
import de.kxmischesdomi.morebannerfeatures.core.config.MBFOptions;
import net.minecraft.world.Container;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.HorseInventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
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
@Mixin(HorseInventoryMenu.class)
public abstract class HorseInventoryMenuMixin extends AbstractContainerMenu {

	@Shadow @Final private Container horseContainer;
	private InventoryBannerable bannerable;

	protected HorseInventoryMenuMixin(@Nullable MenuType<?> type, int syncId) {
		super(type, syncId);
	}

	@Inject(method = "<init>", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void init(int syncId, Inventory playerInventory, Container inventory, AbstractHorse entity, CallbackInfo ci) {

		if (entity instanceof InventoryBannerable bannerable && MBFOptions.HORSE_SLOT.getBooleanValue()) {
			this.bannerable = bannerable;

			int x = 8;
			int y = 54;
			int slot = bannerable.getSlot();

			this.addSlot(new BannerSlot(entity, inventory, slot, x, y));
		}

	}

	@ModifyArgs(method = "quickMoveStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/HorseInventoryMenu;moveItemStackTo(Lnet/minecraft/world/item/ItemStack;IIZ)Z"))
	public void transferSlot(Args args) {

		if (!MBFOptions.HORSE_SLOT.getBooleanValue()) {
			return;
		}

		int endIndex = args.get(2);
		if (endIndex == this.slots.size()) {
			args.set(2, endIndex - 1);
		}

	}

	@Inject(method = "quickMoveStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;getContainerSize()I", shift = At.Shift.AFTER), cancellable = true)
	public void transferSlotToBanner(Player player, int index, CallbackInfoReturnable<ItemStack> cir) {
		if (!MBFOptions.HORSE_SLOT.getBooleanValue()) {
			return;
		}

		Slot slot = this.slots.get(index);
		ItemStack itemStack2 = slot.getItem();

		int transferIndex = bannerable.getTransferIndex();
		if (this.getSlot(transferIndex).mayPlace(itemStack2) && !this.getSlot(transferIndex).hasItem()) {
			ItemStack newStack = itemStack2.split(1);

			if (!this.moveItemStackTo(newStack, transferIndex, transferIndex + 1, false)) {
				cir.setReturnValue(ItemStack.EMPTY);
			} else {
				cir.setReturnValue(ItemStack.EMPTY);
			}
		} else if (index == transferIndex) {
			if (!this.moveItemStackTo(itemStack2, horseContainer.getContainerSize(), this.slots.size() - 1, true)) {

				cir.setReturnValue(ItemStack.EMPTY);
			} else {
				Slot bannerSlot = this.slots.get(transferIndex);
				bannerSlot.set(ItemStack.EMPTY);
			}
		}
	}

}
