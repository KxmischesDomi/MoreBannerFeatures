package de.kxmischesdomi.morebannerfeatures.mixin.llama;

import de.kxmischesdomi.morebannerfeatures.renderer.LLamaBannerFeatureRenderer;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LlamaRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.animal.horse.Llama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(LlamaRenderer.class)
public abstract class LlamaRendererMixin extends MobRenderer<Llama, LlamaModel<Llama>> {

	public LlamaRendererMixin(Context context, LlamaModel<Llama> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context ctx, ModelLayerLocation layer, CallbackInfo ci) {
		addLayer(new LLamaBannerFeatureRenderer(this));
	}

}
