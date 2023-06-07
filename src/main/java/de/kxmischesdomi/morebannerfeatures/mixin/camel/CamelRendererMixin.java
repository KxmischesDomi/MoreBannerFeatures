package de.kxmischesdomi.morebannerfeatures.mixin.camel;

import de.kxmischesdomi.morebannerfeatures.renderer.CamelBannerFeatureRenderer;
import net.minecraft.client.model.CamelModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.CamelRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.animal.camel.Camel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(CamelRenderer.class)
public abstract class CamelRendererMixin extends MobRenderer<Camel, CamelModel<Camel>> {

	public CamelRendererMixin(Context context, CamelModel<Camel> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context ctx, ModelLayerLocation layer, CallbackInfo ci) {
		addLayer(new CamelBannerFeatureRenderer(this));
	}

}
