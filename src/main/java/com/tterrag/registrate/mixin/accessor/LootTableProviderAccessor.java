package com.tterrag.registrate.mixin.accessor;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.nio.file.Path;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;

@Mixin(LootTableProvider.class)
public interface LootTableProviderAccessor {

    @Accessor
    static Logger getLOGGER() {
        throw new UnsupportedOperationException();
    }

    @Accessor("pathProvider")
	DataGenerator.PathProvider getPathProvider();
}
