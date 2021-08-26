package de.kxmischesdomi.morebannerfeatures.mixin.banner;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.2
 */
@Mixin(BannerBlock.class)
public abstract class BannerBlockMixin extends AbstractBannerBlock {

	private static final BooleanProperty HANGING;

	protected BannerBlockMixin(DyeColor color, Settings settings) {
		super(color, settings);
	}

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void init(DyeColor dyeColor, Settings settings, CallbackInfo ci) {
		this.setDefaultState(this.getDefaultState().with(HANGING, false));
	}

	@Inject(method = "getPlacementState", at = @At(value = "TAIL"), cancellable = true)
	private void getPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
		BlockState state = cir.getReturnValue();
		if (state == null) return;

		Direction[] var3 = ctx.getPlacementDirections();
		int var4 = var3.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			Direction direction = var3[var5];
			if (direction == Direction.UP && ctx.getVerticalPlayerLookDirection() == Direction.UP) {
				if (ctx.getWorld().getBlockState(ctx.getBlockPos().up()).getMaterial().isSolid()) {
					cir.setReturnValue(state.with(HANGING, true));
				}

			}
		}

	}

	@Inject(method = "appendProperties", at = @At(value = "TAIL"))
	private void appendProperties(Builder<Block, BlockState> builder, CallbackInfo ci) {
		builder.add(HANGING);
	}

	@Inject(method = "getOutlineShape", at = @At(value = "TAIL"), cancellable = true)
	private void getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
		if (state.get(HANGING)) {
			cir.setReturnValue(Block.createCuboidShape(1.3D, 14.0D, 1.3D, 14.7D, 16.0D, 14.7D));
		}
	}

	@Inject(method = "canPlaceAt", at = @At(value = "TAIL"), cancellable = true)
	private void canPlaceAt(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (state.get(HANGING) || !cir.getReturnValue()) {
			cir.setReturnValue(world.getBlockState(pos.up()).getMaterial().isSolid());
		}
	}

	@Inject(method = "getStateForNeighborUpdate", at = @At(value = "HEAD"), cancellable = true)
	private void getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
		if (state.get(HANGING)) {
			cir.setReturnValue(direction == Direction.UP && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos));
		}
	}

	static {
		HANGING = Properties.HANGING;
	}

}
