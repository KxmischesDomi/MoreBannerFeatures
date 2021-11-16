package de.kxmischesdomi.morebannerfeatures.mixin.boat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import de.kxmischesdomi.morebannerfeatures.core.accessor.Bannerable;
import de.kxmischesdomi.morebannerfeatures.core.errors.ErrorSystemManager;
import de.kxmischesdomi.morebannerfeatures.utils.RendererUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(BoatRenderer.class)
public abstract class BoatRendererMixin extends EntityRenderer<Boat> {

	private final ModelPart modelPart = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.BANNER);
	private final ModelPart banner = modelPart.getChild("flag");
	private final ModelPart pillar = modelPart.getChild("pole");
	private final ModelPart crossbar = modelPart.getChild("bar");

	public BoatRendererMixin(EntityRendererProvider.Context context) {
		super(context);
	}

	@Inject(method = "render", at = @At("TAIL"))
	private void render(Boat entity, float f, float g, PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
		try {

			if (entity instanceof Bannerable bannerable) {
				ItemStack itemStack = bannerable.getBannerItem();
				if (itemStack == null || !(itemStack.getItem() instanceof BannerItem)) return;

				matrices.pushPose();

				matrices.mulPose(Vector3f.YP.rotationDegrees(180 - f));

				float h = entity.getHurtTime() - g;
				float j = entity.getDamage() - g;
				if (j < 0.0F) {
					j = 0.0F;
				}
				if (h > 0.0F) {
					matrices.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(h) * h * j / 10.0F * entity.getHurtDir()));
				}

				float k = entity.getBubbleAngle(g);
				if (!Mth.equal(k, 0.0F)) {
					matrices.mulPose(new Quaternion(new Vector3f(1.0F, 0.0F, 1.0F), entity.getBubbleAngle(g), true));
				}

				matrices.mulPose(Vector3f.YP.rotationDegrees(180));

				matrices.translate(0, 1.05, -0.937);

				matrices.scale(0.6666667F, -0.6666667F, -0.6666667F);

				VertexConsumer vertexConsumer = ModelBakery.BANNER_BASE.buffer(vertexConsumers, RenderType::entityNoOutline);
				this.pillar.render(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
				this.crossbar.render(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY);

				RendererUtils.modifyMatricesBannerSwing(banner, entity, f, true);

				RendererUtils.renderCanvasFromItem(itemStack, matrices, vertexConsumers, light, OverlayTexture.NO_OVERLAY, banner);

				matrices.popPose();
			}
		} catch (Exception exception) {
			ErrorSystemManager.reportException();
			exception.printStackTrace();
		}
	}

}
