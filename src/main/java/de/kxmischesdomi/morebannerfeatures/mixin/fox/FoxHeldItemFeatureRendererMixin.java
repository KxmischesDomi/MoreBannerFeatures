package de.kxmischesdomi.morebannerfeatures.mixin.fox;

import de.kxmischesdomi.morebannerfeatures.core.config.MBFOptions;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.FoxHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.FoxEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.1
 */
@Mixin(FoxHeldItemFeatureRenderer.class)
public abstract class FoxHeldItemFeatureRendererMixin extends FeatureRenderer<FoxEntity, FoxEntityModel<FoxEntity>> {

	public FoxHeldItemFeatureRendererMixin(FeatureRendererContext<FoxEntity, FoxEntityModel<FoxEntity>> context) {
		super(context);
	}

	@Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/FoxEntity;FFFFFF)V", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
	public void renderItem(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, FoxEntity foxEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		if (MBFOptions.FOX_CORRECTION.getBooleanValue()) {
			matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
		}
	}

}
