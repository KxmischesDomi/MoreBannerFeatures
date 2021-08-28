package de.kxmischesdomi.morebannerfeatures.mixin.legacy.bee;

import de.kxmischesdomi.morebannerfeatures.client.feature.BeeBannerFeatureRenderer;
import net.minecraft.client.render.entity.BeeEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BeeEntityModel;
import net.minecraft.entity.passive.BeeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(BeeEntityRenderer.class)
public abstract class BeeEntityRendererMixin extends MobEntityRenderer<BeeEntity, BeeEntityModel<BeeEntity>> {

	public BeeEntityRendererMixin(Context context, BeeEntityModel<BeeEntity> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context context, CallbackInfo ci) {
		this.addFeature(new BeeBannerFeatureRenderer(this));
	}

}
