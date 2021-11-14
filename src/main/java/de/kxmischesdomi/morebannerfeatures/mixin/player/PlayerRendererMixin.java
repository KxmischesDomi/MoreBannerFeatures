package de.kxmischesdomi.morebannerfeatures.mixin.player;

import de.kxmischesdomi.morebannerfeatures.renderer.BannerCapeFeatureRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0.4
 */
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	public PlayerRendererMixin(Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context ctx, boolean slim, CallbackInfo ci) {
		addFeature(new BannerCapeFeatureRenderer(this));
	}

}
