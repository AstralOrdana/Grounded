package com.ordana.grounded.reg;

import com.ordana.grounded.Grounded;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class ModTags {
    private ModTags() {
    }

    private static TagKey<Block> registerBlockTag(String id) {
        return TagKey.create(Registries.BLOCK, Grounded.res(id));
    }
    public static final TagKey<Block> CLAY_SOIL_CROP = registerBlockTag("soil_clay_crops");
    public static final TagKey<Block> SILT_SOIL_CROP = registerBlockTag("soil_silt_crops");
    public static final TagKey<Block> SAND_SOIL_CROP = registerBlockTag("soil_sand_crops");
    public static final TagKey<Block> LOAM_SOIL_CROP = registerBlockTag("soil_loam_crops");
    public static final TagKey<Block> GRASSY_BLOCKS = registerBlockTag("grassy_blocks");


    private static TagKey<Biome> registerBiomeTag(String id) {
        return TagKey.create(Registries.BIOME, Grounded.res(id));
    }
    public static final TagKey<Biome> HAS_LOAM = registerBiomeTag("has_loam");
    public static final TagKey<Biome> HAS_SILT = registerBiomeTag("has_silt");
    public static final TagKey<Biome> HAS_SANDY_DIRT = registerBiomeTag("has_sandy_dirt");
    public static final TagKey<Biome> HAS_EARTHEN_CLAY = registerBiomeTag("has_earthen_clay");
    public static final TagKey<Biome> HAS_PERMAFROST = registerBiomeTag("has_permafrost");
    public static final TagKey<Biome> HAS_LAKEBED = registerBiomeTag("has_lakebed");
}
