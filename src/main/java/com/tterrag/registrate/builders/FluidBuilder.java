package com.tterrag.registrate.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.tterrag.registrate.fabric.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

/**
 * A builder for fluids, allows for customization of the {@link SimpleFlowableFluid.Properties} and {@link FluidData.FluidAttributes}, and creation of the source variant, fluid block, and bucket item, as well as
 * data associated with fluids (tags, etc.).
 *
 * @param <T>
 *            The type of fluid being built
 * @param <P>
 *            Parent object type
 */
public class FluidBuilder<T extends SimpleFlowableFluid, P> extends AbstractBuilder<Fluid, T, P, FluidBuilder<T, P>> {

//    private static class Builder extends FluidAttributes.Builder {
//
//        protected Builder(ResourceLocation still, ResourceLocation flowing, BiFunction<FluidAttributes.Builder, Fluid, FluidAttributes> attributesFactory) {
//            super(still, flowing, attributesFactory);
//        }
//    }

//    /**
//     * Create a new {@link FluidBuilder} and configure data. The created builder will use the default attributes class ({@link FluidAttributes}) and fluid class ({@link SimpleFlowableFluid.Flowing}).
//     *
//     * @param <P>
//     *            Parent object type
//     * @param owner
//     *            The owning {@link AbstractRegistrate} object
//     * @param parent
//     *            The parent object
//     * @param name
//     *            Name of the entry being built
//     * @param callback
//     *            A callback used to actually register the built entry
//     * @param stillTexture
//     *            The texture to use for still fluids
//     * @param flowingTexture
//     *            The texture to use for flowing fluids
//     * @return A new {@link FluidBuilder} with reasonable default data generators.
//     * @see #create(AbstractRegistrate, Object, String, BuilderCallback, ResourceLocation, ResourceLocation, NonNullBiFunction, NonNullFunction)
//     */
//    public static <P> FluidBuilder<SimpleFlowableFluid.Flowing, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
//        return create(owner, parent, name, callback, stillTexture, flowingTexture, (NonNullBiFunction<FluidAttributes.Builder, Fluid, FluidAttributes>) null);
//    }

    /**
     * Create a new {@link FluidBuilder} and configure data. The created builder will use the default fluid class ({@link SimpleFlowableFluid.Flowing}).
     *
     * @param <P>
     *            Parent object type
     * @param owner
     *            The owning {@link AbstractRegistrate} object
     * @param parent
     *            The parent object
     * @param name
     *            Name of the entry being built
     * @param callback
     *            A callback used to actually register the built entry
     * @param stillTexture
     *            The texture to use for still fluids
     * @param flowingTexture
     *            The texture to use for flowing fluids
     * @param attributesFactory
     *            A factory that creates the fluid attributes instance
     * @return A new {@link FluidBuilder} with reasonable default data generators.
     * @see #create(AbstractRegistrate, Object, String, BuilderCallback, ResourceLocation, ResourceLocation, NonNullBiFunction, NonNullFunction)
     */
    public static <P> FluidBuilder<SimpleFlowableFluid.Flowing, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture/*,
            @Nullable NonNullBiFunction<FluidAttributes.Builder, Fluid, FluidAttributes> attributesFactory*/) {
        return create(owner, parent, name, callback, stillTexture, flowingTexture/*, attributesFactory*/, SimpleFlowableFluid.Flowing::new);
    }

//    /**
//     * Create a new {@link FluidBuilder} and configure data. The created builder will use the default attributes class ({@link FluidAttributes}).
//     *
//     * @param <T>
//     *            The type of the builder
//     * @param <P>
//     *            Parent object type
//     * @param owner
//     *            The owning {@link AbstractRegistrate} object
//     * @param parent
//     *            The parent object
//     * @param name
//     *            Name of the entry being built
//     * @param callback
//     *            A callback used to actually register the built entry
//     * @param stillTexture
//     *            The texture to use for still fluids
//     * @param flowingTexture
//     *            The texture to use for flowing fluids
//     * @param factory
//     *            A factory that creates the flowing fluid
//     * @return A new {@link FluidBuilder} with reasonable default data generators.
//     * @see #create(AbstractRegistrate, Object, String, BuilderCallback, ResourceLocation, ResourceLocation, NonNullBiFunction, NonNullFunction)
//     */
//    public static <T extends SimpleFlowableFluid, P> FluidBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture,
//            NonNullFunction<SimpleFlowableFluid.Properties, T> factory) {
//        return create(owner, parent, name, callback, stillTexture, flowingTexture, null, factory);
//    }

    /**
     * Create a new {@link FluidBuilder} and configure data. Used in lieu of adding side-effects to constructor, so that alternate initialization strategies can be done in subclasses.
     * <p>
     * The fluid will be assigned the following data:
     * <ul>
     * <li>The default translation (via {@link #defaultLang()})</li>
     * <li>A default {@link SimpleFlowableFluid.Source source fluid} (via {@link #defaultSource})</li>
     * <li>A default block for the fluid, with its own default blockstate and model that configure the particle texture (via {@link #defaultBlock()})</li>
     * <li>A default bucket item, that uses a simple generated item model with a texture of the same name as this fluid (via {@link #defaultBucket()})</li>
     * <li>Tagged with {@link FluidTags#WATER}</li>
     * </ul>
     *
     * @param <T>
     *            The type of the builder
     * @param <P>
     *            Parent object type
     * @param owner
     *            The owning {@link AbstractRegistrate} object
     * @param parent
     *            The parent object
     * @param name
     *            Name of the entry being built
     * @param callback
     *            A callback used to actually register the built entry
     * @param stillTexture
     *            The texture to use for still fluids
     * @param flowingTexture
     *            The texture to use for flowing fluids
     * @param attributesFactory
     *            A factory that creates the fluid attributes instance
     * @param factory
     *            A factory that creates the flowing fluid
     * @return A new {@link FluidBuilder} with reasonable default data generators.
     */
    public static <T extends SimpleFlowableFluid, P> FluidBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture,
            /*@Nullable NonNullBiFunction<FluidAttributes.Builder, Fluid, FluidAttributes> attributesFactory,*/ NonNullFunction<SimpleFlowableFluid.Properties, T> factory) {
        FluidBuilder<T, P> ret = new FluidBuilder<>(owner, parent, name, callback, stillTexture, flowingTexture/*, attributesFactory*/, factory)
                .defaultLang().defaultSource().defaultBlock().defaultBucket()
                .tag(FluidTags.WATER);

        return ret;
    }

    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;
    private final String sourceName;
    private final String bucketName;
    private final NonNullSupplier<FluidData.Builder> attributes;
    private final NonNullFunction<SimpleFlowableFluid.Properties, T> factory;

    @Nullable
    private Boolean defaultSource, defaultBlock, defaultBucket;

    private NonNullConsumer<FluidData.Builder> attributesCallback = $ -> {};
    private NonNullConsumer<SimpleFlowableFluid.Properties> properties;
    @Nullable
    private NonNullSupplier<? extends SimpleFlowableFluid> source;
    private List<TagKey<Fluid>> tags = new ArrayList<>();

    protected FluidBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture,
            /*@Nullable BiFunction<FluidAttributes.Builder, Fluid, FluidAttributes> attributesFactory,*/ NonNullFunction<SimpleFlowableFluid.Properties, T> factory) {
        super(owner, parent, "flowing_" + name, callback, Registry.FLUID_REGISTRY);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.sourceName = name;
        this.bucketName = name + "_bucket";
        this.attributes = FluidData.Builder::new;
//        this.attributes = () -> attributesFactory == null ? FluidAttributes.builder(stillTexture, flowingTexture) : new Builder(stillTexture, flowingTexture, attributesFactory);
        this.factory = factory;

        String bucketName = this.bucketName;
        this.properties = p -> p.bucket(() -> owner.get(bucketName, Registry.ITEM_REGISTRY).get())
                .block(() -> owner.<Block, LiquidBlock>get(name, Registry.BLOCK_REGISTRY).get());
    }

    /**
     * Modify the attributes of the fluid. Modifications are done lazily, but the passed function is composed with the current one, and as such this method can be called multiple times to perform
     * different operations.
     *
     * @param cons
     *            The action to perform on the attributes
     * @return this {@link FluidBuilder}
     */
    public FluidBuilder<T, P> attributes(NonNullConsumer<FluidData.Builder> cons) {
        attributesCallback = attributesCallback.andThen(cons);
        return this;
    }

    /**
     * Modify the properties of the fluid. Modifications are done lazily, but the passed function is composed with the current one, and as such this method can be called multiple times to perform
     * different operations.
     *
     * @param cons
     *            The action to perform on the properties
     * @return this {@link FluidBuilder}
     */
    public FluidBuilder<T, P> properties(NonNullConsumer<SimpleFlowableFluid.Properties> cons) {
        properties = properties.andThen(cons);
        return this;
    }

    /**
     * Assign the default translation, as specified by {@link RegistrateLangProvider#getAutomaticName(NonNullSupplier)}. This is the default, so it is generally not necessary to call, unless for
     * undoing previous changes.
     *
     * @return this {@link FluidBuilder}
     */
    public FluidBuilder<T, P> defaultLang() {
        return lang(f -> ((FluidData.FluidAttributes)f).getData().translationKey(), RegistrateLangProvider.toEnglishName(sourceName));
    }

    /**
     * Set the translation for this fluid.
     *
     * @param name
     *            A localized English name
     * @return this {@link FluidBuilder}
     */
    public FluidBuilder<T, P> lang(String name) {
        return lang(f -> ((FluidData.FluidAttributes)f).getData().translationKey(), name);
    }

    /**
     * Create a standard {@link SimpleFlowableFluid.Still} for this fluid which will be built and registered along with this fluid.
     *
     * @return this {@link FluidBuilder}
     * @see #source(NonNullFunction)
     * @throws IllegalStateException
     *             If {@link #source(NonNullFunction)} has been called before this method
     */
    public FluidBuilder<T, P> defaultSource() {
        if (this.defaultSource != null) {
            throw new IllegalStateException("Cannot set a default source after a custom source has been created");
        }
        this.defaultSource = true;
        return this;
    }

    /**
     * Create a {@link SimpleFlowableFluid} for this fluid, which is created by the given factory, and which will be built and registered along with this fluid.
     *
     * @param factory
     *            A factory for the fluid, which accepts the properties and returns a new fluid
     * @return this {@link FluidBuilder}
     */
    public FluidBuilder<T, P> source(NonNullFunction<SimpleFlowableFluid.Properties, ? extends SimpleFlowableFluid> factory) {
        this.defaultSource = false;
        this.source = NonNullSupplier.lazy(() -> factory.apply(makeProperties()));
        return this;
    }

    /**
     * Create a standard {@link LiquidBlock} for this fluid, building it immediately, and not allowing for further configuration.
     *
     * @return this {@link FluidBuilder}
     * @see #block()
     * @throws IllegalStateException
     *             If {@link #block()} or {@link #block(NonNullBiFunction)} has been called before this method
     */
    public FluidBuilder<T, P> defaultBlock() {
        if (this.defaultBlock != null) {
            throw new IllegalStateException("Cannot set a default block after a custom block has been created");
        }
        this.defaultBlock = true;
        return this;
    }

    /**
     * Create a standard {@link LiquidBlock} for this fluid, and return the builder for it so that further customization can be done.
     *
     * @return the {@link BlockBuilder} for the {@link LiquidBlock}
     */
    public BlockBuilder<LiquidBlock, FluidBuilder<T, P>> block() {
        return block1(FluidBlockHelper::createFluidBlock);
    }

    /**
     * Create a {@link LiquidBlock} for this fluid, which is created by the given factory, and return the builder for it so that further customization can be done.
     *
     * @param <B>
     *            The type of the block
     * @param factory
     *            A factory for the block, which accepts the block object and properties and returns a new block
     * @return the {@link BlockBuilder} for the {@link LiquidBlock}
     */
    public <B extends LiquidBlock> BlockBuilder<B, FluidBuilder<T, P>> block(NonNullBiFunction<NonNullSupplier<? extends T>, BlockBehaviour.Properties, ? extends B> factory) {
        if (this.defaultBlock == Boolean.FALSE) {
            throw new IllegalStateException("Only one call to block/noBlock per builder allowed");
        }
        this.defaultBlock = false;
        NonNullSupplier<T> supplier = asSupplier();
        return getOwner().<B, FluidBuilder<T, P>>block(this, sourceName, p -> factory.apply(supplier, p))
                .properties(p -> BlockBehaviour.Properties.copy(Blocks.WATER).dropsLike(Blocks.AIR))
//                .properties(p -> {
//                    // TODO is this ok?
//                    FluidAttributes attrs = this.attributes.get().build(Fluids.WATER);
//                    return p.lightLevel($ -> attrs.getLuminosity());
//                })
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getBuilder(sourceName)
                                .texture("particle", stillTexture)));
    }

    // Fabric TODO
    @SuppressWarnings("unchecked")
    public <B extends LiquidBlock> BlockBuilder<B, FluidBuilder<T, P>> block1(NonNullBiFunction<? extends T, BlockBehaviour.Properties, ? extends B> factory) {
        return block((supplier, settings) -> ((NonNullBiFunction<T, BlockBehaviour.Properties, ? extends B>) factory).apply(supplier.get(), settings));
    }

    @Beta
    public FluidBuilder<T, P> noBlock() {
        if (this.defaultBlock == Boolean.FALSE) {
            throw new IllegalStateException("Only one call to block/noBlock per builder allowed");
        }
        this.defaultBlock = false;
        return this;
    }

    /**
     * Create a standard {@link BucketItem} for this fluid, building it immediately, and not allowing for further configuration.
     *
     * @return this {@link FluidBuilder}
     * @see #bucket()
     * @throws IllegalStateException
     *             If {@link #bucket()} or {@link #bucket(NonNullBiFunction)} has been called before this method
     */
    public FluidBuilder<T, P> defaultBucket() {
        if (this.defaultBucket != null) {
            throw new IllegalStateException("Cannot set a default bucket after a custom bucket has been created");
        }
        defaultBucket = true;
        return this;
    }

    /**
     * Create a standard {@link BucketItem} for this fluid, and return the builder for it so that further customization can be done.
     *
     * @return the {@link ItemBuilder} for the {@link BucketItem}
     */
    public ItemBuilder<BucketItem, FluidBuilder<T, P>> bucket() {
        return bucket(BucketItem::new);
    }

    /**
     * Create a {@link BucketItem} for this fluid, which is created by the given factory, and return the builder for it so that further customization can be done.
     *
     * @param <I>
     *            The type of the bucket item
     * @param factory
     *            A factory for the bucket item, which accepts the fluid object supplier and properties and returns a new item
     * @return the {@link ItemBuilder} for the {@link BucketItem}
     */
    public <I extends BucketItem> ItemBuilder<I, FluidBuilder<T, P>> bucket(NonNullBiFunction<? extends SimpleFlowableFluid, Item.Properties, ? extends I> factory) {
        if (this.defaultBucket == Boolean.FALSE) {
            throw new IllegalStateException("Only one call to bucket/noBucket per builder allowed");
        }
        this.defaultBucket = false;
        NonNullSupplier<? extends SimpleFlowableFluid> source = this.source;
        if (source == null) {
            throw new IllegalStateException("Cannot create a bucket before creating a source block");
        }
        return getOwner().<I, FluidBuilder<T, P>>item(this, bucketName, p -> ((NonNullBiFunction<SimpleFlowableFluid, Item.Properties, ? extends I>) factory).apply(this.source.get(), p)) // Fabric TODO
                .properties(p -> p.craftRemainder(Items.BUCKET).stacksTo(1))
                .model((ctx, prov) -> prov.generated(ctx::getEntry, new ResourceLocation(getOwner().getModid(), "item/" + bucketName)));
    }

    @Beta
    public FluidBuilder<T, P> noBucket() {
        if (this.defaultBucket == Boolean.FALSE) {
            throw new IllegalStateException("Only one call to bucket/noBucket per builder allowed");
        }
        this.defaultBucket = false;
        return this;
    }

    /**
     * Assign {@link TagKey}{@code s} to this fluid and its source fluid. Multiple calls will add additional tags.
     *
     * @param tags
     *            The tags to assign
     * @return this {@link FluidBuilder}
     */
    @SafeVarargs
    public final FluidBuilder<T, P> tag(TagKey<Fluid>... tags) {
        FluidBuilder<T, P> ret = this.tag(ProviderType.FLUID_TAGS, tags);
        if (this.tags.isEmpty()) {
            ret.getOwner().<RegistrateTagsProvider<Fluid>, Fluid>setDataGenerator(ret.sourceName, getRegistryKey(), ProviderType.FLUID_TAGS,
                    prov -> this.tags.stream().map(tag -> prov.tag(tag)).forEach(p -> p.add(getSource())));
        }
        this.tags.addAll(Arrays.asList(tags));
        return ret;
    }

    /**
     * Remove {@link TagKey}{@code s} from this fluid and its source fluid. Multiple calls will remove additional tags.
     *
     * @param tags
     *            The tags to remove
     * @return this {@link FluidBuilder}
     */
    @SafeVarargs
    public final FluidBuilder<T, P> removeTag(TagKey<Fluid>... tags) {
        this.tags.removeAll(Arrays.asList(tags));
        return this.removeTag(ProviderType.FLUID_TAGS, tags);
    }

    private SimpleFlowableFluid getSource() {
        NonNullSupplier<? extends SimpleFlowableFluid> source = this.source;
        Preconditions.checkNotNull(source, "Fluid has no source block: " + sourceName);
        return source.get();
    }

    private SimpleFlowableFluid.Properties makeProperties() {
        FluidData.Builder attributes = this.attributes.get();
        RegistryEntry<Block> block = getOwner().getOptional(sourceName, Registry.BLOCK_REGISTRY);
        attributesCallback.accept(attributes);
        // Force the translation key after the user callback runs
        // This is done because we need to remove the lang data generator if using the block key,
        // and if it was possible to undo this change, it might result in the user translation getting
        // silently lost, as there's no good way to check whether the translation key was changed.
        // TODO improve this?
        if (block.isPresent()) {
            attributes.translationKey(block.get().getDescriptionId());
            setData(ProviderType.LANG, NonNullBiConsumer.noop());
        } else {
            attributes.translationKey(Util.makeDescriptionId("fluid", new ResourceLocation(getOwner().getModid(), sourceName)));
        }
        NonNullSupplier<? extends SimpleFlowableFluid> source = this.source;
        SimpleFlowableFluid.Properties ret = new SimpleFlowableFluid.Properties(source == null ? null : source::get, asSupplier(), attributes);
        properties.accept(ret);
        return ret;
    }

    @Override
    protected T createEntry() {
        return factory.apply(makeProperties());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Additionally registers the source fluid.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public FluidEntry<T> register() {
        if (defaultSource == Boolean.TRUE) {
            source(SimpleFlowableFluid.Still::new);
        }
        if (defaultBlock == Boolean.TRUE) {
            block().register();
        }
        if (defaultBucket == Boolean.TRUE) {
            bucket().register();
        }
        NonNullSupplier<? extends SimpleFlowableFluid> source = this.source;
        if (source != null) {
            getCallback().accept(sourceName, Fluid.class, (FluidBuilder) this, source::get);
        } else {
            throw new IllegalStateException("Fluid must have a source version: " + getName());
        }
        if (renderHandler == null) {
            this.setDefaultRenderHandler();
        }
        onRegister(this::registerRenderHandler);
        return (FluidEntry<T>) super.register();
    }

    @Override
    protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate) {
        return new FluidEntry<>(getOwner(), delegate);
    }

    // New - Fabric only

    private Supplier<Supplier<RenderType>> renderLayer;
    private Supplier<RenderHandlerFactory> renderHandler;
    private int color = -1;

    public FluidBuilder<T, P> layer(Supplier<Supplier<RenderType>> layer) {
        EnvExecutor.runWhenOn(EnvType.CLIENT, () -> () -> {
            Preconditions.checkArgument(RenderType.chunkBufferLayers().contains(layer.get().get()), "Invalid block layer: " + layer);
        });
        if (this.renderLayer == null) {
            onRegister(this::registerLayer);
        }
        this.renderLayer = layer;
        return this;
    }

    protected void registerLayer(T entry) {
        EnvExecutor.runWhenOn(EnvType.CLIENT, () -> () -> {
            final RenderType layer = renderLayer.get().get();
            BlockRenderLayerMap.INSTANCE.putFluid(entry, layer);
        });
    }

    public FluidBuilder<T, P> renderHandler(Supplier<RenderHandlerFactory> handler) {
        if (this.color != -1) {
            throw new IllegalArgumentException("Can only set either color or render handler factory!");
        }
        this.renderHandler = handler;
        return this;
    }

    public FluidBuilder<T, P> color(int color) {
        if (this.renderHandler != null) {
            throw new IllegalArgumentException("Can only set either color or render handler factory!");
        }
        this.color = color;
        return this;
    }

    @SuppressWarnings("deprecation")
    protected void registerRenderHandler(T entry) {
        EnvExecutor.runWhenOn(EnvType.CLIENT, () -> () -> {
            final FluidRenderHandler handler = renderHandler.get().create(stillTexture, flowingTexture);
            FluidRenderHandlerRegistry.INSTANCE.register(entry, handler);
            FluidRenderHandlerRegistry.INSTANCE.register(entry.getSource(), handler);
            ClientSpriteRegistryCallback.event(TextureAtlas.LOCATION_BLOCKS).register((atlasTexture, registry) -> {
                registry.register(stillTexture);
                registry.register(flowingTexture);
            });
        });
    }

    protected void setDefaultRenderHandler() {
        this.renderHandler = () -> (stillTexture, flowingTexture) -> {
            final SimpleFluidRenderHandler handler = new SimpleFluidRenderHandler(stillTexture, flowingTexture, flowingTexture, color);
            return handler;
        };
    }

    public interface RenderHandlerFactory {
        FluidRenderHandler create(ResourceLocation stillTexture, ResourceLocation flowingTexture);
    }
}
