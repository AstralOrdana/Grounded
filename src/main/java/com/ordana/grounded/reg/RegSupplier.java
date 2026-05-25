package com.ordana.grounded.reg;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public record RegSupplier<T>(ResourceKey<T> id, T entry) {

    public T get() {
        return entry;
    }
    public T value() {
        return entry;
    }

    public Item asItem() {
        if (entry instanceof ItemLike itemLike) {
            return itemLike.asItem();
        }
        return Items.AIR;
    }

    public Identifier identifier() {
        return id.identifier();
    }

    public Identifier location() {
        return id.identifier();
    }
}