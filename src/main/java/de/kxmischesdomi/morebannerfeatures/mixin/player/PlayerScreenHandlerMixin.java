package de.kxmischesdomi.morebannerfeatures.mixin.player;

import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.InventoryBannerable;
import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.cloak.PlayerCloakSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.4
 */
@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin extends AbstractRecipeScreenHandler<CraftingInventory> {

	public PlayerScreenHandlerMixin(ScreenHandlerType<?> screenHandlerType, int i) {
		super(screenHandlerType, i);
	}

	@Inject(method = "<init>", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void init(PlayerInventory inventory, boolean onServer, PlayerEntity entity, CallbackInfo ci) {

		if (entity instanceof InventoryBannerable bannerable) {

			int x = 77;
			int y = 44;
			int slot = bannerable.getSlot();

			this.addSlot(new PlayerCloakSlot(entity, inventory, slot, x, y));

		}

	}

	@Inject(method = "transferSlot", at = @At("HEAD"), cancellable = true)
	private void transferSlot(PlayerEntity player, int index, CallbackInfoReturnable<ItemStack> cir) {
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();

			if (player instanceof InventoryBannerable) {
				if (itemStack2.getItem() instanceof BannerItem) {
					if (!this.insertItem(itemStack2, 46, 47, false)) {
						cir.setReturnValue(ItemStack.EMPTY);
					}

				}

			}


		}
	}

}
