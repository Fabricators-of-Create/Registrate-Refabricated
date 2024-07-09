package com.tterrag.registrate.providers;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.DebugMarkers;
import com.tterrag.registrate.util.nullness.NonnullType;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import lombok.extern.log4j.Log4j2;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import org.jetbrains.annotations.Nullable;

@Log4j2
public class RegistrateDataProvider implements DataProvider {

    @SuppressWarnings("null")
    static final BiMap<String, ProviderType<?>> TYPES = HashBiMap.create();

    public static @Nullable String getTypeName(ProviderType<?> type) {
        return TYPES.inverse().get(type);
    }

    private final String mod;
    private final Map<ProviderType<?>, RegistrateProvider> subProviders = new LinkedHashMap<>();
    private final CompletableFuture<Provider> registriesLookup;

    public record DataInfo(FabricDataOutput output, ExistingFileHelper helper, CompletableFuture<Provider> registriesLookup) {}

    public RegistrateDataProvider(AbstractRegistrate<?> parent, String modid, ExistingFileHelper helper, FabricDataOutput output, CompletableFuture<Provider> registriesLookup) {
        this.mod = modid;
        this.registriesLookup = registriesLookup;

        EnumSet<EnvType> sides = EnumSet.noneOf(EnvType.class);
//        if (event.includeServer()) {
            sides.add(EnvType.SERVER);
//        }
//        if (event.includeClient()) {
            sides.add(EnvType.CLIENT);
//        }
        log.debug(DebugMarkers.DATA, "Gathering providers for sides: {}", sides);
        Map<ProviderType<?>, RegistrateProvider> known = new HashMap<>();
        for (String id : TYPES.keySet()) {
            ProviderType<?> type = TYPES.get(id);
            RegistrateProvider prov = type.create(parent, new DataInfo(output, helper, registriesLookup), known);
            known.put(type, prov);
            if (sides.contains(prov.getSide())) {
                log.debug(DebugMarkers.DATA, "Adding provider for type: {}", id);
                subProviders.put(type, prov);
            }
        }
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return registriesLookup.thenCompose(provider -> {
            var list = Lists.<CompletableFuture<?>>newArrayList();

            for (Map.Entry<@NonnullType ProviderType<?>, RegistrateProvider> e : subProviders.entrySet()) {
                log.debug(DebugMarkers.DATA, "Generating data for type: {}", getTypeName(e.getKey()));
                list.add(e.getValue().run(cache));
            };

            return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public String getName() {
        return "Registrate Provider for " + mod + " [" + subProviders.values().stream().map(DataProvider::getName).collect(Collectors.joining(", ")) + "]";
    }

    @SuppressWarnings("unchecked")
    public <P extends RegistrateProvider> Optional<P> getSubProvider(ProviderType<P> type) {
        return Optional.ofNullable((P) subProviders.get(type));
    }
}
