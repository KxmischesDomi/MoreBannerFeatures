package de.kxmischesdomi.morebannerfeatures.mixin.strider;

import de.kxmischesdomi.morebannerfeatures.renderer.feature.StriderBannerFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.StriderEntityRenderer;
import net.minecraft.client.render.entity.model.StriderEntityModel;
import net.minecraft.entity.passive.StriderEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(StriderEntityRenderer.class)
public abstract class StriderEntityRendererMixin extends MobEntityRenderer<StriderEntity, StriderEntityModel<StriderEntity>> {

	public StriderEntityRendererMixin(Context context, StriderEntityModel<StriderEntity> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context context, CallbackInfo ci) {
		addFeature(new StriderBannerFeatureRenderer(this));
	}



}
