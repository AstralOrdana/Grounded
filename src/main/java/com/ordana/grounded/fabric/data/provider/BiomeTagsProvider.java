//? fabric {
package com.ordana.grounded.fabric.data.provider;

import com.ordana.grounded.reg.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.tags.BiomeTags.*;

@SuppressWarnings("all")
public class BiomeTagsProvider extends FabricTagsProvider<Biome> {
    public BiomeTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.BIOME, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(ModTags.HAS_EARTHEN_CLAY).forceAddTag(IS_BADLANDS).forceAddTag(IS_SAVANNA);
        builder(ModTags.HAS_LAKEBED).forceAddTag(IS_BADLANDS).add(Biomes.DESERT);
        builder(ModTags.HAS_LOAM).add(Biomes.JUNGLE, Biomes.DARK_FOREST, Biomes.LUSH_CAVES);
        builder(ModTags.HAS_PERMAFROST).add(Biomes.SNOWY_SLOPES, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA);
        builder(ModTags.HAS_SANDY_DIRT).forceAddTag(IS_BADLANDS).forceAddTag(IS_SAVANNA).add(Biomes.DESERT);
        builder(ModTags.HAS_SILT).forceAddTag(IS_RIVER).add(Biomes.SWAMP).add(Biomes.MANGROVE_SWAMP);
    }

    private ResourceKey<Biome> of(String s) {
        return ResourceKey.create(Registries.BIOME, Identifier.parse(s));
    }
}
//?}