package de.kxmischesdomi.morebannerfeatures.mixin.banner;

import de.kxmischesdomi.morebannerfeatures.core.config.MBFOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
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
@Mixin(StandingAndWallBlockItem.class)
public abstract class StandingAndWallBlockItemMixin extends BlockItem {

	@Shadow @Final protected Block wallBlock;

	public StandingAndWallBlockItemMixin(Block block, Properties settings) {
		super(block, settings);
	}

	@Inject(method = "getPlacementState", locals = LocalCapture.CAPTURE_FAILSOFT, at = @At(value = "FIELD", target = "Lnet/minecraft/core/Direction;UP:Lnet/minecraft/core/Direction;"), cancellable = true)
	private void getPlacementState(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir, BlockState blockState, LevelReader worldView, BlockPos blockPos, Direction[] directions, int i, int i1) {

		if (!MBFOptions.HANGING_BANNERS.getBooleanValue()) {
			return;
		}

		Direction direction = directions[i1];
		if (direction != Direction.UP) return;

		StandingAndWallBlockItem blockItem = (StandingAndWallBlockItem) (Object) this;
		if (blockItem instanceof BannerItem bannerItem) {
			blockState = this.wallBlock.getStateForPlacement(context);

			BlockState blockState3 = direction == Direction.UP ? this.getBlock().getStateForPlacement(context) : blockState;
			if (blockState3 != null && blockState3.canSurvive(worldView, blockPos)) {
				cir.setReturnValue(blockState3 != null && worldView.isUnobstructed(blockState3, blockPos, CollisionContext.empty()) ? blockState3 : null);
			}

		}

	}

}