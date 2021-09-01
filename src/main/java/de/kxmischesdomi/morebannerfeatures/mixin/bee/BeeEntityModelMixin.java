package de.kxmischesdomi.morebannerfeatures.mixin.bee;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BeeEntityModel;
import net.minecraft.entity.passive.BeeEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(BeeEntityModel.class)
public abstract class BeeEntityModelMixin<T extends BeeEntity> {

	@Shadow @Final private ModelPart frontLegs;

	@Shadow @Final private ModelPart bone;

	@Inject(method = "setAngles", at = @At(value = "TAIL"))
	private void setAngles(T beeEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
		this.bone.pitch = 0;
		this.frontLegs.pitch = 0;
	}

}
