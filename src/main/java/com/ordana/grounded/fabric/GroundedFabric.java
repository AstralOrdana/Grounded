//? fabric {
package com.ordana.grounded.fabric;

import com.ordana.grounded.Grounded;
import com.ordana.grounded.PlatformSpecific;
import com.ordana.grounded.reg.ModCreativeTab;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;
import java.util.function.Predicate;

public class GroundedFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        Grounded.commonInit();
        CreativeModeTabEvents.MODIFY_OUTPUT_ALL.register((creativeModeTab, output)->{
          ModCreativeTab.addItems(new PlatformSpecific.ItemToTabEvent() {

              @Override
              public void addAfter(ResourceKey<CreativeModeTab> tab, ItemStack target, ItemLike[] entries) {
                  Optional<ResourceKey<CreativeModeTab>> resourceKey = BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(creativeModeTab);
                  if (resourceKey.isPresent() && resourceKey.get().equals(tab)) {
                        output.insertAfter(target, entries);
                  }
              }
          });
        });
    }
}
//?}