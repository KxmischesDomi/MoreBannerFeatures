package de.kxmischesdomi.morebannerfeatures.mixin.legacy.cat;

import com.mojang.datafixers.util.Pair;
import de.kxmischesdomi.morebannerfeatures.utils.RendererUtils;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.CatCollarFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.CatEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * NOT IMPLEMENTED AND NOT WOKING!!!
 *
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(CatCollarFeatureRenderer.class)
public abstract class CatCollarFeatureRendererMixin  extends FeatureRenderer<CatEntity, CatEntityModel<CatEntity>> {

	private static final Identifier SKIN = new Identifier("textures/entity/cat/cat_collar.png");
	private final CatEntityModel<CatEntity> model;
	private ModelPart collarPart;

	public CatCollarFeatureRendererMixin(FeatureRendererContext<CatEntity, CatEntityModel<CatEntity>> context, EntityModelLoader loader) {
		super(context);
		this.model = new CatEntityModel(loader.getModelPart(EntityModelLayers.CAT_COLLAR));
	}

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void init(FeatureRendererContext<CatEntity, CatEntityModel<CatEntity>> context, EntityModelLoader loader, CallbackInfo ci) {
		collarPart = loader.getModelPart(EntityModelLayers.CAT_COLLAR);
	}

	@Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
	private void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CatEntity entity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		ItemStack itemStack = Items.BLACK_BANNER.getDefaultStack();
		List<Pair<BannerPattern, DyeColor>> patterns = BannerBlockEntity.getPatternsFromNbt(((BannerItem)itemStack.getItem()).getColor(), BannerBlockEntity.getPatternListTag(itemStack));
		patterns.add(new Pair<>(BannerPattern.BRICKS, DyeColor.BLUE));

		if (entity.isTamed()) {
			render(this.getContextModel(), this.model, SKIN, matrices, vertexConsumers, light, entity, f, g, j, k, l, h, 0, 0, 0);
			RendererUtils.renderCanvas(matrices, vertexConsumers, light, LivingEntityRenderer.getOverlay(entity, 0.0F), collarPart, ModelLoader.BANNER_BASE, true, patterns);

//			for(int i = 0; i < 17 && i < patterns.size(); ++i) {
//				Pair<BannerPattern, DyeColor> pair = (Pair)patterns.get(i);
//				float[] fs = ((DyeColor)pair.getSecond()).getColorComponents();
//				BannerPattern bannerPattern = (BannerPattern)pair.getFirst();
//				SpriteIdentifier spriteIdentifier = TexturedRenderLayers.getBannerPatternTextureId(bannerPattern);
//
//				renderModel(this.model, SKIN, matrices, vertexConsumers, light, entity, fs[0], fs[1], fs[2]);
////				renderBannerModel(this.model, SKIN, matrices, spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutoutNoCull), light, entity, fs[0], fs[1], fs[2]);
////				renderModel(model, SKIN, matrices, vertexConsumers, light, entity, fs[0], fs[1], fs[2], 1.0F);
//			}

		}

		ci.cancel();
	}

	protected <T extends LivingEntity> void renderBannerModel(EntityModel<T> model, Identifier texture, MatrixStack matrices, VertexConsumer vertexConsumer, int light, T entity, float red, float green, float blue) {
//		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(texture));
		model.render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0.0F), red, green, blue, 1.0F);
	}

}
