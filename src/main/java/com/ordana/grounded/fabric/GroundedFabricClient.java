//? fabric {
package com.ordana.grounded.fabric;

import com.ordana.grounded.GroundedClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;

public class GroundedFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        GroundedClient.registerBlockColors(BlockColorRegistry::register);
    }
}
//?}
