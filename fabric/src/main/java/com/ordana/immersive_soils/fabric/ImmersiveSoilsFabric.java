package com.ordana.immersive_soils.fabric;

import com.ordana.immersive_soils.ImmersiveSoils;
import net.fabricmc.api.ModInitializer;

public class ImmersiveSoilsFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        ImmersiveSoils.commonInit();
    }
}
