package com.ordana.grounded.reg;

import com.ordana.grounded.Grounded;
import com.ordana.grounded.blocks.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.ToIntFunction;

@SuppressWarnings("unused")
public class ModBlocks {

    public static ArrayList<RegSupplier<Block>> BLOCKS = new ArrayList<>();

    public static void init() {
    }

    private static RegSupplier<Block> regWithItem(String name, Function<Properties, Block> blockFactory, BlockBehaviour.Properties settings) { 
        return regBlock(name, blockFactory, settings, true);
    }

    private static RegSupplier<Block> regBlock(String name, Function<Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
        // Create a registry key for the block
        ResourceKey<Block> blockKey = keyOfBlock(name);
        // Create the block instance
        Block block = blockFactory.apply(settings.setId(blockKey));

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            // Items need to be registered with a different type of registry key, but the ID
            // can be the same.
            ResourceKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        var r = new RegSupplier<>(blockKey, Registry.register(BuiltInRegistries.BLOCK, blockKey, block));
        BLOCKS.add(r);
        return r;
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, Grounded.res(name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, Grounded.res(name));
    }


    private static ToIntFunction<BlockState> moltenLightLevel(int litLevel) {
        return (state) -> state.getValue(NulchBlock.MOLTEN) ? litLevel : 0;
    }


    public static final RegSupplier<Block> MULCH_BLOCK = regWithItem("mulch_block", (p) ->
            new MulchBlock(p), Properties.ofFullCopy(Blocks.DIRT).strength(1f, 1f)
                    .sound(SoundType.ROOTED_DIRT).randomTicks());
    public static final RegSupplier<Block> NULCH_BLOCK = regWithItem("nulch_block", (p) ->
            new NulchBlock(p), Properties.ofFullCopy(Blocks.DIRT).strength(1f, 1f)
                    .sound(SoundType.NETHER_WART).lightLevel(moltenLightLevel(10)).randomTicks());


    public static final RegSupplier<Block> SILT = regWithItem("silt", (p) ->
            new SiltBlock(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.MUD).mapColor(MapColor.TERRACOTTA_BROWN));
    public static final RegSupplier<Block> GRASSY_SILT = regWithItem("grassy_silt", (p) ->
            new SiltBlockGrassy(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.MUD).mapColor(MapColor.GRASS));
    public static final RegSupplier<Block> SILT_PATH = regWithItem("silt_path", (p) ->
            new SiltPathBlock(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.MUD).mapColor(MapColor.TERRACOTTA_BROWN));
    public static final RegSupplier<Block> COARSE_SILT = regWithItem("coarse_silt", (p) ->
            new SiltBlock(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.MUD).mapColor(MapColor.TERRACOTTA_BROWN));
    public static final RegSupplier<Block> SILTY_FARMLAND = regWithItem("silty_farmland", (p) ->
            new SiltyFarmlandBlock(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.6f).sound(SoundType.MUD).mapColor(MapColor.TERRACOTTA_BROWN));

    public static final RegSupplier<Block> SANDY_DIRT = regWithItem("sandy_dirt", (p) ->
            new SandyDirtBlock(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.SAND).mapColor(MapColor.SAND));
    public static final RegSupplier<Block> GRASSY_SANDY_DIRT = regWithItem("grassy_sandy_dirt", (p) ->
            new SandyDirtBlockGrassy(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.SAND).mapColor(MapColor.GRASS));
    public static final RegSupplier<Block> SANDY_DIRT_PATH = regWithItem("sandy_dirt_path", (p) ->
            new SandyDirtPathBlock(p), Properties.ofFullCopy(Blocks.DIRT_PATH).strength(0.5f).sound(SoundType.SAND).mapColor(MapColor.TERRACOTTA_BROWN));
    public static final RegSupplier<Block> COARSE_SANDY_DIRT = regWithItem("coarse_sandy_dirt", (p) ->
            new SandyDirtBlock(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.SAND).mapColor(MapColor.SAND));
    public static final RegSupplier<Block> SANDY_FARMLAND = regWithItem("sandy_farmland", (p) ->
            new SandyFarmlandBlock(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.6f).sound(SoundType.SAND).mapColor(MapColor.TERRACOTTA_BROWN));

    public static final RegSupplier<Block> EARTHEN_CLAY = regWithItem("earthen_clay", (p) ->
            new EarthenClayBlock(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.BASALT).mapColor(MapColor.TERRACOTTA_ORANGE));
    public static final RegSupplier<Block> GRASSY_EARTHEN_CLAY = regWithItem("grassy_earthen_clay", (p) ->
            new EarthenClayBlockGrassy(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.BASALT).mapColor(MapColor.GRASS));
    public static final RegSupplier<Block> EARTHEN_CLAY_PATH = regWithItem("earthen_clay_path", (p) ->
            new EarthenClayPathBlock(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.BASALT).mapColor(MapColor.TERRACOTTA_ORANGE));
    public static final RegSupplier<Block> COARSE_EARTHEN_CLAY = regWithItem("coarse_earthen_clay", (p) ->
            new EarthenClayBlock(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.BASALT).mapColor(MapColor.TERRACOTTA_ORANGE));
    public static final RegSupplier<Block> EARTHEN_CLAY_FARMLAND = regWithItem("earthen_clay_farmland", (p) ->
            new EarthenClayFarmlandBlock(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.6f).sound(SoundType.BASALT).mapColor(MapColor.TERRACOTTA_ORANGE));

    public static final RegSupplier<Block> PERMAFROST = regWithItem("permafrost", (p) ->
            new PermafrostBlock(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.8f).sound(SoundType.CALCITE).mapColor(MapColor.SNOW));
    public static final RegSupplier<Block> GRASSY_PERMAFROST = regWithItem("grassy_permafrost", (p) ->
            new PermafrostBlockGrassy(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.8f).sound(SoundType.CALCITE).mapColor(MapColor.GRASS));
    public static final RegSupplier<Block> COARSE_PERMAFROST = regWithItem("coarse_permafrost", (p) ->
            new PermafrostBlock(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.8f).sound(SoundType.CALCITE).mapColor(MapColor.SNOW));
    public static final RegSupplier<Block> PERMAFROST_PATH = regWithItem("permafrost_path", (p) ->
            new PermafrostPathBlock(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.8f).sound(SoundType.CALCITE).mapColor(MapColor.SNOW));

    public static final RegSupplier<Block> LOAM = regWithItem("loam", (p) ->
            new LoamBlock(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.5f).sound(SoundType.GRAVEL).mapColor(MapColor.TERRACOTTA_BROWN));
    public static final RegSupplier<Block> LOAMY_FARMLAND = regWithItem("loamy_farmland", (p) ->
            new LoamyFarmlandBlock(p), Properties.ofFullCopy(Blocks.DIRT).strength(0.6f).sound(SoundType.GRAVEL).mapColor(MapColor.TERRACOTTA_BROWN));

	public static final RegSupplier<Block> OOZE = regWithItem("ooze", (p) ->
			new OozeBlock(p), Properties.ofFullCopy(Blocks.DIRT).mapColor(MapColor.SAND).sound(SoundType.MUD).speedFactor(0.8F).randomTicks());
	public static final RegSupplier<Block> DETRITUS = regWithItem(
		"detritus",
		DetritusBlock::new,
		Properties.ofFullCopy(Blocks.DIRT)
			.mapColor(MapColor.SNOW)
			.sound(SoundType.MUD)
			.speedFactor(0.8F)
			.randomTicks()
			.replaceable()
			.isViewBlocking((statex, level, pos) -> statex.getValue(DetritusBlock.LAYERS) >= DetritusBlock.MAX_LAYERS)
	);

}