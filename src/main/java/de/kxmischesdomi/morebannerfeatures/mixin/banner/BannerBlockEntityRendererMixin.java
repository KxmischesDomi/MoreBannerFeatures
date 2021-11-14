package de.kxmischesdomi.morebannerfeatures.mixin.banner;

import net.minecraft.block.BannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0.2
 */
@Mixin(value = BannerBlockEntityRenderer.class, priority = 10000)
public abstract class BannerBlockEntityRendererMixin implements BlockEntityRenderer<BannerBlockEntity>  {

	@Shadow @Final private ModelPart pillar;

	@Inject(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;pillar:Lnet/minecraft/client/model/ModelPart;"))
	private void render(BannerBlockEntity bannerBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
		BlockState blockState = bannerBlockEntity.getCachedState();

		try {
			if (blockState.getBlock() instanceof BannerBlock && blockState.get(Properties.HANGING)) {
				matrixStack.translate(0.0D, -2.5D, 0.0D);
				this.pillar.visible = false;
			}
		} catch (Exception ex) {
			// There is a banner from before the mod was downloaded.
		}

	}

	@Redirect(method = "renderCanvas(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/SpriteIdentifier;ZLjava/util/List;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/SpriteIdentifier;getVertexConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Ljava/util/function/Function;Z)Lnet/minecraft/client/render/VertexConsumer;"))
	private static VertexConsumer getVertex(SpriteIdentifier spriteIdentifier, VertexConsumerProvider vertexConsumers, Function<Identifier, RenderLayer> layerFactory, boolean bl) {
		return spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityTranslucent, bl);
	}

}
