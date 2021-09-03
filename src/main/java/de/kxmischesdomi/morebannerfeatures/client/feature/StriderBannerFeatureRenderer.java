package de.kxmischesdomi.morebannerfeatures.client.feature;

import com.mojang.datafixers.util.Pair;
import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.Bannerable;
import de.kxmischesdomi.morebannerfeatures.utils.RendererUtils;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
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
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Vec3f;

import java.util.List;

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

		if (entity instanceof Bannerable bannerable) {
			ItemStack itemStack = bannerable.getBannerItem();
			if (itemStack == null || !(itemStack.getItem() instanceof BannerItem)) return;

			matrices.push();

//			banner.roll = -(0.1F * MathHelper.cos(limbAngle) * limbDistance);
//			pillar.roll = 0.1F * MathHelper.sin(limbAngle) * 0.35f * limbDistance;
//			crossbar.roll = 0.1F * MathHelper.sin(limbAngle) * 0.35f * limbDistance;

			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(headYaw));
			if (!entity.hasPassengers()) matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(headPitch));

			matrices.scale(0.7f, 0.7f, 0.7f);

			matrices.translate(0, -1, 0.6);

			VertexConsumer vertexConsumer = ModelLoader.BANNER_BASE.getVertexConsumer(vertexConsumers, RenderLayer::getEntityNoOutline);
			this.pillar.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
			this.crossbar.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
			RendererUtils.modifyMatricesBannerSwing(this.banner, entity, tickDelta, true);
			RendererUtils.modifyMatricesFreezing(matrices, entity, entity.isFreezing() || entity.isCold());

			// Safety try catch to avoid crashes!
			try {
				List<Pair<BannerPattern, DyeColor>> bannerPatterns = BannerBlockEntity.getPatternsFromNbt(((BannerItem)itemStack.getItem()).getColor(), BannerBlockEntity.getPatternListTag(itemStack));
				RendererUtils.renderCanvas(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, banner, ModelLoader.BANNER_BASE, true, bannerPatterns);

			} catch (Exception exception) {
				exception.printStackTrace();
			}

			matrices.pop();

		}

	}

}
