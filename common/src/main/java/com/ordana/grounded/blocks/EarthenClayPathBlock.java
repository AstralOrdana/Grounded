package com.ordana.grounded.blocks;

import com.ordana.grounded.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirtPathBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.Objects;

public class EarthenClayPathBlock extends DirtPathBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public EarthenClayPathBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(WATERLOGGED);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {

        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        boolean bl = fluidState.getType() == Fluids.WATER;
        return !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ? Block.pushEntitiesUp(this.defaultBlockState(), Objects.requireNonNull(ModBlocks.EARTHEN_CLAY.get().getStateForPlacement(context)), context.getLevel(), context.getClickedPos()) : Objects.requireNonNull(super.getStateForPlacement(context)).setValue(WATERLOGGED, bl);
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        turnToDirt(state, level, pos);
    }



    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (direction == Direction.UP && !state.canSurvive(level, currentPos)) {
            level.scheduleTick(currentPos, this, 1);
        }
        return state;
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            if (random.nextInt(25) == 1) {
                BlockPos blockpos = pos.below();
                BlockState blockstate = level.getBlockState(blockpos);
                if (!blockstate.canOcclude() || !blockstate.isFaceSturdy(level, blockpos, Direction.UP)) {
                    double d0 = pos.getX() + random.nextDouble();
                    double d1 = pos.getY() - 0.05D;
                    double d2 = pos.getZ() + random.nextDouble();
                    level.addParticle(ParticleTypes.DRIPPING_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    public static boolean isNearWater(LevelReader level, BlockPos pos) {
        boolean isNear = false;
        for (Direction dir : Direction.values()) {
            var relativeBlock = level.getBlockState(pos.relative(dir));
            if (level.getFluidState(pos.relative(dir)).is(FluidTags.WATER) && (!relativeBlock.is(ModBlocks.EARTHEN_CLAY.get()) && !relativeBlock.is(ModBlocks.GRASSY_EARTHEN_CLAY.get()) && !relativeBlock.is(ModBlocks.EARTHEN_CLAY_PATH.get()))) isNear = true;
        }
        return isNear;
    }


    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos pos, RandomSource randomSource) {
        if (!blockState.getValue(WATERLOGGED) && serverLevel.isRainingAt(pos.above()) || isNearWater(serverLevel, pos)) {
            serverLevel.setBlock(pos, blockState.setValue(WATERLOGGED, true), 2);
        } else if (blockState.getValue(WATERLOGGED) && serverLevel.dimensionType().ultraWarm()) {
            serverLevel.setBlock(pos, blockState.setValue(WATERLOGGED, false), 2);
        }
    }

    public static void turnToDirt(BlockState state, Level level, BlockPos pos) {
        level.setBlockAndUpdate(pos, pushEntitiesUp(state, ModBlocks.EARTHEN_CLAY.get().withPropertiesOf(state), level, pos));
    }
}
