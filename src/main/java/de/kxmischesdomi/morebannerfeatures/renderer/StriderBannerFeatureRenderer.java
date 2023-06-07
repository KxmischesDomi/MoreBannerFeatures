package de.kxmischesdomi.morebannerfeatures.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import de.kxmischesdomi.morebannerfeatures.core.accessor.Bannerable;
import de.kxmischesdomi.morebannerfeatures.core.errors.ErrorSystemManager;
import de.kxmischesdomi.morebannerfeatures.utils.RendererUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.StriderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class StriderBannerFeatureRenderer extends RenderLayer<Strider, StriderModel<Strider>> {

	private final ModelPart modelPart = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.BANNER);
	private final ModelPart banner = modelPart.getChild("flag");
	private final ModelPart pillar = modelPart.getChild("pole");
	private final ModelPart crossbar = modelPart.getChild("bar");

	public StriderBannerFeatureRenderer(RenderLayerParent<Strider, StriderModel<Strider>> context) {
		super(context);
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, Strider entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

		// Safety try catch to avoid crashes!
		try {
			if (entity instanceof Bannerable bannerable) {
				ItemStack itemStack = bannerable.getBannerItem();
				if (itemStack == null || !(itemStack.getItem() instanceof BannerItem)) return;

				matrices.pushPose();

				matrices.mulPose(Axis.YP.rotationDegrees(headYaw));
				if (!entity.isVehicle()) {
					matrices.mulPose(Axis.XP.rotationDegrees(headPitch));
				}

				matrices.scale(0.7f, 0.7f, 0.7f);

				matrices.translate(0, -1, 0.6);

				VertexConsumer vertexConsumer = ModelBakery.BANNER_BASE.buffer(vertexConsumers, RenderType::entityNoOutline);
				this.pillar.render(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
				this.crossbar.render(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
				RendererUtils.modifyMatricesBannerSwing(this.banner, entity, tickDelta, true);
				RendererUtils.modifyMatricesFreezing(matrices, entity, entity.isFullyFrozen() || entity.isSuffocating());

				RendererUtils.renderCanvasFromItem(itemStack, matrices, vertexConsumers, light, OverlayTexture.NO_OVERLAY, this.banner);

				matrices.popPose();

			}
		} catch (Exception exception) {
			ErrorSystemManager.reportException();
			exception.printStackTrace();
		}

	}

}
