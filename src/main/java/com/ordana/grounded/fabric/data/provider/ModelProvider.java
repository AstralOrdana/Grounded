//? fabric {
package com.ordana.grounded.fabric.data.provider;

import com.ordana.grounded.reg.ModBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.color.item.GrassColorSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        ModBlocks.BLOCKS.forEach(block -> {
            var model = block.identifier().withPrefix("block/");
            if (block.identifier().getPath().contains("grassy")) {
                blockModelGenerators.registerSimpleTintedItemModel(block.get(), model, new GrassColorSource());
            } else {
                blockModelGenerators.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(model));
            }
        });
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {

    }
}
//?}
