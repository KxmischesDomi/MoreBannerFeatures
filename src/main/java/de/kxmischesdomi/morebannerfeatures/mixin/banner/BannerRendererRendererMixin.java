package de.kxmischesdomi.morebannerfeatures.mixin.banner;

import com.mojang.blaze3d.vertex.PoseStack;
import de.kxmischesdomi.morebannerfeatures.core.config.MBFOptions;
import de.kxmischesdomi.morebannerfeatures.utils.RendererUtils;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0.2
 */
@Mixin(value = BannerRenderer.class)
public abstract class BannerRendererRendererMixin implements BlockEntityRenderer<BannerBlockEntity>  {

	@Shadow @Final private ModelPart pole;

	@Inject(method = "render(Lnet/minecraft/world/level/block/entity/BannerBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/blockentity/BannerRenderer;pole:Lnet/minecraft/client/model/geom/ModelPart;"))
	private void render(BannerBlockEntity bannerBlockEntity, float f, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, int j, CallbackInfo ci) {
		if (!MBFOptions.HANGING_BANNERS.getBooleanValue()) {
			return;
		}

		BlockState blockState = bannerBlockEntity.getBlockState();

		try {
			if (blockState.getBlock() instanceof BannerBlock && blockState.getValue(BlockStateProperties.HANGING)) {
				matrixStack.translate(0.0D, -2.5D, 0.0D);
				this.pole.visible = false;
			}
		} catch (Exception ex) {
			// There is a banner from before the mod was downloaded.
		}

	}

	@ModifyArgs(method = "renderPatterns(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/resources/model/Material;ZLjava/util/List;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;buffer(Lnet/minecraft/client/renderer/MultiBufferSource;Ljava/util/function/Function;Z)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
	private static void modifyArgs(Args args) {
		if (RendererUtils.nextBannerGlint) {
			RendererUtils.nextBannerGlint = false;
			args.set(2, true);
		} else if (!((boolean) args.get(2))) {
			args.set(2, MBFOptions.BANNER_GLINT.getBooleanValue());
		}
	}

}
