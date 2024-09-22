package com.tterrag.registrate.fabric;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.inventory.InventoryMenu;

public class FluidSpriteReloadListener implements IdentifiableResourceReloadListener, ResourceManagerReloadListener {
	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("registrate", "fluid_sprites");
	public static final FluidSpriteReloadListener INSTANCE = new FluidSpriteReloadListener();

	private final Multimap<ResourceLocation, Consumer<TextureAtlasSprite>> callbacks = HashMultimap.create();

	public void registerCallback(ResourceLocation id, Consumer<TextureAtlasSprite> callback) {
		callbacks.put(id, callback);
	}

	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		// Fluid rendering always uses the block atlas
		Function<ResourceLocation, TextureAtlasSprite> atlas = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
		for (ResourceLocation id : callbacks.keySet()) {
			TextureAtlasSprite sprite = atlas.apply(id);
			for (Consumer<TextureAtlasSprite> consumer : callbacks.get(id)) {
				consumer.accept(sprite);
			}
		}
	}

	@Override
	public ResourceLocation getFabricId() {
		return ID;
	}

	public Collection<ResourceLocation> getFabricDependencies() {
		return Arrays.asList(ResourceReloadListenerKeys.TEXTURES, ResourceReloadListenerKeys.MODELS);
	}
}
