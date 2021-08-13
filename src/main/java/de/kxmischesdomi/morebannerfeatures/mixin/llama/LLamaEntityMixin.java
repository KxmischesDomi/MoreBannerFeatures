package de.kxmischesdomi.morebannerfeatures.mixin.llama;

import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.SideBannerable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(LlamaEntity.class)
public abstract class LLamaEntityMixin extends AbstractDonkeyEntity implements SideBannerable {

	@Shadow @Nullable public abstract DyeColor getCarpetColor();

	public LLamaEntityMixin(EntityType<? extends AbstractDonkeyEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public float getXOffset() {
		return getCarpetColor() == null ? 0.13F : 0.2F;
	}

	@Override
	public float getYOffset() {
		return getCarpetColor() == null ? 0.14F : 0.21F;
	}

	@Override
	public float getZOffset() {
		return 0F;
	}
}
