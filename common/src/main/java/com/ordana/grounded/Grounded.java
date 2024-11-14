package com.ordana.grounded;

import com.ordana.grounded.reg.ModBlocks;
import com.ordana.grounded.reg.ModCreativeTab;
import com.ordana.grounded.reg.ModFeatures;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.resources.ResourceLocation;

public class Grounded {

    public static final String MOD_ID = "grounded";

    public static ResourceLocation res(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static void commonInit() {

        if (PlatHelper.getPhysicalSide().isClient()) {
            GroundedClient.init();
        }

        ModCreativeTab.init();
        ModBlocks.init();
        ModFeatures.init();
    }

}