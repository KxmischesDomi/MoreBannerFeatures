package de.kxmischesdomi.morebannerfeatures.mixin.banner;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0.1
 */
@Mixin(WallStandingBlockItem.class)
public abstract class WallStandingBlockItemMixin extends BlockItem {

	@Shadow @Final protected Block wallBlock;

	public WallStandingBlockItemMixin(Block block, Settings settings) {
		super(block, settings);
	}

	@Inject(method = "getPlacementState", locals = LocalCapture.CAPTURE_FAILSOFT, at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/Direction;UP:Lnet/minecraft/util/math/Direction;"), cancellable = true)
	private void getPlacementState(ItemPlacementContext context, CallbackInfoReturnable<BlockState> cir, BlockState blockState, WorldView worldView, BlockPos blockPos, Direction[] directions, int i, int i1) {
		Direction direction = directions[i1];
		if (direction != Direction.UP) return;

		WallStandingBlockItem blockItem = (WallStandingBlockItem) (Object) this;
		if (blockItem instanceof BannerItem bannerItem) {
			blockState = this.wallBlock.getPlacementState(context);

			BlockState blockState3 = direction == Direction.UP ? this.getBlock().getPlacementState(context) : blockState;
			if (blockState3 != null && blockState3.canPlaceAt(worldView, blockPos)) {
				cir.setReturnValue(blockState3 != null && worldView.canPlace(blockState3, blockPos, ShapeContext.absent()) ? blockState3 : null);
			}

		}

	}

}