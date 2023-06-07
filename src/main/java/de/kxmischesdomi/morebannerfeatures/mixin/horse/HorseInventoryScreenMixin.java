package de.kxmischesdomi.morebannerfeatures.mixin.horse;

import de.kxmischesdomi.morebannerfeatures.MoreBannerFeatures;
import de.kxmischesdomi.morebannerfeatures.core.accessor.Bannerable;
import de.kxmischesdomi.morebannerfeatures.core.config.MBFOptions;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.HorseInventoryMenu;
import org.spongepowered.asm.mixin.Final;
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
@Mixin(HorseInventoryScreen.class)
public abstract class HorseInventoryScreenMixin extends AbstractContainerScreen<HorseInventoryMenu> {

	private static final ResourceLocation SLOT_BACKGROUND = new ResourceLocation(MoreBannerFeatures.MOD_ID, "textures/gui/background.png");
	private static final ResourceLocation SLOT_ICON = new ResourceLocation(MoreBannerFeatures.MOD_ID, "textures/gui/banner.png");

	@Shadow @Final private AbstractHorse horse;

	public HorseInventoryScreenMixin(HorseInventoryMenu handler, Inventory inventory, Component title) {
		super(handler, inventory, title);
	}

	@Inject(method = "renderBg", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
	public void drawBackground(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci, int k, int l) {


		if (this.horse instanceof Bannerable && MBFOptions.HORSE_SLOT.getBooleanValue()) {
			// TODO: TEST CHANGES
			int localI = (this.width - this.imageWidth) / 2;
			int localJ = (this.height - this.imageHeight) / 2;

			guiGraphics.blit(SLOT_BACKGROUND, localI + 7, localJ + 35 + 18, 0, 0, 18, 18, 18, 18);

			if (((Bannerable) this.horse).getBannerItem().isEmpty()) {
				guiGraphics.blit(SLOT_ICON, localI + 7, localJ + 35 + 18, 0, 0, 18, 18, 18, 18);
			}

		}

	}

}
