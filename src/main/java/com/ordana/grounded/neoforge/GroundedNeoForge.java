//? neoforge {
/*package com.ordana.grounded.neoforge;

import com.ordana.grounded.Grounded;
import net.neoforged.fml.common.Mod;

@Mod(Grounded.MOD_ID)
public class GroundedNeoForge {
    public static final String MOD_ID = Grounded.MOD_ID;

    public GroundedNeoForge() {
        Grounded.commonInit();
        if (PlatHelper.isClient()) {
            GroundedClient.init();
        }
    }
}

*///?}