package de.kxmischesdomi.morebannerfeatures.renderer;

import com.mojang.datafixers.util.Pair;
import de.kxmischesdomi.morebannerfeatures.core.accessor.Bannerable;
import de.kxmischesdomi.morebannerfeatures.core.accessor.SideBannerable;
import de.kxmischesdomi.morebannerfeatures.core.config.MBFOptions;
import de.kxmischesdomi.morebannerfeatures.core.errors.ErrorSystemManager;
import de.kxmischesdomi.morebannerfeatures.utils.DevelopmentUtils;
import de.kxmischesdomi.morebannerfeatures.utils.RendererUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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

	private static ModelPart flagPart;
	private static ModelPart crossbarPart;

	public HorseBaseBannerFeatureRenderer(FeatureRendererContext<HorseBaseEntity, HorseEntityModel<HorseBaseEntity>> context) {
		super(context);
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

			if (entity instanceof HorseBaseEntity horseBaseEntity) {
				float o = horseBaseEntity.getAngryAnimationProgress(tickDelta);
				matrices.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(o * -0.7853982F));
				matrices.translate(0, o * -0.4136991F, o * 0.3926991F);
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
		RendererUtils.modifyMatricesBannerSwing(flagPart, entity, tickDelta, false, aFloat -> -aFloat);

		// Safety try catch to avoid crashes!
		try {
			List<Pair<BannerPattern, DyeColor>> bannerPatterns = BannerBlockEntity.getPatternsFromNbt(((BannerItem)itemStack.getItem()).getColor(), BannerBlockEntity.getPatternListTag(itemStack));

			int overlay = LivingEntityRenderer.getOverlay((LivingEntity) entity, 0.0F);
			boolean bar = MBFOptions.BAR.getBooleanValue();

			matrices.push();

			if (bar) {
				matrices.translate(0, -0.01, 0.115);
			}

			RendererUtils.renderCanvasFromItem(itemStack, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, flagPart);

			matrices.pop();

			if (bar) {
				matrices.translate(0, 1.99, -0.07);

				VertexConsumer vertexConsumer = ModelLoader.BANNER_BASE.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
				crossbarPart.render(matrices, vertexConsumer, light, overlay);
			}

		} catch (Exception exception) {
			ErrorSystemManager.reportException();
			exception.printStackTrace();
		}

	}

	static {
		ModelPart modelPart = MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.BANNER);
		flagPart = modelPart.getChild("flag");
		crossbarPart = modelPart.getChild("bar");
	}

}
