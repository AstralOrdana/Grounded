package com.ordana.grounded;

import com.ordana.grounded.reg.ModBlocks;
import com.ordana.grounded.reg.ModCreativeTab;
import net.minecraft.resources.Identifier;

public class Grounded {

    public static final String MOD_ID = "grounded";

    public static Identifier res(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }

    public static void commonInit() {
        ModCreativeTab.init();
        ModBlocks.init();
    }

}