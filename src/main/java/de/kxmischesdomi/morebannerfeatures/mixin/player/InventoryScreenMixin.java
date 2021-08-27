package de.kxmischesdomi.morebannerfeatures.mixin.player;

import com.mojang.blaze3d.systems.RenderSystem;
import de.kxmischesdomi.morebannerfeatures.MoreBannerFeatures;
import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.Bannerable;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.4
 */
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {

	private static final Identifier BANNER_ICON = new Identifier(MoreBannerFeatures.MOD_ID, "textures/gui/banner_player.png");

	private PlayerEntity entity;

	public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(PlayerEntity player, CallbackInfo ci) {
		this.entity = player;
	}

	@Inject(method = "drawBackground", at = @At("TAIL"))
	private void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo ci) {

		if (this.entity instanceof Bannerable) {
			int i = (this.width - this.backgroundWidth) / 2;
			int j = (this.height - this.backgroundHeight) / 2;

			RenderSystem.setShaderTexture(0, MoreBannerFeatures.BANNER_BACKGROUND);
			drawTexture(matrices, i + 77 - 1, j + 44 - 1, 0, 0, 18, 18, 18, 18);

			if (((Bannerable) this.entity).getBannerItem().isEmpty()) {
				RenderSystem.setShaderTexture(0, BANNER_ICON);
				drawTexture(matrices, i + 77 - 1, j + 44 - 1, 0, 0, 18, 18, 18, 18);
			}

		}

	}

}
