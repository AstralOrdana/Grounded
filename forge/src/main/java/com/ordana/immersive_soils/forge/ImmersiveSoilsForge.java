package com.ordana.immersive_soils.forge;

import com.ordana.immersive_soils.ImmersiveSoils;
import net.minecraftforge.fml.common.Mod;

@Mod(ImmersiveSoils.MOD_ID)
public class ImmersiveSoilsForge {
    public static final String MOD_ID = ImmersiveSoils.MOD_ID;

    public ImmersiveSoilsForge() {
        ImmersiveSoils.commonInit();
    }
}

