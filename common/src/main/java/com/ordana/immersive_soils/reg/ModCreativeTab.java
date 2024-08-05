package com.ordana.immersive_soils.reg;

import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ModCreativeTab {

    public static void init(){
        RegHelper.addItemsToTabsRegistration(ModCreativeTab::addItems);
    }

    public static void addItems(RegHelper.ItemToTabEvent e) {
        after(e, Items.PODZOL, CreativeModeTabs.NATURAL_BLOCKS,
                ModBlocks.LOAM
        );

        after(e, Items.ROOTED_DIRT, CreativeModeTabs.NATURAL_BLOCKS,
                ModBlocks.ROOTED_GRASS_BLOCK,
                ModBlocks.EARTHEN_CLAY, ModBlocks.SANDY_DIRT, ModBlocks.SILT, ModBlocks.PERMAFROST,
                ModBlocks.GRASSY_EARTHEN_CLAY, ModBlocks.GRASSY_SANDY_DIRT, ModBlocks.GRASSY_SILT, ModBlocks.GRASSY_PERMAFROST
        );

        after(e, Items.FARMLAND, CreativeModeTabs.NATURAL_BLOCKS,
                ModBlocks.LOAMY_FARMLAND,
                ModBlocks.EARTHEN_CLAY_FARMLAND, ModBlocks.SANDY_FARMLAND, ModBlocks.SILTY_FARMLAND,
                ModBlocks.MULCH_BLOCK, ModBlocks.NULCH_BLOCK
        );

        after(e, Items.DIRT_PATH, CreativeModeTabs.NATURAL_BLOCKS,
                ModBlocks.EARTHEN_CLAY_PATH, ModBlocks.SANDY_DIRT_PATH, ModBlocks.SILT_PATH, ModBlocks.PERMAFROST_PATH
        );

        after(e, Items.COARSE_DIRT, CreativeModeTabs.NATURAL_BLOCKS,
                ModBlocks.COARSE_EARTHEN_CLAY, ModBlocks.COARSE_SANDY_DIRT, ModBlocks.COARSE_SILT, ModBlocks.COARSE_PERMAFROST
        );

    }

    private static void after(RegHelper.ItemToTabEvent event, Item target,
                              ResourceKey<CreativeModeTab> tab, Supplier<?>... items) {
        after(event, i -> i.is(target), tab, items);
    }

    private static void after(RegHelper.ItemToTabEvent event, Predicate<ItemStack> targetPred,
                              ResourceKey<CreativeModeTab> tab, Supplier<?>... items) {
        ItemLike[] entries = Arrays.stream(items).map((s -> (ItemLike) (s.get()))).toArray(ItemLike[]::new);
        event.addAfter(tab, targetPred, entries);
    }
}
