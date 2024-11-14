package com.ordana.grounded;

import com.ordana.grounded.reg.ModBlocks;
import net.mehvahdjukaar.moonlight.api.misc.EventCalled;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class GroundedClient {
    
    public static void init() {
        ClientHelper.addClientSetup(GroundedClient::setup);
        ClientHelper.addBlockColorsRegistration(GroundedClient::registerBlockColors);
        ClientHelper.addItemColorsRegistration(GroundedClient::registerItemColors);
    }

    public static void setup() {
        ClientHelper.registerRenderType(ModBlocks.GRASSY_PERMAFROST.get(), RenderType.cutoutMipped());
        ClientHelper.registerRenderType(ModBlocks.GRASSY_SILT.get(), RenderType.cutoutMipped());
        ClientHelper.registerRenderType(ModBlocks.GRASSY_EARTHEN_CLAY.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.GRASSY_SANDY_DIRT.get(), RenderType.cutoutMipped());
        ClientHelper.registerRenderType(ModBlocks.ROOTED_GRASS_BLOCK.get(), RenderType.cutoutMipped());
        ClientHelper.registerRenderType(ModBlocks.HANGING_ROOTS_WALL.get(), RenderType.cutoutMipped());
    }

    @EventCalled
    private static void registerBlockColors(ClientHelper.BlockColorEvent event) {

        event.register((blockState, level, blockPos, i) -> {
                    if (i == 0) return -1;
                    return event.getColor(Blocks.GRASS_BLOCK.defaultBlockState(), level, blockPos, i);
                },
                ModBlocks.GRASSY_PERMAFROST.get(),
                ModBlocks.GRASSY_SILT.get(),
                ModBlocks.GRASSY_EARTHEN_CLAY.get(),
                ModBlocks.GRASSY_SANDY_DIRT.get(),
                ModBlocks.ROOTED_GRASS_BLOCK.get());
    }

    private static void registerItemColors(ClientHelper.ItemColorEvent event) {

        event.register((itemStack, i) -> event.getColor(Items.GRASS_BLOCK.getDefaultInstance(), i),
                ModBlocks.GRASSY_SILT.get(),
                ModBlocks.GRASSY_PERMAFROST.get(),
                ModBlocks.GRASSY_SANDY_DIRT.get(),
                ModBlocks.GRASSY_EARTHEN_CLAY.get(),
                ModBlocks.ROOTED_GRASS_BLOCK.get());
    }

}