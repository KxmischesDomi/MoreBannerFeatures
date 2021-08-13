package de.kxmischesdomi.morebannerfeatures.client.feature;

import com.mojang.datafixers.util.Pair;
import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.Bannerable;
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
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BeeEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.lang.reflect.Field;
import java.util.List;

/**
 * NOT IMPLEMENTED!!!
 *
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class BeeBannerFeature extends FeatureRenderer<BeeEntity, BeeEntityModel<BeeEntity>> {

	private final ModelPart modelPart = MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.BANNER);
	private final ModelPart banner = modelPart.getChild("flag");
	private final ModelPart crossbar = modelPart.getChild("bar");

	public BeeBannerFeature(FeatureRendererContext<BeeEntity, BeeEntityModel<BeeEntity>> context) {
		super(context);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, BeeEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

		if (entity instanceof Bannerable bannerable) {
			ItemStack itemStack = bannerable.getBannerItem();
			if (itemStack == null || !(itemStack.getItem() instanceof BannerItem)) return;
			List<Pair<BannerPattern, DyeColor>> bannerPatterns = BannerBlockEntity.getPatternsFromNbt(((BannerItem)itemStack.getItem()).getColor(), BannerBlockEntity.getPatternListTag(itemStack));

			matrices.push();

			matrices.scale(0.7f, 0.7f, 0.7f);

			float bonePivotY = getBonePivotY();
			bonePivotY -= 19;
			bonePivotY /= 9;
			matrices.translate(0, bonePivotY + 2.13, -0.17);
			RendererUtils.modifyMatricesDevelopment(matrices);

			VertexConsumer vertexConsumer = ModelLoader.BANNER_BASE.getVertexConsumer(vertexConsumers, RenderLayer::getEntityNoOutline);
			this.crossbar.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);

			RendererUtils.modifyMatricesBannerSwing(banner, entity, tickDelta, true);

			// Moving Effect
			Vec3d velocity = entity.getVelocity();
			velocity = makePositive(velocity);
			double speed = getHighestSideCord(velocity);
			matrices.multiply(Vec3f.POSITIVE_X.getRadialQuaternion((float) speed * 3));

			BannerBlockEntityRenderer.renderCanvas(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, banner, ModelLoader.BANNER_BASE, true, bannerPatterns);

			matrices.pop();

		}


	}

	private Vec3d makePositive(Vec3d vec3d) {
		if (vec3d.getX() < 0) vec3d = new Vec3d(-vec3d.getX(), vec3d.getY(), vec3d.getZ());
		if (vec3d.getY() < 0) vec3d = new Vec3d(vec3d.getX(), -vec3d.getY(), vec3d.getZ());
		if (vec3d.getZ() < 0) vec3d = new Vec3d(vec3d.getX(), vec3d.getY(), -vec3d.getZ());
		return vec3d;
	}

	private double getHighestSideCord(Vec3d vec3d) {
		if (vec3d.getX() > vec3d.getZ()) {
			return vec3d.getX();
		} else {
			return vec3d.getZ();
		}
	}

	private float getBonePivotY() {
		try {
			Field field = getContextModel().getClass().getDeclaredField("bone");
			field.setAccessible(true);
			ModelPart part = (ModelPart) field.get(getContextModel());
			return part.pivotY;

		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

}
