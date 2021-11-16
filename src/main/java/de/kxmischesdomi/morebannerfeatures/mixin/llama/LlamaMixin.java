package de.kxmischesdomi.morebannerfeatures.mixin.llama;

import de.kxmischesdomi.morebannerfeatures.core.accessor.SideBannerable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(Llama.class)
public abstract class LlamaMixin extends AbstractChestedHorse implements SideBannerable {

	@Shadow public @Nullable abstract DyeColor getSwag();

	public LlamaMixin(EntityType<? extends AbstractChestedHorse> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public float getXOffset() {
		return getSwag() == null ? 0.13F : 0.2F;
	}

	@Override
	public float getYOffset() {
		return getSwag() == null ? 0.14F : 0.21F;
	}

	@Override
	public float getZOffset() {
		return 0F;
	}
}
