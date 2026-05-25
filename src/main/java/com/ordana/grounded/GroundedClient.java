package com.ordana.grounded;

import com.ordana.grounded.reg.ModBlocks;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class GroundedClient {
    
    public static void init() {

    }

    public static void registerBlockColors(PlatformSpecific.BlockColorEvent event) {

        event.register(List.of(BlockTintSources.grassBlock(), c->-1),
                ModBlocks.GRASSY_PERMAFROST.get(),
                ModBlocks.GRASSY_SILT.get(),
                ModBlocks.GRASSY_EARTHEN_CLAY.get(),
                ModBlocks.GRASSY_SANDY_DIRT.get());
    }

        /*
    private static void registerItemColors(ClientHelper.ItemColorEvent event) {


        event.register((itemStack, i) -> event.getColor(Items.GRASS_BLOCK.getDefaultInstance(), i),
                ModBlocks.GRASSY_SILT.get(),
                ModBlocks.GRASSY_PERMAFROST.get(),
                ModBlocks.GRASSY_SANDY_DIRT.get(),
                ModBlocks.GRASSY_EARTHEN_CLAY.get());


    }

         */

}