package de.kxmischesdomi.morebannerfeatures.utils;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.List;
import java.util.function.Function;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class RendererUtils {

	public static void modifyMatricesDevelopment(MatrixStack matrices) {
		// DEVELOPMENT OFFSETS FOR TESTING
		Vec3d offset = DevelopmentUtils.offset;
		matrices.translate(offset.getX(), -offset.getY(), offset.getZ());
		offset = DevelopmentUtils.scale;
		matrices.scale((float) offset.getX(), (float) offset.getY(), (float) offset.getZ());
	}

	public static void modifyMatricesBannerSwing(ModelPart banner, Entity entity, float tickDelta, boolean applyPivotY) {
		modifyMatricesBannerSwing(banner, entity, tickDelta, applyPivotY, aFloat -> aFloat);
	}

	public static void modifyMatricesBannerSwing(ModelPart banner, Entity entity, float tickDelta, boolean applyPivotY, Function<Float, Float> pitchFunction) {
		Vec3d pos = entity.getPos();
		long m = entity.world.getTime();
		float n = ((float)Math.floorMod((long)(pos.getX() * 7 + pos.getY() * 9 + pos.getZ() * 13) + m, 100L) + tickDelta) / 100.0F;
		banner.pitch = pitchFunction.apply((-0.0125F + 0.01F * MathHelper.cos(6.2831855F * n)) * 3.1415927F);
		if (applyPivotY) banner.pivotY = -32.0F;
	}

	public static void modifyMatricesFreezing(MatrixStack matrices, Entity entity, boolean freezing) {
		if (freezing) {
			float yaw = (float) (Math.cos((double) entity.age * 3.25D) * 3.141592653589793D * 0.4000000059604645D);
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(yaw));
		}
	}

	public static void renderCanvasFromItem(ItemStack itemStack, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart canvas) {
		if (itemStack.getItem() instanceof BannerItem) {
			List<Pair<BannerPattern, DyeColor>> bannerPatterns = BannerBlockEntity.getPatternsFromNbt(((BannerItem) itemStack.getItem()).getColor(), BannerBlockEntity.getPatternListTag(itemStack));
			BannerBlockEntityRenderer.renderCanvas(matrixStack, vertexConsumers, light, overlay, canvas, ModelLoader.BANNER_BASE, true, bannerPatterns, itemStack.hasGlint());
		}

	}

	/**
	 * Will be removed in the next version.
	 * Couldn't be removed yet because of Waveycapes compatibility.
	 */
	@Deprecated(forRemoval = true)
	public static void renderCanvas(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart canvas, SpriteIdentifier baseSprite, boolean isBanner, List<Pair<BannerPattern, DyeColor>> patterns) {
		BannerBlockEntityRenderer.renderCanvas(matrices, vertexConsumers, light, overlay, canvas, baseSprite, isBanner, patterns, false);
	}

	@Deprecated(forRemoval = true)
	public static void renderCanvas(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart canvas, SpriteIdentifier baseSprite, boolean isBanner, List<Pair<BannerPattern, DyeColor>> patterns, boolean glint) {
		BannerBlockEntityRenderer.renderCanvas(matrices, vertexConsumers, light, overlay, canvas, baseSprite, isBanner, patterns);
	}

}
