package de.kxmischesdomi.morebannerfeatures.mixin.boat;

import de.kxmischesdomi.morebannerfeatures.core.accessor.Bannerable;
import de.kxmischesdomi.morebannerfeatures.core.errors.ErrorSystemManager;
import de.kxmischesdomi.morebannerfeatures.utils.RendererUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(BoatEntityRenderer.class)
public abstract class BoatEntityRendererMixin extends EntityRenderer<BoatEntity> {

	private final ModelPart modelPart = MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.BANNER);
	private final ModelPart banner = modelPart.getChild("flag");
	private final ModelPart pillar = modelPart.getChild("pole");
	private final ModelPart crossbar = modelPart.getChild("bar");

	public BoatEntityRendererMixin(EntityRendererFactory.Context context) {
		super(context);
	}

	@Inject(method = "render", at = @At("TAIL"))
	private void render(BoatEntity entity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		try {

			if (entity instanceof Bannerable bannerable) {
				ItemStack itemStack = bannerable.getBannerItem();
				if (itemStack == null || !(itemStack.getItem() instanceof BannerItem)) return;

				matrices.push();

				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180 - f));

				float h = (float) entity.getDamageWobbleTicks() - g;
				float j = entity.getDamageWobbleStrength() - g;
				if (j < 0.0F) {
					j = 0.0F;
				}
				if (h > 0.0F) {
					matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(MathHelper.sin(h) * h * j / 10.0F * (float) entity.getDamageWobbleSide()));
				}

				float k = entity.interpolateBubbleWobble(g);
				if (!MathHelper.approximatelyEquals(k, 0.0F)) {
					matrices.multiply(new Quaternion(new Vec3f(1.0F, 0.0F, 1.0F), entity.interpolateBubbleWobble(g), true));
				}

				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));

				matrices.translate(0, 1.05, -0.937);

				matrices.scale(0.6666667F, -0.6666667F, -0.6666667F);

				VertexConsumer vertexConsumer = ModelLoader.BANNER_BASE.getVertexConsumer(vertexConsumers, RenderLayer::getEntityNoOutline);
				this.pillar.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
				this.crossbar.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);

				Vec3d pos = entity.getPos();
				long m = entity.world.getTime();
				float n = ((float) Math.floorMod((long) (pos.getX() * 7 + pos.getY() * 9 + pos.getZ() * 13) + m, 100L) + g + f) / 100.0F;
				this.banner.pitch = (-0.0125F + 0.01F * MathHelper.cos(6.2831855F * n)) * 3.1415927F;
				this.banner.pivotY = -32.0F;

				RendererUtils.renderCanvasFromItem(itemStack, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, banner);

				matrices.pop();
			}
		} catch (Exception exception) {
			ErrorSystemManager.reportException();
			exception.printStackTrace();
		}
	}

}
