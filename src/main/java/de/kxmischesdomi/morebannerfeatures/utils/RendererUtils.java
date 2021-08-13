package de.kxmischesdomi.morebannerfeatures.utils;

import de.kxmischesdomi.morebannerfeatures.MoreBannerFeatures;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.function.Function;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class RendererUtils {

	public static void modifyMatricesDevelopment(MatrixStack matrices) {
		// DEVELOPMENT OFFSETS FOR TESTING
		Vec3d offset = MoreBannerFeatures.offset;
		matrices.translate(offset.getX(), -offset.getY(), offset.getZ());
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

}
