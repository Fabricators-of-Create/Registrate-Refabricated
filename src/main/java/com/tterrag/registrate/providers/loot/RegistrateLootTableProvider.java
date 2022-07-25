package com.tterrag.registrate.providers.loot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.common.collect.*;
import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.fabric.NonNullTriFunction;
import com.tterrag.registrate.mixin.LootTableProviderMixin;
import com.tterrag.registrate.mixin.accessor.LootContextParamSetsAccessor;
import com.tterrag.registrate.mixin.accessor.LootTableProviderAccessor;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class RegistrateLootTableProvider extends LootTableProviderMixin implements RegistrateProvider {

	@Override
    public void run(CachedOutput pCache) {
        Map<ResourceLocation, LootTable> map = Maps.newHashMap();
        this.getTables().forEach((p_124458_) -> {
            p_124458_.getFirst().get().accept((p_176077_, p_176078_) -> {
                if (map.put(p_176077_, p_176078_.setParamSet(p_124458_.getSecond()).build()) != null) {
                    throw new IllegalStateException("Duplicate loot table " + p_176077_);
                }
            });
        });
        ValidationContext validationcontext = new ValidationContext(LootContextParamSets.ALL_PARAMS, (p_124465_) -> {
            return null;
        }, map::get);

        validate(map, validationcontext);

        Multimap<String, String> multimap = validationcontext.getProblems();
        if (!multimap.isEmpty()) {
            multimap.forEach((p_124446_, p_124447_) -> {
                LootTableProviderAccessor.getLOGGER().warn("Found validation problem in {}: {}", p_124446_, p_124447_);
            });
            throw new IllegalStateException("Failed to validate loot tables, see logs");
        } else {
            map.forEach((p_124451_, p_124452_) -> {
                Path path1 = ((LootTableProviderAccessor)(this)).getPathProvider().json(p_124451_);

                try {
                    DataProvider.saveStable(pCache, LootTables.serialize(p_124452_), path1);
                } catch (IOException ioexception) {
                    LootTableProviderAccessor.getLOGGER().error("Couldn't save loot table {}", path1, ioexception);
                }

            });
        }
    }

    public interface LootType<T extends RegistrateLootTables> {

        static LootType<RegistrateBlockLootTables> BLOCK = register("block", LootContextParamSets.BLOCK, RegistrateBlockLootTables::new);
//        static LootType<RegistrateEntityLootTables> ENTITY = register("entity", LootContextParamSets.ENTITY, RegistrateEntityLootTables::new);

        T getLootCreator(AbstractRegistrate<?> parent, Consumer<T> callback, FabricDataGenerator generator);

        LootContextParamSet getLootSet();

        static <T extends RegistrateLootTables> LootType<T> register(String name, LootContextParamSet set, NonNullTriFunction<AbstractRegistrate, Consumer<T>, FabricDataGenerator, T> factory) {
            LootType<T> type = new LootType<T>() {
                @Override
                public T getLootCreator(AbstractRegistrate<?> parent, Consumer<T> callback, FabricDataGenerator generator) {
                    return factory.apply(parent, callback, generator);
                }

                @Override
                public LootContextParamSet getLootSet() {
                    return set;
                }
            };
            LOOT_TYPES.put(name, type);
            return type;
        }
    }

    private static final Map<String, LootType<?>> LOOT_TYPES = new HashMap<>();

    private final AbstractRegistrate<?> parent;

    private final Multimap<LootType<?>, Consumer<? super RegistrateLootTables>> specialLootActions = HashMultimap.create();
    private final Multimap<LootContextParamSet, Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>> lootActions = HashMultimap.create();
    private final Set<RegistrateLootTables> currentLootCreators = new HashSet<>();

    public RegistrateLootTableProvider(AbstractRegistrate<?> parent, DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
        this.parent = parent;
    }

    @Override
    public String getName() {
        return "Loot tables";
    }

    @Override
    public EnvType getSide() {
        return EnvType.SERVER;
    }

    //@Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationresults) {
        currentLootCreators.forEach(c -> c.validate(map, validationresults));
    }

    @SuppressWarnings("unchecked")
    public <T extends RegistrateLootTables> void addLootAction(LootType<T> type, NonNullConsumer<? extends RegistrateLootTables> action) {
        this.specialLootActions.put(type, (Consumer<? super RegistrateLootTables>) action);
    }

    public void addLootAction(LootContextParamSet set, Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> action) {
        this.lootActions.put(set, action);
    }

    private Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>> getLootCreator(AbstractRegistrate<?> parent, LootType<?> type) {
        return () -> {
            RegistrateLootTables creator = type.getLootCreator(parent, cons -> specialLootActions.get(type).forEach(c -> c.accept(cons)), (FabricDataGenerator) dataGen);
            currentLootCreators.add(creator);
            return creator;
        };
    }

    private static final BiMap<ResourceLocation, LootContextParamSet> SET_REGISTRY = LootContextParamSetsAccessor.getREGISTRY();

//    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        parent.genData(ProviderType.LOOT, this);
        currentLootCreators.clear();
        ImmutableList.Builder<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> builder = ImmutableList.builder();
        for (LootType<?> type : LOOT_TYPES.values()) {
            builder.add(Pair.of(getLootCreator(parent, type), type.getLootSet()));
        }
        for (LootContextParamSet set : SET_REGISTRY.values()) {
            builder.add(Pair.of(() -> callback -> lootActions.get(set).forEach(a -> a.accept(callback)), set));
        }
        return builder.build();
    }
}
