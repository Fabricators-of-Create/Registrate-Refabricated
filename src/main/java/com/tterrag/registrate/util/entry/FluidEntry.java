package com.tterrag.registrate.util.entry;

import java.util.Optional;
import java.util.function.Predicate;

import com.tterrag.registrate.AbstractRegistrate;

<<<<<<< HEAD
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import com.tterrag.registrate.fabric.RegistryObject;
import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import org.jetbrains.annotations.Nullable;

public class FluidEntry<T extends SimpleFlowableFluid> extends RegistryEntry<T> {
=======
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class FluidEntry<T extends BaseFlowingFluid> extends RegistryEntry<Fluid, T> {
>>>>>>> upstream/1.21/dev

    private final @Nullable BlockEntry<? extends Block> block;

    public FluidEntry(AbstractRegistrate<?> owner, DeferredHolder<Fluid, T> delegate) {
        super(owner, delegate);
        BlockEntry<? extends Block> block = null;
        try {
<<<<<<< HEAD
            block = BlockEntry.cast(getSibling(Registries.BLOCK));
=======
            block = BlockEntry.cast(getSibling(BuiltInRegistries.BLOCK));
>>>>>>> upstream/1.21/dev
        } catch (IllegalArgumentException e) {} // TODO add way to get entry optionally
        this.block = block;
    }

    @Override
    public <R> boolean is(R entry) {
        return get().isSame((Fluid) entry);
    }

    @SuppressWarnings("unchecked")
<<<<<<< HEAD
    public <S extends SimpleFlowableFluid> S getSource() {
=======
    public <S extends BaseFlowingFluid> S getSource() {
>>>>>>> upstream/1.21/dev
        return (S) get().getSource();
    }

    @SuppressWarnings({ "unchecked", "null" })
    public <B extends Block> Optional<B> getBlock() {
        return (Optional<B>) Optional.ofNullable(block).map(RegistryEntry::get);
    }

    @Override
    public RegistryEntry<T> filter(Predicate<? super T> predicate) {
        return super.filter(predicate);
    }

    @SuppressWarnings({ "unchecked", "null" })
    public <I extends Item> Optional<I> getBucket() {
        return Optional.ofNullable((I) get().getBucket());
    }
}
