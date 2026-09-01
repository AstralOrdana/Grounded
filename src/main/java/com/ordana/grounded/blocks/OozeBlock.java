package com.ordana.grounded.blocks;

import com.mojang.serialization.MapCodec;
import com.ordana.grounded.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class OozeBlock extends FallingBlock {

	private static final VoxelShape SHAPE = column(16.0, 0.0, 14.0);
	private static final MapCodec<OozeBlock> CODEC = simpleCodec(OozeBlock::new);

	public OozeBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends FallingBlock> codec() {
		return CODEC;
	}

	@Override
	public int getDustColor(BlockState blockState, BlockGetter level, BlockPos pos) {
		return blockState.getMapColor(level, pos).col;
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
		return Shapes.block();
	}

	@Override
	protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return Shapes.block();
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}

	@Override
	protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
		return 0.2F;
	}

	@Override
	public float getSpeedFactor() {
		return 0.8F;
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		BlockPos.MutableBlockPos above = pos.above().mutable();
		BlockState aboveState = level.getBlockState(above);

		if (aboveState.is(ModTags.OOZE_CROPS)) {
			if (aboveState.getBlock() instanceof GrowingPlantBodyBlock plantBlock)
				plantBlock.getHeadPos(level, pos, plantBlock).ifPresent(above::set);

			level.getBlockState(above).randomTick(level, above, random);
		}
	}

}
