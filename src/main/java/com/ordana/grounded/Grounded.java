package com.ordana.grounded;

import com.ordana.grounded.reg.ModBlocks;
import com.ordana.grounded.reg.ModCreativeTab;
import com.ordana.grounded.reg.ModTags;
import com.ordana.grounded.reg.RegSupplier;
import dev.worldgen.lithostitched.api.event.AddWorldgenModifiersEvent;
import dev.worldgen.lithostitched.api.util.InjectionType;
import dev.worldgen.lithostitched.api.worldgen.modifier.WorldgenModifier;
import dev.worldgen.lithostitched.api.worldgen.surface.LithostitchedSurfaceConditions;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.jspecify.annotations.NonNull;

public class Grounded {

    public static final String MOD_ID = "grounded";

    public static Identifier res(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }

    public static void commonInit() {
        ModCreativeTab.init();
        ModBlocks.init();
        AddWorldgenModifiersEvent.EVENT.register((registries, consumer) -> {
            consumer.accept(
                    Identifier.fromNamespaceAndPath(MOD_ID, "surface_rules"),
                    WorldgenModifier.builder()
                            .priority(1000)  // default is 1000; we set it at 3000 to let datapacks override our surface rules by default
                            .addSurfaceRule(Level.OVERWORLD, InjectionType.PREPEND, makeRules(registries))
            );
        });
    }

    private static SurfaceRules.RuleSource makeRules(RegistryAccess registries) {
        return SurfaceRules.sequence(
                getRuleSource(registries, ModTags.HAS_EARTHEN_CLAY, ModBlocks.EARTHEN_CLAY, ModBlocks.GRASSY_EARTHEN_CLAY),
                getRuleSource(registries, ModTags.HAS_PERMAFROST, ModBlocks.PERMAFROST, ModBlocks.GRASSY_PERMAFROST)
        );
    }

    private static SurfaceRules.@NonNull RuleSource getRuleSource(RegistryAccess registries, TagKey<Biome> biomeTagKey, RegSupplier<Block> dirt, RegSupplier<Block> grass) {
        return SurfaceRules.ifTrue(
                LithostitchedSurfaceConditions.biome(registries.getOrThrow(biomeTagKey)),
                SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(1.75), makeStateRule(dirt)), SurfaceRules.ifTrue(surfaceNoiseAbove(-0.95), makeStateRule(grass)))
        );
    }


    private static SurfaceRules.RuleSource makeStateRule(final RegSupplier<Block> block) {
        return SurfaceRules.state(block.entry().defaultBlockState());
    }

    private static SurfaceRules.ConditionSource surfaceNoiseAbove(final double threshold) {
        return SurfaceRules.noiseCondition(Noises.SURFACE, threshold / (double)8.25F, Double.MAX_VALUE);
    }

}