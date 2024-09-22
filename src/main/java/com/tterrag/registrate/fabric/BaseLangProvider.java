package com.tterrag.registrate.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BaseLangProvider extends FabricLanguageProvider {
	private final Map<String, String> entries = new HashMap<>();

	protected BaseLangProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(output, registryLookup);
	}

	protected BaseLangProvider(FabricDataOutput output, String languageCode, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(output, languageCode, registryLookup);
	}

	@Override
	public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
		entries.forEach(translationBuilder::add);
	}

	public void add(String key, String value) {
		entries.put(key, value);
	}
}
