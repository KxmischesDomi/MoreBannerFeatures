package de.kxmischesdomi.morebannerfeatures.client.feature;

import com.mojang.datafixers.util.Pair;
import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.Bannerable;
import de.kxmischesdomi.morebannerfeatures.utils.RendererUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPart.Cuboid;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class BannerCapeFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	private final ModelPart cloak;

	public BannerCapeFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
		super(featureRendererContext);

		// OWN CLOAK WITH CUSTOM TEXTURE SIZE TO FIT THE BANNER TEXTURE
		ModelPartBuilder modelPartBuilder = new ModelPartBuilder();
		modelPartBuilder.uv(0, 0).cuboid(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F);
		List<Cuboid> cuboids = modelPartBuilder.build().stream().map(modelCuboidData -> modelCuboidData.createCuboid(34, 27)).collect(Collectors.toList());
		cloak = new ModelPart(cuboids, new HashMap<>());
	}

	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (hasCustomBanner(player) && !player.isInvisible()) {
			ItemStack itemStack = player.getEquippedStack(EquipmentSlot.CHEST);
			if (!itemStack.isOf(Items.ELYTRA)) {
				matrices.push();
				matrices.translate(0.0D, 0.0D, 0.125D);
				double d = MathHelper.lerp(tickDelta, player.prevCapeX, player.capeX) - MathHelper.lerp(tickDelta, player.prevX, player.getX());
				double e = MathHelper.lerp(tickDelta, player.prevCapeY, player.capeY) - MathHelper.lerp(tickDelta, player.prevY, player.getY());
				double m = MathHelper.lerp(tickDelta, player.prevCapeZ, player.capeZ) - MathHelper.lerp(tickDelta, player.prevZ, player.getZ());
				float n = player.prevBodyYaw + (player.bodyYaw - player.prevBodyYaw);
				double o = MathHelper.sin(n * 0.017453292F);
				double p = -MathHelper.cos(n * 0.017453292F);
				float q = (float)e * 10.0F;
				q = MathHelper.clamp(q, -6.0F, 32.0F);
				float r = (float)(d * o + m * p) * 100.0F;
				r = MathHelper.clamp(r, 0.0F, 150.0F);
				float s = (float)(d * p - m * o) * 100.0F;
				s = MathHelper.clamp(s, -20.0F, 20.0F);
				if (r < 0.0F) {
					r = 0.0F;
				}

				float t = MathHelper.lerp(tickDelta, player.prevStrideDistance, player.strideDistance);
				q += MathHelper.sin(MathHelper.lerp(tickDelta, player.prevHorizontalSpeed, player.horizontalSpeed) * 6.0F) * 32.0F * t;
				if (player.isInSneakingPose()) {
					q += 25.0F;
					RendererUtils.modifyMatricesDevelopment(matrices);
					matrices.translate(0, 0.14, -0.02);
				}

				matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(6.0F + r / 2.0F + q));
				matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(s / 2.0F));
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - s / 2.0F));

				ItemStack bannerItem = ((Bannerable) player).getBannerItem();
				List<Pair<BannerPattern, DyeColor>> patterns = BannerBlockEntity.getPatternsFromNbt(((BannerItem) bannerItem.getItem()).getColor(), BannerBlockEntity.getPatternListTag(bannerItem));

				RendererUtils.renderCanvas(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, cloak, ModelLoader.BANNER_BASE, true, patterns);

				matrices.pop();
			}
		}
	}

	public boolean hasCustomBanner(AbstractClientPlayerEntity playerEntity) {
		return playerEntity instanceof Bannerable && !((Bannerable) playerEntity).getBannerItem().isEmpty();
	}

}
