package com.ordana.grounded.blocks;

import com.mojang.serialization.MapCodec;
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
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class PermafrostBlock extends FallingBlock {

    public PermafrostBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return simpleCodec(PermafrostBlock::new);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinBuildHeight()) {
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level, pos, state);
            this.falling(fallingBlockEntity);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (canMelt(level, pos)) level.scheduleTick(pos, this, this.getDelayAfterPlace());
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
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

    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    public boolean canMelt(Level level, BlockPos pos) {
        boolean melt = false;
        for (Direction dir : Direction.values()) {
            var brightness = level.getBrightness(LightLayer.BLOCK, pos.relative(dir));
            boolean water = level.getFluidState(pos.relative(dir)).is(FluidTags.WATER);
            if (brightness > 11 || water) melt = true;
        }
        return melt;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return state;
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock) {
        if (level.random.nextBoolean()) level.destroyBlock(pos, false);
    }


    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();
        var coarse = state.getBlock() == ModBlocks.COARSE_PERMAFROST.get();
        var tool = 0;
        if (item instanceof ShovelItem) tool = 1;
        if (item instanceof HoeItem) tool = 2;
        if (!coarse && tool == 2) return ItemInteractionResult.FAIL;
        if (tool > 0) {
            level.playSound(player, pos, tool == 1 ? SoundEvents.SHOVEL_FLATTEN : SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0f, 1.0f);
            stack.hurtAndBreak(1, player, Player.getSlotForHand(hand));
            if (player instanceof ServerPlayer) {
                level.setBlockAndUpdate(pos, tool == 1 ? ModBlocks.PERMAFROST_PATH.get().defaultBlockState() : ModBlocks.PERMAFROST.get().defaultBlockState());
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

}
