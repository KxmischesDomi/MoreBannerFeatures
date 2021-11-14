package de.kxmischesdomi.morebannerfeatures.mixin.horse;

import de.kxmischesdomi.morebannerfeatures.renderer.feature.HorseBaseBannerFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.HorseBaseEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.entity.passive.HorseBaseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(HorseBaseEntityRenderer.class)
public abstract class HorseBaseEntityRendererMixin<M extends HorseEntityModel<HorseBaseEntity>> extends MobEntityRenderer<HorseBaseEntity, HorseEntityModel<HorseBaseEntity>> {

	public HorseBaseEntityRendererMixin(Context context, HorseEntityModel<HorseBaseEntity> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context ctx, M model, float scale, CallbackInfo ci) {
		addFeature(new HorseBaseBannerFeatureRenderer(this));
	}

}
