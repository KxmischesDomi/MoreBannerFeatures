package de.kxmischesdomi.morebannerfeatures.mixin.pig;

import de.kxmischesdomi.morebannerfeatures.renderer.PigBannerFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.PigEntityRenderer;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.entity.passive.PigEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(PigEntityRenderer.class)
public abstract class PigEntityRendererMixin extends MobEntityRenderer<PigEntity, PigEntityModel<PigEntity>> {

	public PigEntityRendererMixin(Context context, PigEntityModel<PigEntity> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context context, CallbackInfo ci) {
		addFeature(new PigBannerFeatureRenderer(this));
	}

}
