package de.kxmischesdomi.morebannerfeatures.mixin.player;

import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.Bannerable;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.4
 */
@Mixin(CapeFeatureRenderer.class)
public abstract class CapeFeatureRendererMixin extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	public CapeFeatureRendererMixin(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
		super(context);
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;canRenderCapeTexture()Z"))
	public boolean canRenderCapeTexture(AbstractClientPlayerEntity abstractClientPlayerEntity) {
		return abstractClientPlayerEntity.canRenderCapeTexture() && !hasCustomBanner(abstractClientPlayerEntity);
	}

	public boolean hasCustomBanner(AbstractClientPlayerEntity playerEntity) {
		return playerEntity instanceof Bannerable && !((Bannerable) playerEntity).getBannerItem().isEmpty();
	}

}
