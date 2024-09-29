package com.tterrag.registrate.util.entry;

import java.util.function.Consumer;

import com.tterrag.registrate.AbstractRegistrate;

import io.github.fabricators_of_create.porting_lib.util.DeferredHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.MenuType;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import org.jetbrains.annotations.Nullable;

public class MenuEntry<T extends AbstractContainerMenu> extends RegistryEntry<MenuType<?>, MenuType<T>> {

    public MenuEntry(AbstractRegistrate<?> owner, DeferredHolder<MenuType<?>, MenuType<T>> delegate) {
        super(owner, delegate);
    }

    public T create(int windowId, Inventory playerInv) {
        return get().create(windowId, playerInv);
    }

    public MenuConstructor asProvider() {
        return (window, playerinv, $) -> create(window, playerinv);
    }

    public void open(ServerPlayer player, Component displayName) {
        open(player, displayName, asProvider());
    }

    public void open(ServerPlayer player, Component displayName, Consumer<Object> extraData) {
        open(player, displayName, asProvider(), extraData);
    }

    public void open(ServerPlayer player, Component displayName, MenuConstructor provider) {
        player.openMenu(new SimpleMenuProvider(provider, displayName));
    }

    public void open(ServerPlayer player, Component displayName, MenuConstructor provider, Consumer<Object> extraData) {
        player.openMenu(new ExtendedScreenHandlerFactory<>() {
            // FIXME
            @Override
            public Object getScreenOpeningData(ServerPlayer player) {
                extraData.accept(null);
                return null;
            }

            @Override
            public Component getDisplayName() {
                return displayName;
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return provider.createMenu(i, inventory, player);
            }
        });
    }
}
