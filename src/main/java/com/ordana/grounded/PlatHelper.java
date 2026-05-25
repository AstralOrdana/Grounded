package com.ordana.grounded;

//? fabric {
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
//?} else {
/*import net.neoforged.fml.loading.FMLEnvironment;
*///?}

public class PlatHelper {
    public static boolean isClient() {
        //? fabric {
        return FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT);
        //?} else {
        /*return FMLEnvironment.getDist().isClient();
        *///?}
    }
}
