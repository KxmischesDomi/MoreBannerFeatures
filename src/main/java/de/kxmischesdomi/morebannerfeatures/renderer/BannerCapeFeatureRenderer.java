package de.kxmischesdomi.morebannerfeatures.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.kxmischesdomi.morebannerfeatures.core.accessor.Bannerable;
import de.kxmischesdomi.morebannerfeatures.core.errors.ErrorSystemManager;
import de.kxmischesdomi.morebannerfeatures.utils.RendererUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelPart.Cube;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class BannerCapeFeatureRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

	private final ModelPart cloak;

	public BannerCapeFeatureRenderer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> featureRendererContext) {
		super(featureRendererContext);

		// OWN CLOAK WITH CUSTOM TEXTURE SIZE TO FIT THE BANNER TEXTURE
		CubeListBuilder modelPartBuilder = new CubeListBuilder();
		modelPartBuilder.texOffs(0, 0).addBox(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F);
		List<Cube> cuboids = modelPartBuilder.getCubes().stream().map(modelCuboidData -> modelCuboidData.bake(34, 27)).collect(Collectors.toList());
		cloak = new ModelPart(cuboids, new HashMap<>());
	}

	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, AbstractClientPlayer player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		// Safety try catch to avoid crashes!
		try {
			if (hasCustomBanner(player) && !player.isInvisible()) {
				ItemStack bannerItem = ((Bannerable) player).getBannerItem();
				ItemStack itemStack = player.getItemBySlot(EquipmentSlot.CHEST);
				if (bannerItem.getItem() instanceof BannerItem && !itemStack.is(Items.ELYTRA)) {
					matrices.pushPose();
					matrices.translate(0.0D, 0.0D, 0.125D);
					double d = Mth.lerp(tickDelta, player.xCloakO, player.xCloak) - Mth.lerp(tickDelta, player.xo, player.getX());
					double e = Mth.lerp(tickDelta, player.yCloakO, player.yCloak) - Mth.lerp(tickDelta, player.yo, player.getY());
					double m = Mth.lerp(tickDelta, player.zCloakO, player.zCloak) - Mth.lerp(tickDelta, player.zo, player.getZ());
					float n = player.yBodyRotO + (player.yBodyRot - player.yBodyRotO);
					double o = Mth.sin(n * 0.017453292F);
					double p = -Mth.cos(n * 0.017453292F);
					float q = (float)e * 10.0F;
					q = Mth.clamp(q, -6.0F, 32.0F);
					float r = (float)(d * o + m * p) * 100.0F;
					r = Mth.clamp(r, 0.0F, 150.0F);
					float s = (float)(d * p - m * o) * 100.0F;
					s = Mth.clamp(s, -20.0F, 20.0F);
					if (r < 0.0F) {
						r = 0.0F;
					}

					float t = Mth.lerp(tickDelta, player.oBob, player.bob);
					q += Mth.sin(Mth.lerp(tickDelta, player.walkDistO, player.walkDist) * 6.0F) * 32.0F * t;
					if (player.isCrouching()) {
						q += 25.0F;
						matrices.translate(0, 0.14, -0.02);
					}
					RendererUtils.modifyMatricesDevelopment(matrices);

					matrices.mulPose(Vector3f.XP.rotationDegrees(6.0F + r / 2.0F + q));
					matrices.mulPose(Vector3f.ZP.rotationDegrees(s / 2.0F));
					matrices.mulPose(Vector3f.YP.rotationDegrees(180.0F - s / 2.0F));

					RendererUtils.renderCanvasFromItem(bannerItem, matrices, vertexConsumers, light, OverlayTexture.NO_OVERLAY, cloak);

					matrices.popPose();
				}
			}
		} catch (Exception exception) {
			ErrorSystemManager.reportException();
			exception.printStackTrace();
		}
	}

	public boolean hasCustomBanner(AbstractClientPlayer playerEntity) {
		return playerEntity instanceof Bannerable && !((Bannerable) playerEntity).getBannerItem().isEmpty();
	}

}
