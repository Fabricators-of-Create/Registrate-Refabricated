package com.tterrag.registrate.fabric;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.function.Consumer;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.GenerationStep.Carving;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class RegistryUtil {
	private static BiMap<Registry<?>, Class<?>> registryMap;

	@SuppressWarnings("unchecked")
	public static <T> Registry<T> getRegistry(Class<T> clazz) {
		return (Registry<T>) getRegistryMap().inverse().get(clazz);
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> getRegistrationClass(Registry<T> registry) {
		return (Class<T>) getRegistryMap().get(registry);
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> getRegistrationClass(ResourceKey<? extends Registry<?>> key) {
		Registry<?> registry = Registry.REGISTRY.get(key.location());
		return (Class<T>) getRegistrationClass(registry);
	}

	public static BiMap<Registry<?>, Class<?>> getRegistryMap() {
		if (registryMap == null) {
			registryMap = createDefaultRegistryMap();
		}
		return registryMap;
	}

	public static BiMap<Registry<?>, Class<?>> createDefaultRegistryMap() {
		BiMap<Registry<?>, Class<?>> map = HashBiMap.create();

		// Fluid before Block because of FluidBlock ctor
		map.put(Registry.FLUID, Fluid.class);
		map.put(Registry.BLOCK, Block.class);
		map.put(Registry.ITEM, Item.class);

		map.put(Registry.ACTIVITY, Activity.class);
		map.put(Registry.ATTRIBUTE, Attribute.class);
//		map.put(Registry.BIOME_SOURCE, Codec.class);
		map.put(Registry.BLOCK_ENTITY_TYPE, BlockEntityType.class);
//		map.put(Registry.BLOCK_PLACER_TYPES, BlockPlacerType.class);
		map.put(Registry.BLOCKSTATE_PROVIDER_TYPES, BlockStateProviderType.class);
		map.put(Registry.CARVER, Carving.class);
//		map.put(Registry.CHUNK_GENERATOR, Codec.class);
//		map.put(Registry.CUSTOM_STAT, Identifier.class);
//		map.put(Registry.DECORATOR, FeatureDecorator.class);
		map.put(Registry.ENCHANTMENT, Enchantment.class);
		map.put(Registry.FEATURE, Feature.class);
		map.put(Registry.FEATURE_SIZE_TYPES, FeatureSizeType.class);
		map.put(Registry.FOLIAGE_PLACER_TYPES, FoliagePlacerType.class);
		map.put(Registry.LOOT_CONDITION_TYPE, LootItemConditionType.class);
		map.put(Registry.LOOT_FUNCTION_TYPE, LootItemFunctionType.class);
		map.put(Registry.LOOT_POOL_ENTRY_TYPE, LootPoolEntryType.class);
		map.put(Registry.PARTICLE_TYPE, ParticleType.class);
		map.put(Registry.POS_RULE_TEST, PosRuleTestType.class);
		map.put(Registry.RECIPE_SERIALIZER, RecipeSerializer.class);
		map.put(Registry.RECIPE_TYPE, RecipeType.class);
		map.put(Registry.REGISTRY, Registry.class);
		map.put(Registry.RULE_TEST, RuleTestType.class);
		map.put(Registry.SCHEDULE, Schedule.class);
		map.put(Registry.MENU, MenuType.class);
		map.put(Registry.SOUND_EVENT, SoundEvent.class);
		map.put(Registry.STAT_TYPE, StatType.class);
		map.put(Registry.MOB_EFFECT, MobEffect.class);
		map.put(Registry.STRUCTURE_PIECE, StructurePieceType.class);
		map.put(Registry.STRUCTURE_POOL_ELEMENT, StructurePoolElementType.class);
		map.put(Registry.STRUCTURE_PROCESSOR, StructureProcessorType.class);
//		map.put(Registry.SURFACE_BUILDER, SurfaceBuilder.class);
		map.put(Registry.TREE_DECORATOR_TYPES, TreeDecoratorType.class);
		map.put(Registry.TRUNK_PLACER_TYPES, TrunkPlacerType.class);
//		map.put(Registry.BLOCK, Block.class);
		map.put(Registry.CHUNK_STATUS, ChunkStatus.class);
		map.put(Registry.ENTITY_TYPE, EntityType.class);
//		map.put(Registry.FLUID, Fluid.class);
//		map.put(Registry.ITEM, Item.class);
		map.put(Registry.MEMORY_MODULE_TYPE, MemoryModuleType.class);
		map.put(Registry.PAINTING_VARIANT, PaintingVariant.class);
		map.put(Registry.POINT_OF_INTEREST_TYPE, PoiType.class);
		map.put(Registry.POTION, Potion.class);
		map.put(Registry.SENSOR_TYPE, SensorType.class);
		map.put(Registry.VILLAGER_PROFESSION, VillagerProfession.class);
		map.put(Registry.VILLAGER_TYPE, VillagerType.class);

		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ResourceLocation getId(Registry<?> registry) {
		return ((Registry) Registry.REGISTRY).getKey(registry);
	}

	public static void forAllRegistries(Consumer<Registry<?>> consumer) {
		// Fluid, Block, and Item need to run first
		getRegistryMap().keySet().forEach(consumer);
//		Registry.REGISTRIES.forEach(consumer);
	}
}
