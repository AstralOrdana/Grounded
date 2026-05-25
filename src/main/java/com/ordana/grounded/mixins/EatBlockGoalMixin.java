package com.ordana.grounded.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.ordana.grounded.blocks.WeatheringHelper;
import com.ordana.grounded.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EatBlockGoal.class)
public abstract class EatBlockGoalMixin extends Goal {

    @Shadow
    @Final
    private Mob mob;

    @Shadow
    @Final
    private Level level;

    @WrapOperation(method = "canUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    protected boolean setBlock(BlockState instance, Object o, Operation<Boolean> original) {
        return original.call(instance, o) || instance.is(ModTags.GRASSY_BLOCKS);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;blockPosition()Lnet/minecraft/core/BlockPos;"), cancellable = true)
    protected void setBlock(CallbackInfo ci) {
        BlockPos below = mob.blockPosition().below();
        BlockState grassBlockState = this.level.getBlockState(below);
        if (grassBlockState.is(ModTags.GRASSY_BLOCKS)) {
            var newState = WeatheringHelper.getSoilFromGrass(grassBlockState);
            if (getServerLevel(this.level).getGameRules().get(GameRules.MOB_GRIEFING)) {
                this.level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, below, Block.getId(grassBlockState));
                this.level.setBlock(below, newState.get(), 2);
            }

            this.mob.ate();
            ci.cancel();
        }
    }
}
