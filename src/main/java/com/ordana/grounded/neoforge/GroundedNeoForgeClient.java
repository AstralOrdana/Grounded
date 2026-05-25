//? neoforge {
/*package com.ordana.grounded.neoforge;

import com.ordana.grounded.Grounded;
import com.ordana.grounded.GroundedClient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@Mod(Grounded.MOD_ID)
@EventBusSubscriber(modid = Grounded.MOD_ID)
public class GroundedNeoForgeClient {

    @SubscribeEvent
    public static void registerColorHandlers(RegisterColorHandlersEvent.BlockTintSources event) {
        GroundedClient.registerBlockColors(event::register);
    }
}

*///?}