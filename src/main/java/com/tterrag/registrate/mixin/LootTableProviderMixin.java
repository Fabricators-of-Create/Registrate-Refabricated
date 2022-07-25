package com.tterrag.registrate.mixin;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(LootTableProvider.class)
public class LootTableProviderMixin extends LootTableProvider {

	public DataGenerator dataGen;

	public LootTableProviderMixin(DataGenerator dataGenerator) {
		super(dataGenerator);
		dataGen = dataGenerator;
	}
}
