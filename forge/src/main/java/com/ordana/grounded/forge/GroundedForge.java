package com.ordana.grounded.forge;

import com.ordana.grounded.Grounded;
import net.minecraftforge.fml.common.Mod;

@Mod(Grounded.MOD_ID)
public class GroundedForge {
    public static final String MOD_ID = Grounded.MOD_ID;

    public GroundedForge() {
        Grounded.commonInit();
    }
}

