package de.kxmischesdomi.morebannerfeatures.mixin.trader;

import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.SideBannerable;
import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.InventoryBannerable;
import de.kxmischesdomi.morebannerfeatures.utils.BannerUtils;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.TraderLlamaEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * NOT IMPLEMENTED!!!
 *
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(TraderLlamaEntity.class)
public abstract class TraderLLamaEntityMixin extends AbstractDonkeyEntity implements SideBannerable, InventoryBannerable {

	public TraderLLamaEntityMixin(EntityType<? extends AbstractDonkeyEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "initialize", at = @At("HEAD"))
	private void initEquipment(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
//		if (spawnReason == SpawnReason.EVENT) {
			this.initEquipment(difficulty);
//		}
	}

	@Override
	protected void initEquipment(LocalDifficulty difficulty) {
		items.setStack(getSlot(), BannerUtils.getRandomBannerItemStack(random));
		super.initEquipment(difficulty);
	}

	@Override
	public float getXOffset() {
		return 0.2F;
	}

	@Override
	public float getYOffset() {
		return 0.21F;
	}

	@Override
	public float getZOffset() {
		return 0F;
	}
}
