package de.kxmischesdomi.morebannerfeatures.mixin.legacy.dog;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.WolfCollarFeatureRenderer;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.WolfEntity;
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
@Mixin(WolfCollarFeatureRenderer.class)
public abstract class WolfCollarFeatureRendererMixin extends FeatureRenderer<WolfEntity, WolfEntityModel<WolfEntity>> {
	private static final Identifier SKIN = new Identifier("textures/entity/wolf/wolf_collar.png");

	public WolfCollarFeatureRendererMixin(FeatureRendererContext<WolfEntity, WolfEntityModel<WolfEntity>> context) {
		super(context);
	}

	@Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
	private void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, WolfEntity entity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {

		if (entity.isTamed() && !entity.isInvisible()) {

			ItemStack itemStack = Items.BLACK_BANNER.getDefaultStack();
			List<Pair<BannerPattern, DyeColor>> patterns = BannerBlockEntity.getPatternsFromNbt(((BannerItem)itemStack.getItem()).getColor(), BannerBlockEntity.getPatternListTag(itemStack));
			patterns.add(new Pair<>(BannerPattern.BRICKS, DyeColor.BLUE));

			FeatureRenderer.renderModel(this.getContextModel(), SKIN, matrices, vertexConsumers, light, entity, 0, 0, 0);
//			BannerBlockEntityRenderer.renderCanvas(matrices, vertexConsumers, light, LivingEntityRenderer.getOverlay(entity, 0.0F), getContextModel(), ModelLoader.BANNER_BASE, true, patterns);
		}

		ci.cancel();
	}

}
