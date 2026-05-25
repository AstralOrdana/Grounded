package com.ordana.grounded;

//? neoforge
//import com.ordana.grounded.neoforge.PlatformSpecificImpl;
//? fabric
import com.ordana.grounded.fabric.PlatformSpecificImpl;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.function.Predicate;

public interface PlatformSpecific {
    PlatformSpecific INSTANCE = new PlatformSpecificImpl();

    default void addFeatureToBiome(GenerationStep.Decoration step, TagKey<Biome> tagKey, ResourceKey<PlacedFeature> feature) {

    }

    interface ItemToTabEvent {
        void addAfter(ResourceKey<CreativeModeTab> tab, ItemStack target, ItemLike[] entries);
    }

    @FunctionalInterface
    interface BlockColorEvent {
        void register(List<BlockTintSource> var1, Block... var2);
    }
}
