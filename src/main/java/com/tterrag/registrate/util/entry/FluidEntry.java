package com.tterrag.registrate.util.entry;

import java.util.Optional;
import java.util.function.Predicate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.fabric.DeferredHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import org.jetbrains.annotations.Nullable;

public class FluidEntry<T extends SimpleFlowableFluid> extends RegistryEntry<Fluid, T> {

    private final @Nullable BlockEntry<? extends Block> block;

    public FluidEntry(AbstractRegistrate<?> owner, DeferredHolder<Fluid, T> delegate) {
        super(owner, delegate);
        BlockEntry<? extends Block> block = null;
        try {
            block = BlockEntry.cast(getSibling(BuiltInRegistries.BLOCK));
        } catch (IllegalArgumentException e) {} // TODO add way to get entry optionally
        this.block = block;
    }

    @Override
    public <R> boolean is(R entry) {
        return get().isSame((Fluid) entry);
    }

    @SuppressWarnings("unchecked")
    public <S extends SimpleFlowableFluid> S getSource() {
        return (S) get().getSource();
    }

    @SuppressWarnings({ "unchecked", "null" })
    public <B extends Block> Optional<B> getBlock() {
        return (Optional<B>) Optional.ofNullable(block).map(RegistryEntry::get);
    }

    @Override
    public Optional<RegistryEntry<Fluid, T>> filter(Predicate<Fluid> predicate) {
        return super.filter(predicate);
    }

    @SuppressWarnings({ "unchecked", "null" })
    public <I extends Item> Optional<I> getBucket() {
        return Optional.ofNullable((I) get().getBucket());
    }
}
