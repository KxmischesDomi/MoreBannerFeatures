package de.kxmischesdomi.morebannerfeatures.client.feature;

import com.mojang.datafixers.util.Pair;
import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.Bannerable;
import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.SideBannerable;
import de.kxmischesdomi.morebannerfeatures.utils.DevelopmentUtils;
import de.kxmischesdomi.morebannerfeatures.utils.RendererUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class HorseBaseBannerFeatureRenderer extends FeatureRenderer<HorseBaseEntity, HorseEntityModel<HorseBaseEntity>> {

	private static ModelPart bannerPart;

	public HorseBaseBannerFeatureRenderer(FeatureRendererContext<HorseBaseEntity, HorseEntityModel<HorseBaseEntity>> context) {
		super(context);
		bannerPart = MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.BANNER).getChild("flag");
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, HorseBaseEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		renderSideBanner(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
	}

	public static void renderSideBanner(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (entity instanceof Bannerable bannerable) {
			matrices.push();

			// ENTITY AND ITEM PREPARATIONS
			ItemStack itemStack = bannerable.getBannerItem();
			if (itemStack == null || itemStack.isEmpty() || !(itemStack.getItem() instanceof BannerItem)) {
				matrices.pop();
				return;
			}

			// RENDERING PREPARATIONS
			matrices.scale(0.45f, 0.45f, 0.45f);

			// FIRST BANNER
			matrices.push();

			// START MODIFYING
			scaleMatricesForEntity(matrices, entity);
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
			modifyMatricesDefault(matrices, true);
			modifyMatricesForEntity(matrices, entity, true);
			RendererUtils.modifyMatricesFreezing(matrices, entity, entity.isFreezing());

			// FINISHED MODIFYING
			renderBanner(matrices, vertexConsumers, light, entity, tickDelta, itemStack);
			matrices.pop();

			// SECOND BANNER

			// START MODIFYING
			scaleMatricesForEntity(matrices, entity);
			matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(90));
			modifyMatricesDefault(matrices, false);
			modifyMatricesForEntity(matrices, entity, false);
			RendererUtils.modifyMatricesFreezing(matrices, entity, entity.isFreezing());

			// FINISHED MODIFYING
			renderBanner(matrices, vertexConsumers, light, entity, tickDelta, itemStack);
			matrices.pop();
		}
	}

	private static Vec3d modifyMatricesDefault(MatrixStack matrices, boolean first) {
		// ADD DEFAULTS
		Vec3d offset = new Vec3d(0.83F, -0.41F, 0.07F);
		// DEVELOPMENT OFFSETS FOR TESTING
		offset = offset.add(DevelopmentUtils.offset);

		if (first) {
			matrices.translate(-offset.getZ(), -offset.getY(), offset.getX());
		} else {
			matrices.translate(offset.getZ(), -offset.getY(), offset.getX());

		}

		return offset;
	}

	private static Vec3d modifyMatricesForEntity(MatrixStack matrices, Entity entity, boolean first) {
		if (!DevelopmentUtils.applyEntityOffsets) return Vec3d.ZERO;

		if (entity instanceof SideBannerable bannerable) {

			Vec3d offset = new Vec3d(bannerable.getXOffset(), bannerable.getYOffset(), bannerable.getZOffset());

			if (first) {
				matrices.translate(-offset.getZ(), -offset.getY(), offset.getX());
			} else {
				matrices.translate(offset.getZ(), -offset.getY(), offset.getX());
			}

			return offset;
		}

		return Vec3d.ZERO;
	}

	private static void scaleMatricesForEntity(MatrixStack matrices, Entity entity) {
		if (!DevelopmentUtils.applyEntityOffsets) return;

		if (entity instanceof SideBannerable bannerable) {
			Vec3d scaleOffset = bannerable.getScaleOffset();
			if (scaleOffset != null) matrices.scale((float) scaleOffset.getX(), (float) scaleOffset.getY(), (float) scaleOffset.getZ());
		}

	}

	private static void renderBanner(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float tickDelta, ItemStack itemStack) {
		RendererUtils.modifyMatricesBannerSwing(bannerPart, entity, tickDelta, false, aFloat -> -aFloat);

		// Safety try catch to avoid crashes!
		try {
			List<Pair<BannerPattern, DyeColor>> bannerPatterns = BannerBlockEntity.getPatternsFromNbt(((BannerItem)itemStack.getItem()).getColor(), BannerBlockEntity.getPatternListTag(itemStack));

			RendererUtils.renderCanvas(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, bannerPart, ModelLoader.BANNER_BASE, true, bannerPatterns);
		} catch (Exception exception) {
			exception.printStackTrace();
		}


	}

}
