//? fabric {
package com.ordana.grounded.fabric.data;

import com.ordana.grounded.fabric.data.provider.BiomeTagsProvider;
import com.ordana.grounded.fabric.data.provider.BlockTagsProvider;
import com.ordana.grounded.fabric.data.provider.ItemTagsProvider;
import com.ordana.grounded.fabric.data.provider.ModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataGenerator;

public class GroundedDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModelProvider::new);
        pack.addProvider(BiomeTagsProvider::new);
        var blockTags = pack.addProvider(BlockTagsProvider::new);
        pack.addProvider((output, registriesFuture) -> new ItemTagsProvider(output, registriesFuture, blockTags));
    }
}
//?}