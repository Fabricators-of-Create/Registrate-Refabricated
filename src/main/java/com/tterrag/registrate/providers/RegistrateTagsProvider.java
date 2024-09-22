package com.tterrag.registrate.providers;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.tterrag.registrate.AbstractRegistrate;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public interface RegistrateTagsProvider<T> extends RegistrateLookupFillerProvider {

    FabricTagProvider<T>.FabricTagBuilder addTag(TagKey<T> tag);

    CompletableFuture<TagsProvider.TagLookup<T>> contentsGetter();

	ResourceKey<? extends Registry<T>> registry();

    class Impl<T> extends FabricTagProvider<T> implements RegistrateTagsProvider<T> {
        private final AbstractRegistrate<?> owner;
        private final ProviderType<? extends Impl<T>> type;
        private final String name;

        public Impl(AbstractRegistrate<?> owner, ProviderType<? extends Impl<T>> type, String name, FabricDataOutput packOutput, ResourceKey<? extends Registry<T>> registryIn, CompletableFuture<HolderLookup.Provider> registriesLookup) {
            super(packOutput, registryIn, registriesLookup);

            this.owner = owner;
            this.type = type;
            this.name = name;
        }

        @Override
        public String getName() {
            return "Tags (%s)".formatted(name);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            owner.genData(type, this);
        }

        @Override
        public EnvType getSide() {
            return EnvType.SERVER;
        }

        @Override
        public FabricTagProvider<T>.FabricTagBuilder addTag(TagKey<T> tag) {
            return super.getOrCreateTagBuilder(tag);
        }

        @Override
        public CompletableFuture<HolderLookup.Provider> getFilledProvider() {
            return createContentsProvider();
        }

		@Override
		public ResourceKey<? extends Registry<T>> registry() {
			return registryKey;
		}

	}

    class IntrinsicImpl<T> extends FabricTagProvider<T> implements RegistrateTagsProvider<T> {
        private final AbstractRegistrate<?> owner;
        private final ProviderType<? extends IntrinsicImpl<T>> type;
        private final String name;

        public IntrinsicImpl(AbstractRegistrate<?> owner, ProviderType<? extends IntrinsicImpl<T>> type, String name, FabricDataOutput packOutput, ResourceKey<? extends Registry<T>> registryIn, CompletableFuture<Provider> registriesLookup, Function<T, ResourceKey<T>> keyExtractor) {
            super(packOutput, registryIn, registriesLookup);

            this.owner = owner;
            this.type = type;
            this.name = name;
        }

        @Override
        public String getName() {
            return "Tags (%s)".formatted(name);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            owner.genData(type, this);
        }

        @Override
        public EnvType getSide() {
            return EnvType.SERVER;
        }

        @Override
        public FabricTagProvider<T>.FabricTagBuilder addTag(TagKey<T> tag) {
            return super.getOrCreateTagBuilder(tag);
        }

        @Override
        public CompletableFuture<HolderLookup.Provider> getFilledProvider() {
            return createContentsProvider();
        }

		@Override
		public ResourceKey<? extends Registry<T>> registry() {
			return registryKey;
		}

	}
}
