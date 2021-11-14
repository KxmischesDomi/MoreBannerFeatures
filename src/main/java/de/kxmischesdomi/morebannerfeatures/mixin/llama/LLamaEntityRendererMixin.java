package de.kxmischesdomi.morebannerfeatures.mixin.llama;

import de.kxmischesdomi.morebannerfeatures.renderer.LLamaBannerFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.LlamaEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.LlamaEntityModel;
import net.minecraft.entity.passive.LlamaEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(LlamaEntityRenderer.class)
public abstract class LLamaEntityRendererMixin extends MobEntityRenderer<LlamaEntity, LlamaEntityModel<LlamaEntity>> {

	public LLamaEntityRendererMixin(Context context, LlamaEntityModel<LlamaEntity> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context ctx, EntityModelLayer layer, CallbackInfo ci) {
		addFeature(new LLamaBannerFeatureRenderer(this));
	}

}
