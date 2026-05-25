package com.ordana.grounded.blocks;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.ordana.grounded.reg.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

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
                    .put(Blocks.DIRT, Blocks.GRASS_BLOCK)
                    .build());

    public static final Supplier<Map<Block, Block>> GRASSY_TO_SOIL = Suppliers.memoize(() ->
            ImmutableMap.<Block, Block>builder()
                    .put(ModBlocks.GRASSY_SANDY_DIRT.get(), ModBlocks.SANDY_DIRT.get())
                    .put(ModBlocks.GRASSY_EARTHEN_CLAY.get(), ModBlocks.EARTHEN_CLAY.get())
                    .put(ModBlocks.GRASSY_SILT.get(), ModBlocks.SILT.get())
                    .put(ModBlocks.GRASSY_PERMAFROST.get(), ModBlocks.PERMAFROST.get())
                    .put(Blocks.GRASS_BLOCK, Blocks.DIRT)
                    .build());

    static Optional<Block> getSoilFromGrass(Block block) {
        return Optional.ofNullable(GRASSY_TO_SOIL.get().get(block));
    }

    public static Optional<BlockState> getSoilFromGrass(BlockState state) {
        return getSoilFromGrass(state.getBlock()).map(block -> block.withPropertiesOf(state));
    }

    static Optional<Block> getGrassySoil(Block block) {
        return Optional.ofNullable(SOIL_TO_GRASSY.get().get(block));
    }

    public static Optional<BlockState> getGrassySoil(BlockState state) {
        return getGrassySoil(state.getBlock()).map(block -> block.withPropertiesOf(state));
    }
}
