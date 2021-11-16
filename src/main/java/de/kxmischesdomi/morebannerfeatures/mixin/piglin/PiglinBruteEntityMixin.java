package de.kxmischesdomi.morebannerfeatures.mixin.piglin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(PiglinBrute.class)
public abstract class PiglinBruteEntityMixin extends AbstractPiglin {

	@Shadow public abstract Brain<PiglinBrute> getBrain();

	private static final ItemStack HEAD_BANNER;

	public PiglinBruteEntityMixin(EntityType<? extends AbstractPiglin> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "initialize", at = @At(value = "HEAD"))
	private void initialize(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, SpawnGroupData entityData, CompoundTag entityNbt, CallbackInfoReturnable<SpawnGroupData> cir) {
		if (spawnReason == MobSpawnType.STRUCTURE) {
			this.setItemSlot(EquipmentSlot.HEAD, HEAD_BANNER);
		}
	}

	@Override
	protected void dropEquipment() {
		spawnAtLocation(getItemBySlot(EquipmentSlot.HEAD));
		super.dropEquipment();
	}

	static {
		HEAD_BANNER = new ItemStack(Items.BROWN_BANNER);
		HEAD_BANNER.setHoverName(new TranslatableComponent("block.minecraft.fortune_banner"));
		CompoundTag nbtCompound = HEAD_BANNER.getOrCreateTagElement("BlockEntityTag");

		ListTag nbtList = new ListTag();
		nbtCompound.put("Patterns", nbtList);

		CompoundTag snoutPattern = new CompoundTag();
		snoutPattern.putString("Pattern", BannerPattern.PIGLIN.getHashname());
		snoutPattern.putInt("Color", DyeColor.YELLOW.getId());
		nbtList.add(snoutPattern);

	}


}
