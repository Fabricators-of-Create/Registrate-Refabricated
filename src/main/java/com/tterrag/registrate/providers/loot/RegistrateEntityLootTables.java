package com.tterrag.registrate.providers.loot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.tterrag.registrate.AbstractRegistrate;

import lombok.Getter;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class RegistrateEntityLootTables extends SimpleFabricLootTableProvider implements RegistrateLootTables {

    private final AbstractRegistrate<?> parent;
    @Getter
    private final HolderLookup.Provider provider;
    private final Consumer<RegistrateEntityLootTables> callback;

    private final Map<ResourceKey<LootTable>, Builder> entries = new HashMap<>();

    public RegistrateEntityLootTables(HolderLookup.Provider provider, AbstractRegistrate<?> parent, Consumer<RegistrateEntityLootTables> callback, FabricDataOutput output) {
        super(output, CompletableFuture.supplyAsync(() -> provider), LootContextParamSets.ENTITY);
        this.parent = parent;
        this.provider = provider;
        this.callback = callback;
    }

    @Override
    public void generate(@NotNull BiConsumer<ResourceKey<LootTable>, Builder> consumer) {
        callback.accept(this);
        entries.forEach(consumer);
    }

    public void add(EntityType<?> type, LootTable.Builder table) {
        entries.put(type.getDefaultLootTable(), table);
    }

    public void add(ResourceKey<LootTable> id, LootTable.Builder table) {
        entries.put(id, table);
    }

}
