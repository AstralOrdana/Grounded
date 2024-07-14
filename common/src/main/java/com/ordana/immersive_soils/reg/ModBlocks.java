package com.ordana.immersive_soils.reg;

import com.ordana.immersive_soils.ImmersiveSoils;
import com.ordana.immersive_soils.blocks.*;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

@SuppressWarnings("unused")
public class ModBlocks {

    public static void init() {
    }

    public static <T extends Block> Supplier<T> regBlock(String name, Supplier<T> block) {
        return RegHelper.registerBlock(ImmersiveSoils.res(name), block);
    }
    public static Supplier<BlockItem> regBlockItem(String name, Supplier<? extends Block> blockSup, Item.Properties properties) {
        return RegHelper.registerItem(ImmersiveSoils.res(name), () -> new BlockItem(blockSup.get(), properties));
    }

    public static <T extends Block> Supplier<T> regWithItem(String name, Supplier<T> blockFactory) {
        Supplier<T> block = regBlock(name, blockFactory);
        regBlockItem(name, block, new Item.Properties());
        return block;
    }

    private static ToIntFunction<BlockState> moltenLightLevel(int litLevel) {
        return (state) -> state.getValue(NulchBlock.MOLTEN) ? litLevel : 0;
    }


    public static final Supplier<Block> HANGING_ROOTS_WALL = regBlock("hanging_roots_wall", () ->
            new WallRootsBlock(Properties.copy(Blocks.HANGING_ROOTS)));


    public static final Supplier<Block> MULCH_BLOCK = regWithItem("mulch_block", () ->
            new MulchBlock(Properties.copy(Blocks.DIRT).strength(1f, 1f)
                    .sound(SoundType.ROOTED_DIRT).randomTicks()));
    public static final Supplier<Block> NULCH_BLOCK = regWithItem("nulch_block", () ->
            new NulchBlock(Properties.copy(Blocks.DIRT).strength(1f, 1f)
                    .sound(SoundType.NETHER_WART).lightLevel(moltenLightLevel(10)).randomTicks()));

    public static final Supplier<Block> SILT = regWithItem("silt", () ->
            new SiltBlock(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.MUD)));
    public static final Supplier<Block> GRASSY_SILT = regWithItem("grassy_silt", () ->
            new SiltBlockGrassy(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.MUD)));
    public static final Supplier<Block> SILTY_FARMLAND = regWithItem("silty_farmland", () ->
            new SiltyFarmlandBlock(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.MUD)));
    /*
    public static final Supplier<Block> ROOTED_SILT = regWithItem("rooted_silt", () ->
        new SiltBlock(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.MUD)));
    public static final Supplier<Block> ROOTED_GRASSY_SILT = regWithItem("rooted_grassy_silt", () ->
        new SiltBlock(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.MUD)));*/


    public static final Supplier<Block> SANDY_DIRT = regWithItem("sandy_dirt", () ->
            new SandyDirtBlock(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.SAND)));
    public static final Supplier<Block> GRASSY_SANDY_DIRT = regWithItem("grassy_sandy_dirt", () ->
            new SandyDirtBlockGrassy(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.SAND)));
    public static final Supplier<Block> SANDY_FARMLAND = regWithItem("sandy_farmland", () ->
            new SandyFarmlandBlock(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.SAND)));

    /*
    public static final Supplier<Block> ROOTED_SANDY_DIRT = regWithItem("rooted_sandy_dirt", () ->
        new SandyDirtBlock(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.SAND)));
    public static final Supplier<Block> ROOTED_GRASSY_SANDY_DIRT = regWithItem("rooted_grassy_sandy_dirt", () ->
        new SandyDirtBlock(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.SAND)));*/

    public static final Supplier<Block> EARTHEN_CLAY = regWithItem("earthen_clay", () ->
            new EarthenClayBlock(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.BASALT)));
    public static final Supplier<Block> GRASSY_EARTHEN_CLAY = regWithItem("grassy_earthen_clay", () ->
            new EarthenClayBlockGrassy(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.BASALT)));
    public static final Supplier<Block> EARTHEN_CLAY_FARMLAND = regWithItem("earthen_clay_farmland", () ->
            new EarthenClayFarmlandBlock(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.BASALT)));

    public static final Supplier<Block> LOAM = regWithItem("loam", () ->
            new LoamBlock(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.GRAVEL)));
    public static final Supplier<Block> LOAMY_FARMLAND = regWithItem("loamy_farmland", () ->
            new LoamyFarmlandBlock(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.GRAVEL)));

    /*
    public static final Supplier<Block> ROOTED_LOAM = regWithItem("rooted_loam", () ->
        new LoamBlock(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.GRAVEL)));
    public static final Supplier<Block> ROOTED_GRASSY_LOAM = regWithItem("rooted_grassy_loam", () ->
        new LoamBlock(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.GRAVEL)));*/

    public static final Supplier<Block> PERMAFROST = regWithItem("permafrost", () ->
            new PermafrostBlock(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.CALCITE)));
    public static final Supplier<Block> GRASSY_PERMAFROST = regWithItem("grassy_permafrost", () ->
            new PermafrostBlockGrassy(Properties.copy(Blocks.DIRT).strength(0.5f).sound(SoundType.CALCITE)));


    public static final Supplier<Block> ROOTED_GRASS_BLOCK = regWithItem("rooted_grass_block", () ->
            new RootedGrassBlock(Properties.copy(Blocks.GRASS_BLOCK).randomTicks().strength(0.5F)
                    .sound(SoundType.ROOTED_DIRT)));

}