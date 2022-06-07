package de.kxmischesdomi.morebannerfeatures.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Function;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class RendererUtils {

	public static boolean nextBannerGlint = false;

	public static void modifyMatricesBannerSwing(ModelPart banner, Entity entity, float tickDelta, boolean applyPivotY) {
		modifyMatricesBannerSwing(banner, entity, tickDelta, applyPivotY, aFloat -> aFloat);
	}

	public static void modifyMatricesBannerSwing(ModelPart banner, Entity entity, float tickDelta, boolean applyPivotY, Function<Float, Float> pitchFunction) {
		Vec3 pos = entity.position();
		long m = entity.level.getGameTime();
		float n = ((float)Math.floorMod((long)(pos.x() * 7 + pos.y() * 9 + pos.z() * 13) + m, 100L) + tickDelta) / 100.0F;
		banner.xRot = pitchFunction.apply((-0.0125F + 0.01F * Mth.cos(6.2831855F * n)) * 3.1415927F);
		if (applyPivotY) banner.y = -32.0F;
	}

	public static void modifyMatricesFreezing(PoseStack matrices, Entity entity, boolean freezing) {
		if (freezing) {
			float yaw = (float) (Math.cos((double) entity.tickCount * 3.25D) * 3.141592653589793D * 0.4000000059604645D);
			matrices.mulPose(Vector3f.YP.rotationDegrees(yaw));
		}
	}

	public static void renderCanvasFromItem(ItemStack itemStack, PoseStack matrixStack, MultiBufferSource vertexConsumers, int light, int overlay, ModelPart canvas) {
		if (itemStack.getItem() instanceof BannerItem) {
			List<Pair<Holder<BannerPattern>, DyeColor>> bannerPatterns = BannerBlockEntity.createPatterns(((BannerItem) itemStack.getItem()).getColor(), BannerBlockEntity.getItemPatterns(itemStack));
			BannerRenderer.renderPatterns(matrixStack, vertexConsumers, light, overlay, canvas, ModelBakery.BANNER_BASE, true, bannerPatterns, itemStack.hasFoil());
		}

	}

	/**
	 * Will be removed in the next version.
	 * Couldn't be removed yet because of Waveycapes compatibility.
	 */
	@Deprecated(forRemoval = true)
	public static void renderCanvas(PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, ModelPart canvas, Material baseSprite, boolean isBanner, List<Pair<Holder<BannerPattern>, DyeColor>> patterns) {
		BannerRenderer.renderPatterns(matrices, vertexConsumers, light, overlay, canvas, baseSprite, isBanner, patterns, false);
	}

	@Deprecated(forRemoval = true)
	public static void renderCanvas(PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, ModelPart canvas, Material baseSprite, boolean isBanner, List<Pair<Holder<BannerPattern>, DyeColor>> patterns, boolean glint) {
		BannerRenderer.renderPatterns(matrices, vertexConsumers, light, overlay, canvas, baseSprite, isBanner, patterns);
	}

}
