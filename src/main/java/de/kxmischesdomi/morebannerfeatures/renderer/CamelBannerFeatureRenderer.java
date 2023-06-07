package de.kxmischesdomi.morebannerfeatures.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.CamelModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.animal.camel.Camel;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class CamelBannerFeatureRenderer extends RenderLayer<Camel, CamelModel<Camel>> {

	public CamelBannerFeatureRenderer(RenderLayerParent<Camel, CamelModel<Camel>> context) {
		super(context);
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, Camel entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		HorseBaseBannerFeatureRenderer.renderSideBanner(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
	}

}
