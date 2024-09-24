package com.tterrag.registrate.providers;

import com.tterrag.registrate.AbstractRegistrate;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.api.EnvType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public final class RegistrateGenericProvider implements RegistrateProvider
{
    private final AbstractRegistrate<?> registrate;
    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> registries;
    private final ExistingFileHelper existingFileHelper;
    private final EnvType side;
    private final ProviderType<RegistrateGenericProvider> providerType;
    private final List<Generator> generators = Lists.newArrayList();

    @ApiStatus.Internal
    RegistrateGenericProvider(AbstractRegistrate<?> registrate, RegistrateDataProvider.DataInfo info, EnvType side, ProviderType<RegistrateGenericProvider> providerType)
    {
        this.registrate = registrate;
        this.side = side;
        this.providerType = providerType;

        output = info.output();
        registries = info.registriesLookup();
        existingFileHelper = info.helper();
    }

    public RegistrateGenericProvider add(Generator generator)
    {
        generators.add(generator);
        return this;
    }

    @Override
    public EnvType getSide()
    {
        return side;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache)
    {
        generators.clear();
        var data = new GeneratorData(output, registries, existingFileHelper);
        registrate.genData(providerType, this);
        return CompletableFuture.allOf(generators
                .stream()
                .map(generator -> generator.generate(data))
                .map(provider -> provider.run(cache))
                .toArray(CompletableFuture[]::new)
        );
    }

    @Override
    public String getName()
    {
        return "generic_%s_provider".formatted(side.name().toLowerCase(Locale.ROOT));
    }

    public record GeneratorData(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper)
    {
    }

    @FunctionalInterface
    public interface Generator
    {
        DataProvider generate(GeneratorData data);
    }
}
