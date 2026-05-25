package com.ordana.grounded.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.ordana.grounded.blocks.WeatheringHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(SpreadingSnowyBlock.class)
public class GrassBlockMixin {


    @WrapOperation(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z",
    ordinal = 0))
    protected boolean mayPlaceOn(BlockState instance, Object block, Operation<Boolean> original, @Share("newSoil")LocalRef<BlockState> newSoil) {
        boolean or = original.call(instance, block);
        if(!or){
            Optional<BlockState> soil = WeatheringHelper.getGrassySoil(instance);
            if (soil.isPresent()) {
                newSoil.set(soil.get());
                or = true;
            }
        }else newSoil.set(null);
        return or;
    }

    @WrapOperation(method = "randomTick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z",
    ordinal = 1))
    protected boolean mayPlaceOn(ServerLevel instance, BlockPos pos, BlockState state, Operation<Boolean> operation,
                                 @Share("newSoil")LocalRef<BlockState> newSoil) {
        BlockState soil = newSoil.get();
        if(soil != null){
            if(state.hasProperty(SnowyBlock.SNOWY) && state.getValue(SnowyBlock.SNOWY)) {
                soil = soil.setValue(SnowyBlock.SNOWY, true);
            }
            return operation.call(instance, pos, soil);
        }
        return operation.call(instance, pos, state);
    }
}
