package com.ordana.immersive_soils;

import com.ordana.immersive_soils.reg.ModBlocks;
import com.ordana.immersive_soils.reg.ModCreativeTab;
import com.ordana.immersive_soils.reg.ModFeatures;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.resources.ResourceLocation;

public class ImmersiveSoils {

    public static final String MOD_ID = "immersive_soils";

    public static ResourceLocation res(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static void commonInit() {

        if (PlatHelper.getPhysicalSide().isClient()) {
            ImmersiveSoilsClient.init();
        }

        ModCreativeTab.init();
        ModBlocks.init();
        ModFeatures.init();
    }

}