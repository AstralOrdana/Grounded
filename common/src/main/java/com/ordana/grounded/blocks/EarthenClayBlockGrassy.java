package com.ordana.grounded.blocks;

import com.ordana.grounded.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class EarthenClayBlockGrassy extends BaseSoilBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public EarthenClayBlockGrassy(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(SNOWY, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(WATERLOGGED).add(SNOWY);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        boolean bl = fluidState.getType() == Fluids.WATER;
        return super.getStateForPlacement(context).setValue(WATERLOGGED, bl);
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
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
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        if (!canBeGrass(blockState, serverLevel, pos)) {
            serverLevel.setBlockAndUpdate(pos, ModBlocks.EARTHEN_CLAY.get().defaultBlockState());
        }
        if (!blockState.getValue(WATERLOGGED) && serverLevel.isRainingAt(pos.above()) || isNearWater(serverLevel, pos)) {
            serverLevel.setBlock(pos, blockState.setValue(WATERLOGGED, true), 2);
        } else if (blockState.getValue(WATERLOGGED) && serverLevel.dimensionType().ultraWarm()) {
            serverLevel.setBlock(pos, blockState.setValue(WATERLOGGED, false), 2);
        }
        super.randomTick(blockState, serverLevel, pos, random);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();
        var bl = state.getValue(WATERLOGGED);
        var tool = 0;
        if (item instanceof ShovelItem) tool = 1;
        if (item instanceof HoeItem) tool = 2;
        if (tool > 0) {
            level.playSound(player, pos, tool == 1 ? SoundEvents.SHOVEL_FLATTEN : SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0f, 1.0f);
            stack.hurtAndBreak(1, player, (l) -> l.broadcastBreakEvent(hand));
            if (player instanceof ServerPlayer) {
                level.setBlockAndUpdate(pos, tool == 1 ? ModBlocks.EARTHEN_CLAY_PATH.get().defaultBlockState().setValue(WATERLOGGED, bl) : ModBlocks.EARTHEN_CLAY_FARMLAND.get().defaultBlockState().setValue(BlockStateProperties.MOISTURE, bl ? 7 : 0));
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }
}
