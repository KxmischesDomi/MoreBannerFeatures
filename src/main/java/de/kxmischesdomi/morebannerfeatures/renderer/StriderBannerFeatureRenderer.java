package de.kxmischesdomi.morebannerfeatures.renderer;

import de.kxmischesdomi.morebannerfeatures.core.accessor.Bannerable;
import de.kxmischesdomi.morebannerfeatures.core.errors.ErrorSystemManager;
import de.kxmischesdomi.morebannerfeatures.utils.RendererUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.StriderEntityModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class StriderBannerFeatureRenderer extends FeatureRenderer<StriderEntity, StriderEntityModel<StriderEntity>> {

	private final ModelPart modelPart = MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.BANNER);
	private final ModelPart banner = modelPart.getChild("flag");
	private final ModelPart pillar = modelPart.getChild("pole");
	private final ModelPart crossbar = modelPart.getChild("bar");

	public StriderBannerFeatureRenderer(FeatureRendererContext<StriderEntity, StriderEntityModel<StriderEntity>> context) {
		super(context);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, StriderEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

		// Safety try catch to avoid crashes!
		try {
			if (entity instanceof Bannerable bannerable) {
				ItemStack itemStack = bannerable.getBannerItem();
				if (itemStack == null || !(itemStack.getItem() instanceof BannerItem)) return;

				matrices.push();

				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(headYaw));
				if (!entity.hasPassengers()) matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(headPitch));

				matrices.scale(0.7f, 0.7f, 0.7f);

				matrices.translate(0, -1, 0.6);

				VertexConsumer vertexConsumer = ModelLoader.BANNER_BASE.getVertexConsumer(vertexConsumers, RenderLayer::getEntityNoOutline);
				this.pillar.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
				this.crossbar.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
				RendererUtils.modifyMatricesBannerSwing(this.banner, entity, tickDelta, true);
				RendererUtils.modifyMatricesFreezing(matrices, entity, entity.isFreezing() || entity.isCold());

				RendererUtils.renderCanvasFromItem(itemStack, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, this.banner);

				matrices.pop();

			}
		} catch (Exception exception) {
			ErrorSystemManager.reportException();
			exception.printStackTrace();
		}

	}

}
