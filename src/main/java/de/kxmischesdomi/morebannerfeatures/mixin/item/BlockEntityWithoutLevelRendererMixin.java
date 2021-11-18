package de.kxmischesdomi.morebannerfeatures.mixin.item;

import com.mojang.blaze3d.vertex.PoseStack;
import de.kxmischesdomi.morebannerfeatures.utils.RendererUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractBannerBlock;
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

	@Inject(method = "renderByItem", at = @At(value = "HEAD"))
	public void fixBannerItemRendering(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, CallbackInfo ci) {
		if (itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractBannerBlock && itemStack.hasFoil()) {
			RendererUtils.nextBannerGlint = true;
		}
	}

}
