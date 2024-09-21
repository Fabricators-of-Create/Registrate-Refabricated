package com.tterrag.registrate.util.entry;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.fabric.RegistryObject;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
<<<<<<< HEAD
import org.jetbrains.annotations.Nullable;
=======
import net.neoforged.neoforge.registries.DeferredHolder;
>>>>>>> upstream/1.21/dev

/**
 * Wraps a {@link net.neoforged.neoforge.registries.DeferredHolder}, providing a cleaner API with null-safe access, and registrate-specific extensions such as {@link #getSibling(ResourceKey)}.
 *
 * @param <S>
 *            The type of the entry
 */
<<<<<<< HEAD
@EqualsAndHashCode(of = "delegate")
public class RegistryEntry<T> implements NonNullSupplier<T> {

    private static RegistryEntry<?> EMPTY = new RegistryEntry(null, RegistryObject.empty());

    public static <T> RegistryEntry<T> empty() {
        @SuppressWarnings("unchecked")
        RegistryEntry<T> t = (RegistryEntry<T>) EMPTY;
        return t;
    }

    private interface Exclusions<T> {

        T get();

        RegistryObject<T> filter(Predicate<? super T> predicate);
        
        public void updateReference(Registry<? extends T> registry);
    }

=======
public class RegistryEntry<R, S extends R> extends DeferredHolder<R, S> implements NonNullSupplier<S> {
>>>>>>> upstream/1.21/dev
    private final AbstractRegistrate<?> owner;

    @SuppressWarnings("unused")
    public RegistryEntry(AbstractRegistrate<?> owner, DeferredHolder<R, S> key) {
        super(key.getKey());

        if (owner == null)
            throw new NullPointerException("Owner must not be null");
        this.owner = owner;
    }

<<<<<<< HEAD
    /**
     * Update the underlying entry manually from the given registry.
     *
     * @param registry
     *            The registry to pull the entry from.
     */
    public void updateReference(Registry<? super T> registry) {
        RegistryObject<T> delegate = this.delegate;
        Objects.requireNonNull(delegate, "Registry entry is empty").updateReference(registry);
    }

    /**
     * Get the entry, throwing an exception if it is not present for any reason.
     * 
     * @return The (non-null) entry
     */
    @Override
    public @NonnullType T get() {
        RegistryObject<T> delegate = this.delegate;
        return Objects.requireNonNull(getUnchecked(), () -> delegate == null ? "Registry entry is empty" : "Registry entry not present: " + delegate.getId());
    }

    /**
     * Get the entry without performing any checks.
     * 
     * @return The (nullable) entry
     */
    public @Nullable T getUnchecked() {
        RegistryObject<T> delegate = this.delegate;
        return delegate == null ? null : delegate.orElse(null);
    }

    public <R, E extends R> RegistryEntry<E> getSibling(ResourceKey<? extends Registry<R>> registryType) {
        return this == EMPTY ? empty() : owner.get(getId().getPath(), registryType);
    }

    public <R, E extends R> RegistryEntry<E> getSibling(Registry<R> registry) {
        return getSibling(registry.key());
    }

    /**
     * If an entry is present, and the entry matches the given predicate, return an {@link RegistryEntry} describing the value, otherwise return an empty {@link RegistryEntry}.
=======
    public <X, Y extends X> RegistryEntry<X, Y> getSibling(ResourceKey<? extends Registry<X>> registryType) {
        return owner.get(getId().getPath(), registryType);
    }

    public <X, Y extends X> RegistryEntry<X,Y> getSibling(Registry<X> registry) {
        return getSibling(registry.key());
    }

    /**
     * If an entry is present, and the entry matches the given predicate, return an {@link Optional<RegistryEntry>} describing the value, otherwise return an empty {@link Optional}.
>>>>>>> upstream/1.21/dev
     *
     * @param predicate
     *            a {@link Predicate predicate} to apply to the entry, if present
     * @return an {@link RegistryEntry} describing the value of this {@link RegistryEntry} if the entry is present and matches the given predicate, otherwise an empty {@link RegistryEntry}
     * @throws NullPointerException
     *             if the predicate is null
     */
    public Optional<RegistryEntry<R, S>> filter(Predicate<R> predicate) {
        Objects.requireNonNull(predicate);
        if (predicate.test(get())) {
            return Optional.of(this);
        }
        return Optional.empty();
    }

    public <X> boolean is(X entry) {
        return get() == entry;
    }

    @SuppressWarnings("unchecked")
    protected static <E extends RegistryEntry<?, ?>> E cast(Class<? super E> clazz, RegistryEntry<?, ?> entry) {
        if (clazz.isInstance(entry)) {
            return (E) entry;
        }
        throw new IllegalArgumentException("Could not convert RegistryEntry: expecting " + clazz + ", found " + entry.getClass());
    }
}
