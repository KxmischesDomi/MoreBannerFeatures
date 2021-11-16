package de.kxmischesdomi.morebannerfeatures.mixin.strider;

import de.kxmischesdomi.morebannerfeatures.renderer.StriderBannerFeatureRenderer;
import net.minecraft.client.model.StriderModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.StriderRenderer;
import net.minecraft.world.entity.monster.Strider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(StriderRenderer.class)
public abstract class StriderEntityRendererMixin extends MobRenderer<Strider, StriderModel<Strider>> {

	public StriderEntityRendererMixin(Context context, StriderModel<Strider> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context context, CallbackInfo ci) {
		addLayer(new StriderBannerFeatureRenderer(this));
	}



}
