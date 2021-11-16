package de.kxmischesdomi.morebannerfeatures.mixin.horse;

import de.kxmischesdomi.morebannerfeatures.renderer.HorseBaseBannerFeatureRenderer;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(AbstractHorseRenderer.class)
public abstract class AbstractHorseRendererMixin<M extends HorseModel<AbstractHorse>> extends MobRenderer<AbstractHorse, HorseModel<AbstractHorse>> {

	public AbstractHorseRendererMixin(Context context, HorseModel<AbstractHorse> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context ctx, M model, float scale, CallbackInfo ci) {
		addLayer(new HorseBaseBannerFeatureRenderer(this));
	}

}
