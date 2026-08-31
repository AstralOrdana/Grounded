package com.ordana.grounded.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.ordana.grounded.blocks.DetritusBlock;
import com.ordana.grounded.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {

    @Shadow private BlockState blockState;

	public FallingBlockEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canBeReplaced(Lnet/minecraft/world/item/context/BlockPlaceContext;)Z"))
    private boolean modifyCanReplace(boolean original, @Local(name = "currentState") BlockState currentState) {
        return original || currentState.is(ModBlocks.DETRITUS.get());
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;hasProperty(Lnet/minecraft/world/level/block/state/properties/Property;)Z"), cancellable = true)
    private void doDetritusMerge(CallbackInfo ci, @Local(name = "pos") BlockPos pos, @Local(name = "currentState") BlockState currentState) {
        if (!currentState.is(ModBlocks.DETRITUS.get()) || !this.blockState.is(ModBlocks.DETRITUS.get())) {
			return;
		}

		int layersToMergeWith = currentState.getValue(DetritusBlock.LAYERS);

		if (layersToMergeWith < DetritusBlock.MAX_LAYERS) {
			int totalLayers = layersToMergeWith + this.blockState.getValue(DetritusBlock.LAYERS);

			if (this.level() instanceof ServerLevel serverLevel)
				DetritusBlock.merge(serverLevel, pos, currentState, (FallingBlockEntity) (Object) this, totalLayers);

			this.discard();
			ci.cancel();
		}
	}

}
