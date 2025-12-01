package com.ordana.grounded.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ModFallingPathBlock extends FallingBlock {
    protected static final VoxelShape SHAPE;

    public ModFallingPathBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return simpleCodec(ModFallingPathBlock::new);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ? Block.pushEntitiesUp(this.defaultBlockState(), Blocks.DIRT.defaultBlockState(), context.getLevel(), context.getClickedPos()) : super.getStateForPlacement(context);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP && !state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 1);
        }
        level.scheduleTick(pos, this, this.getDelayAfterPlace());
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinBuildHeight()) {
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level, pos, state);
            this.falling(fallingBlockEntity);
        }
        FarmBlock.turnToDirt(null, state, level, pos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos.above());
        return !blockState.isSolid() || blockState.getBlock() instanceof FenceGateBlock;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return false;
    }

    static {
        SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);
    }
}
