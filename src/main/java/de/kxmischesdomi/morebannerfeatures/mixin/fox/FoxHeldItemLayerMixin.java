package de.kxmischesdomi.morebannerfeatures.mixin.fox;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.kxmischesdomi.morebannerfeatures.core.config.MBFOptions;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.FoxHeldItemLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.animal.Fox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.1
 */
@Mixin(FoxHeldItemLayer.class)
public abstract class FoxHeldItemLayerMixin extends RenderLayer<Fox, FoxModel<Fox>> {

	public FoxHeldItemLayerMixin(RenderLayerParent<Fox, FoxModel<Fox>> context) {
		super(context);
	}

	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Fox;FFFFFF)V", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemTransforms$TransformType;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
	public void renderItem(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, Fox foxEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		if (MBFOptions.FOX_CORRECTION.getBooleanValue()) {
			matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
		}
	}

}
