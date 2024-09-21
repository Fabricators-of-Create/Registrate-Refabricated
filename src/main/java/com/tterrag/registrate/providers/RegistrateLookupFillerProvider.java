package com.tterrag.registrate.providers;

import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public interface RegistrateLookupFillerProvider extends RegistrateProvider {

    CompletableFuture<HolderLookup.Provider> getFilledProvider();

}
