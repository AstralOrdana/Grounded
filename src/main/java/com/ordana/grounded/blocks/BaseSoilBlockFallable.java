package com.ordana.grounded.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.lighting.LightEngine;

import java.util.List;
import java.util.Optional;

public class BaseSoilBlockFallable extends FallingBlock implements BonemealableBlock {

    public static final BooleanProperty SNOWY;

    public BaseSoilBlockFallable(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SNOWY, false));
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return simpleCodec(BaseSoilBlockFallable::new);
    }

    @Override
    public int getDustColor(BlockState blockState, BlockGetter level, BlockPos pos) {
        return blockState.getBlock().defaultMapColor().col;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    public static boolean canBeGrass(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        if (aboveState.is(Blocks.SNOW) && aboveState.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (aboveState.getFluidState().isFull()) {
            return false;
        } else {
            int lightBlockInto = LightEngine.getLightBlockInto(state, aboveState, Direction.UP, aboveState.getLightDampening());
            return lightBlockInto < 15;
        }
    }


    public static boolean canPropagate(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.above();
        return canBeGrass(state, level, pos) && !level.getFluidState(blockpos).is(FluidTags.WATER);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getMaxLocalRawBrightness(pos.above()) >= 9 && canBeGrass(state, level, pos)) {

            for(int i = 0; i < 4; ++i) {
                BlockPos blockPos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                BlockState s = level.getBlockState(blockPos);
                var soil = WeatheringHelper.getGrassySoil(s);
                if (s.is(Blocks.MYCELIUM) && canPropagate(state, level, blockPos)) {
                    level.setBlockAndUpdate(blockPos, this.defaultBlockState().setValue(BlockStateProperties.SNOWY, level.getBlockState(blockPos.above()).is(Blocks.SNOW)));
                } else if (soil.isPresent() && canPropagate(state, level, blockPos)) {
                    if (soil.get().hasProperty(BlockStateProperties.SNOWY)) level.setBlockAndUpdate(blockPos, soil.get().setValue(BlockStateProperties.SNOWY, level.getBlockState(blockPos.above()).is(Blocks.SNOW)));
                    else level.setBlockAndUpdate(blockPos, soil.get());
                }
            }
        }
    }


    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState blockState) {
        return level.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos above = pos.above();
        BlockState grass = Blocks.SHORT_GRASS.defaultBlockState();
        Optional<Holder.Reference<PlacedFeature>> grassFeature = level.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE).get(VegetationPlacements.GRASS_BONEMEAL);

        label47:
        for(int j = 0; j < 128; ++j) {
            BlockPos testPos = above;

            for(int i = 0; i < j / 16; ++i) {
                testPos = testPos.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                if (!level.getBlockState(testPos.below()).is(this) || level.getBlockState(testPos).isCollisionShapeFullBlock(level, testPos)) {
                    continue label47;
                }
            }

            BlockState testState = level.getBlockState(testPos);
            if (testState.is(grass.getBlock()) && random.nextInt(10) == 0) {
                BonemealableBlock bonemealableBlock = (BonemealableBlock)grass.getBlock();
                if (bonemealableBlock.isValidBonemealTarget(level, testPos, testState)) {
                    bonemealableBlock.performBonemeal(level, random, testPos, testState);
                }
            }

            if (testState.isAir() && !level.isOutsideBuildHeight(testPos)) {
                if (random.nextInt(8) == 0) {
                    List<ConfiguredFeature<?, ?>> features = ((Biome)level.getBiome(testPos).value()).getGenerationSettings().getBoneMealFeatures();
                    if (!features.isEmpty()) {
                        ConfiguredFeature<?, ?> placementFeature = (ConfiguredFeature) Util.getRandom(features, random);
                        placementFeature.place(level, level.getChunkSource().getGenerator(), random, testPos);
                    }
                } else if (grassFeature.isPresent()) {
                    ((PlacedFeature)((Holder.Reference)grassFeature.get()).value()).place(level, level.getChunkSource().getGenerator(), random, testPos);
                }
            }
        }

    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        return directionToNeighbour == Direction.UP ? state.setValue(SNOWY, isSnowySetting(neighbourState)) :  super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos().above());
        return this.defaultBlockState().setValue(SNOWY, isSnowySetting(blockState));
    }

    static boolean isSnowySetting(BlockState state) {
        return state.is(BlockTags.SNOW);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SNOWY);
    }

    static {
        SNOWY = BlockStateProperties.SNOWY;
    }
}
