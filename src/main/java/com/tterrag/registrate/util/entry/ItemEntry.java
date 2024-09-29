package com.tterrag.registrate.util.entry;

import com.tterrag.registrate.AbstractRegistrate;

import io.github.fabricators_of_create.porting_lib.util.DeferredHolder;
import net.minecraft.world.item.Item;

public class ItemEntry<T extends Item> extends ItemProviderEntry<Item, T> {

    public ItemEntry(AbstractRegistrate<?> owner, DeferredHolder<Item, T> delegate) {
        super(owner, delegate);
    }
    
    public static <T extends Item> ItemEntry<T> cast(RegistryEntry<Item, T> entry) {
        return RegistryEntry.cast(ItemEntry.class, entry);
    }
}
