package com.ordana.grounded.blocks;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.ordana.grounded.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class WeatheringHelper {

    public static final Supplier<Map<Block, Block>> SOIL_TO_GRASSY = Suppliers.memoize(() ->
            ImmutableMap.<Block, Block>builder()
                    .put(ModBlocks.SANDY_DIRT.get(), ModBlocks.GRASSY_SANDY_DIRT.get())
                    .put(ModBlocks.EARTHEN_CLAY.get(), ModBlocks.GRASSY_EARTHEN_CLAY.get())
                    .put(ModBlocks.SILT.get(), ModBlocks.GRASSY_SILT.get())
                    .put(ModBlocks.PERMAFROST.get(), ModBlocks.GRASSY_PERMAFROST.get())
                    .put(Blocks.ROOTED_DIRT, ModBlocks.ROOTED_GRASS_BLOCK.get())
                    .put(Blocks.DIRT, Blocks.GRASS_BLOCK)
                    .build());

    static Optional<Block> getGrassySoil(Block block) {
        return Optional.ofNullable(SOIL_TO_GRASSY.get().get(block));
    }

    public static Optional<BlockState> getGrassySoil(BlockState state) {
        return getGrassySoil(state.getBlock()).map(block -> block.withPropertiesOf(state));
    }

    public static void growHangingRoots(ServerLevel level, RandomSource random, BlockPos pos) {
        Direction dir = Direction.values()[1 + random.nextInt(5)].getOpposite();
        BlockPos targetPos = pos.relative(dir);
        BlockState targetState = level.getBlockState(targetPos);
        FluidState fluidState = level.getFluidState(targetPos);
        boolean bl = fluidState.is(Fluids.WATER);
        if (targetState.canBeReplaced()) {
            BlockState newState = dir == Direction.DOWN ?
                    Blocks.HANGING_ROOTS.defaultBlockState() :
                    ModBlocks.HANGING_ROOTS_WALL.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, dir);
            level.setBlockAndUpdate(targetPos, newState.setValue(BlockStateProperties.WATERLOGGED, bl));
        }
    }

}
