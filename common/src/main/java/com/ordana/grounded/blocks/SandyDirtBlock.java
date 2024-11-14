package com.ordana.grounded.blocks;

import com.ordana.grounded.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SandyDirtBlock extends FallingBlock {
    public SandyDirtBlock(Properties properties) {
        super(properties);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();
        var coarse = state.getBlock() == ModBlocks.COARSE_SANDY_DIRT.get();
        var tool = 0;
        if (item instanceof ShovelItem) tool = 1;
        if (item instanceof HoeItem) tool = 2;
        if (tool > 0) {
            level.playSound(player, pos, tool == 1 ? SoundEvents.SHOVEL_FLATTEN : SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0f, 1.0f);
            stack.hurtAndBreak(1, player, (l) -> l.broadcastBreakEvent(hand));
            if (player instanceof ServerPlayer) {
                level.setBlockAndUpdate(pos, tool == 1 ? ModBlocks.SANDY_DIRT_PATH.get().defaultBlockState() : coarse ? ModBlocks.SANDY_DIRT.get().defaultBlockState() : ModBlocks.SANDY_FARMLAND.get().defaultBlockState());
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    public int getDustColor(BlockState state, BlockGetter level, BlockPos pos) {
        return 0xd3b893;
    }
}
