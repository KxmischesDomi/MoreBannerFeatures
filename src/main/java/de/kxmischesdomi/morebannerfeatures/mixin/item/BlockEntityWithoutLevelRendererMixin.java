package de.kxmischesdomi.morebannerfeatures.mixin.item;

import com.mojang.blaze3d.vertex.PoseStack;
import de.kxmischesdomi.morebannerfeatures.utils.RendererUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.1.0
 */
@Mixin(BlockEntityWithoutLevelRenderer.class)
public abstract class BlockEntityWithoutLevelRendererMixin {

	@Inject(method = "renderByItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/AbstractBannerBlock;getColor()Lnet/minecraft/world/item/DyeColor;"))
	public void render(ItemStack stack, ItemTransforms.TransformType mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, CallbackInfo ci) {
		if (stack.hasFoil()) {
			RendererUtils.nextBannerGlint = true;
		}
	}

}
