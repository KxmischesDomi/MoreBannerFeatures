package de.kxmischesdomi.morebannerfeatures.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3f;
import de.kxmischesdomi.morebannerfeatures.core.accessor.Bannerable;
import de.kxmischesdomi.morebannerfeatures.core.accessor.SideBannerable;
import de.kxmischesdomi.morebannerfeatures.core.config.MBFOptions;
import de.kxmischesdomi.morebannerfeatures.core.errors.ErrorSystemManager;
import de.kxmischesdomi.morebannerfeatures.utils.DevelopmentUtils;
import de.kxmischesdomi.morebannerfeatures.utils.RendererUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class HorseBaseBannerFeatureRenderer extends RenderLayer<AbstractHorse, HorseModel<AbstractHorse>> {

	private static ModelPart flagPart;
	private static ModelPart crossbarPart;

	public HorseBaseBannerFeatureRenderer(RenderLayerParent<AbstractHorse, HorseModel<AbstractHorse>> context) {
		super(context);
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, AbstractHorse entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		renderSideBanner(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
	}

	public static void renderSideBanner(PoseStack matrices, MultiBufferSource vertexConsumers, int light, Entity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (entity instanceof Bannerable bannerable) {
			matrices.pushPose();

			// ENTITY AND ITEM PREPARATIONS
			ItemStack itemStack = bannerable.getBannerItem();
			if (itemStack == null || itemStack.isEmpty() || !(itemStack.getItem() instanceof BannerItem)) {
				matrices.popPose();
				return;
			}

			if (entity instanceof AbstractHorse abstractHorse) {
				float o = abstractHorse.getStandAnim(tickDelta);
				matrices.mulPose(Vector3f.XP.rotation(o * -0.7853982F));
				matrices.translate(0, o * -0.4136991F, o * 0.3926991F);
			}

			// RENDERING PREPARATIONS
			matrices.scale(0.45f, 0.45f, 0.45f);

			// FIRST BANNER
			matrices.pushPose();

			// START MODIFYING
			scaleMatricesForEntity(matrices, entity);
			matrices.mulPose(Vector3f.YP.rotationDegrees(90));
			modifyMatricesDefault(matrices, true);
			modifyMatricesForEntity(matrices, entity, true);
			RendererUtils.modifyMatricesFreezing(matrices, entity, entity.isFullyFrozen());

			// FINISHED MODIFYING
			renderBanner(matrices, vertexConsumers, light, entity, tickDelta, itemStack);
			matrices.popPose();

			// SECOND BANNER

			// START MODIFYING
			scaleMatricesForEntity(matrices, entity);
			matrices.mulPose(Vector3f.YN.rotationDegrees(90));
			modifyMatricesDefault(matrices, false);
			modifyMatricesForEntity(matrices, entity, false);
			RendererUtils.modifyMatricesFreezing(matrices, entity, entity.isFullyFrozen());

			// FINISHED MODIFYING
			renderBanner(matrices, vertexConsumers, light, entity, tickDelta, itemStack);
			matrices.popPose();
		}
	}

	private static Vec3 modifyMatricesDefault(PoseStack matrices, boolean first) {
		// ADD DEFAULTS
		Vec3 offset = new Vec3(0.83F, -0.41F, 0.07F);
		// DEVELOPMENT OFFSETS FOR TESTING
		offset = offset.add(DevelopmentUtils.offset);

		if (first) {
			matrices.translate(-offset.z(), -offset.y(), offset.x());
		} else {
			matrices.translate(offset.z(), -offset.y(), offset.x());

		}

		return offset;
	}

	private static Vec3 modifyMatricesForEntity(PoseStack matrices, Entity entity, boolean first) {
		if (!DevelopmentUtils.applyEntityOffsets) return Vec3.ZERO;

		if (entity instanceof SideBannerable bannerable) {

			Vec3 offset = new Vec3(bannerable.getXOffset(), bannerable.getYOffset(), bannerable.getZOffset());

			if (first) {
				matrices.translate(-offset.z(), -offset.y(), offset.x());
			} else {
				matrices.translate(offset.z(), -offset.y(), offset.x());
			}

			return offset;
		}

		return Vec3.ZERO;
	}

	private static void scaleMatricesForEntity(PoseStack matrices, Entity entity) {
		if (!DevelopmentUtils.applyEntityOffsets) return;

		if (entity instanceof SideBannerable bannerable) {
			Vec3 scaleOffset = bannerable.getScaleOffset();
			if (scaleOffset != null) matrices.scale((float) scaleOffset.x(), (float) scaleOffset.y(), (float) scaleOffset.z());
		}

	}

	private static void renderBanner(PoseStack matrices, MultiBufferSource vertexConsumers, int light, Entity entity, float tickDelta, ItemStack itemStack) {
		RendererUtils.modifyMatricesBannerSwing(flagPart, entity, tickDelta, false, aFloat -> -aFloat);

		// Safety try catch to avoid crashes!
		try {
			List<Pair<BannerPattern, DyeColor>> bannerPatterns = BannerBlockEntity.createPatterns(((BannerItem)itemStack.getItem()).getColor(), BannerBlockEntity.getItemPatterns(itemStack));

			int overlay = LivingEntityRenderer.getOverlayCoords((LivingEntity) entity, 0.0F);
			boolean bar = MBFOptions.BAR.getBooleanValue();

			matrices.pushPose();

			if (bar) {
				matrices.translate(0, -0.01, 0.115);
			}

			RendererUtils.renderCanvasFromItem(itemStack, matrices, vertexConsumers, light, OverlayTexture.NO_OVERLAY, flagPart);

			matrices.popPose();

			if (bar) {
				matrices.translate(0, 1.99, -0.07);

				VertexConsumer vertexConsumer = ModelBakery.BANNER_BASE.buffer(vertexConsumers, RenderType::entitySolid);
				crossbarPart.render(matrices, vertexConsumer, light, overlay);
			}

		} catch (Exception exception) {
			ErrorSystemManager.reportException();
			exception.printStackTrace();
		}

	}

	static {
		ModelPart modelPart = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.BANNER);
		flagPart = modelPart.getChild("flag");
		crossbarPart = modelPart.getChild("bar");
	}

}
