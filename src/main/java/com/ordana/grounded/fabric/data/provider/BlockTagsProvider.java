//? fabric {
package com.ordana.grounded.fabric.data.provider;

import com.ordana.grounded.reg.ModBlocks;
import com.ordana.grounded.reg.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("all")
public class BlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
    public BlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        valueLookupBuilder(ModTags.FARMLANDS).add(Blocks.FARMLAND);
        builder(ModTags.FARMLANDS).add(ModBlocks.LOAMY_FARMLAND.id(), ModBlocks.SILTY_FARMLAND.id(), ModBlocks.SANDY_FARMLAND.id(), ModBlocks.EARTHEN_CLAY_FARMLAND.id());

        valueLookupBuilder(ModTags.CLAY_SOIL_CROP).add(Blocks.WHEAT, Blocks.BAMBOO_SAPLING, Blocks.BAMBOO);
        valueLookupBuilder(ModTags.LOAM_SOIL_CROP).add(Blocks.MELON_STEM, Blocks.PUMPKIN_STEM, Blocks.SWEET_BERRY_BUSH);
        builder(ModTags.LOAM_SOIL_CROP).addOptional(of("farmersdelight:budding_tomatoes")).addOptional(of("farmersdelight:tomatoes"));
        valueLookupBuilder(ModTags.SAND_SOIL_CROP).add(Blocks.CARROTS, Blocks.BEETROOTS);
        builder(ModTags.SAND_SOIL_CROP).addOptional(of("farmersdelight:cabbages"));
        valueLookupBuilder(ModTags.SILT_SOIL_CROP).add(Blocks.POTATOES, Blocks.WHEAT);
        builder(ModTags.SILT_SOIL_CROP).addOptional(of("farmersdelight:cabbages")).addOptional(of("farmersdelight:flax")).addOptional(of("farmersdelight:onions"));
		valueLookupBuilder(ModTags.OOZE_CROPS).add(Blocks.KELP, Blocks.KELP_PLANT);

        ResourceKey[] DIRT_BLOCKS = {ModBlocks.MULCH_BLOCK.id(), ModBlocks.LOAM.id(), ModBlocks.SILT.id(), ModBlocks.EARTHEN_CLAY.id(), ModBlocks.SANDY_DIRT.id(), ModBlocks.PERMAFROST.id()};
        ResourceKey[] GRASS_BLOCKS = {ModBlocks.GRASSY_SILT.id(), ModBlocks.GRASSY_EARTHEN_CLAY.id(), ModBlocks.GRASSY_SANDY_DIRT.id(), ModBlocks.GRASSY_PERMAFROST.id()};

        builder(BlockTags.SUPPORTS_CROPS).forceAddTag(ModTags.FARMLANDS);
        builder(BlockTags.GROWS_CROPS).forceAddTag(ModTags.FARMLANDS);
        builder(BlockTags.SUPPORTS_STEM_CROPS).forceAddTag(ModTags.FARMLANDS);
        builder(BlockTags.SUPPORTS_STEM_FRUIT).forceAddTag(ModTags.FARMLANDS);
        builder(BlockTags.SUPPORTS_VEGETATION).forceAddTag(ModTags.FARMLANDS);
        builder(BlockTags.ANIMALS_SPAWNABLE_ON).add(DIRT_BLOCKS).add(GRASS_BLOCKS);
        builder(BlockTags.GOATS_SPAWNABLE_ON).add(DIRT_BLOCKS).add(GRASS_BLOCKS);
        builder(BlockTags.FOXES_SPAWNABLE_ON).add(DIRT_BLOCKS).add(GRASS_BLOCKS);
        builder(BlockTags.RABBITS_SPAWNABLE_ON).add(DIRT_BLOCKS).add(GRASS_BLOCKS);
        builder(BlockTags.WOLVES_SPAWNABLE_ON).add(DIRT_BLOCKS).add(GRASS_BLOCKS);
        builder(BlockTags.SUPPORTS_BAMBOO).add(ModBlocks.EARTHEN_CLAY_FARMLAND.id(), ModBlocks.LOAMY_FARMLAND.id(), ModBlocks.SANDY_FARMLAND.id(), ModBlocks.SILTY_FARMLAND.id());
        builder(BlockTags.DIRT).add(DIRT_BLOCKS);
        builder(ModTags.GRASSY_BLOCKS).add(GRASS_BLOCKS);
        builder(BlockTags.GRASS_BLOCKS).addTag(ModTags.GRASSY_BLOCKS);
        builder(BlockTags.OVERRIDES_MUSHROOM_LIGHT_REQUIREMENT).add(ModBlocks.MULCH_BLOCK.id(), ModBlocks.NULCH_BLOCK.id());
    }

    private ResourceKey<Block> of(String s) {
        return ResourceKey.create(Registries.BLOCK, Identifier.parse(s));
    }
}
//?}