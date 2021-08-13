package de.kxmischesdomi.morebannerfeatures.mixin.piglin;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.PiglinBruteEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.LiteralText;
import net.minecraft.util.DyeColor;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(PiglinBruteEntity.class)
public abstract class PiglinBruteEntityMixin extends AbstractPiglinEntity {

	@Shadow public abstract Brain<PiglinBruteEntity> getBrain();

	private static final ItemStack HEAD_BANNER;

	public PiglinBruteEntityMixin(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "initialize", at = @At(value = "HEAD"))
	private void initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
		if (spawnReason == SpawnReason.STRUCTURE) {
			this.equipStack(EquipmentSlot.HEAD, HEAD_BANNER);
		}
	}

	@Override
	protected void dropInventory() {
		dropStack(getEquippedStack(EquipmentSlot.HEAD));
		super.dropInventory();
	}

	static {
		HEAD_BANNER = new ItemStack(Items.BROWN_BANNER);
		HEAD_BANNER.setCustomName(new LiteralText("§eFortune Banner"));
		NbtCompound nbtCompound = HEAD_BANNER.getOrCreateSubNbt("BlockEntityTag");

		NbtList nbtList = new NbtList();
		nbtCompound.put("Patterns", nbtList);

		NbtCompound snoutPattern = new NbtCompound();
		snoutPattern.putString("Pattern", BannerPattern.PIGLIN.getId());
		snoutPattern.putInt("Color", DyeColor.YELLOW.getId());
		nbtList.add(snoutPattern);

	}


}
