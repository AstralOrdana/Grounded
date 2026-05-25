package com.ordana.grounded.reg;

import net.minecraft.resources.ResourceKey;

public record RegSupplier<T>(ResourceKey<T> id, T entry) {

    public T get() {
        return entry;
    }
    public T value() {
        return entry;
    }
}