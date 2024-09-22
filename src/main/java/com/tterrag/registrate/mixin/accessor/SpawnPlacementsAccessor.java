package com.tterrag.registrate.mixin.accessor;

import net.minecraft.world.entity.SpawnPlacementType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

@Mixin(SpawnPlacements.class)
public interface SpawnPlacementsAccessor {
    @Invoker
    static <T extends Mob> void callRegister(EntityType<T> entityType, SpawnPlacementType spawnPlacementType, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> predicate) {
        throw new UnsupportedOperationException();
    }
}
