package de.kxmischesdomi.morebannerfeatures.mixin.player;

import de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures.cloak.CloakInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements CloakInventory {

	public DefaultedList<ItemStack> cloak;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(PlayerEntity player, CallbackInfo ci) {
		this.cloak = DefaultedList.ofSize(1, ItemStack.EMPTY);
	}

	@Override
	public void setCloak(ItemStack itemStack) {
		cloak.set(0, itemStack);
	}

	@Override
	public ItemStack getCloak() {
		return cloak.get(0);
	}

	@Inject(method = "readNbt", at = @At("HEAD"))
	private void readNBT(NbtList nbtList, CallbackInfo ci) {
		this.cloak.clear();

		for(int i = 0; i < nbtList.size(); ++i) {
			NbtCompound nbtCompound = nbtList.getCompound(i);
			int j = nbtCompound.getByte("Slot") & 255;
			ItemStack itemStack = ItemStack.fromNbt(nbtCompound);
			if (!itemStack.isEmpty()) {
				if (j >= 200 && j < this.cloak.size() + 200) {
					this.cloak.set(j - 200, itemStack);
				}
			}
		}

	}

	@Inject(method = "writeNbt", at = @At("TAIL"))
	private void writeNbt(NbtList nbtList, CallbackInfoReturnable<NbtList> cir) {
		int k;
		for(k = 0; k < this.cloak.size(); ++k) {
			if (!this.cloak.get(k).isEmpty()) {
				NbtCompound nbtCompound3 = new NbtCompound();
				nbtCompound3.putByte("Slot", (byte)(k + 200));
				this.cloak.get(k).writeNbt(nbtCompound3);
				nbtList.add(nbtCompound3);
			}
		}
	}

//	@ModifyArgs(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;"))
//	private void immutableListOf(Args args) {
////		args.set(3, cloak);
//	}

}
