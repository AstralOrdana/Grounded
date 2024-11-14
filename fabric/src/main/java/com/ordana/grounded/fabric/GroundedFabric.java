package com.ordana.grounded.fabric;

import com.ordana.grounded.Grounded;
import net.fabricmc.api.ModInitializer;

public class GroundedFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        Grounded.commonInit();
    }
}
