package com.ordana.grounded;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class PlatHelper {
    public static boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT);
    }
}
