package com.tterrag.registrate.providers;

import com.tterrag.registrate.AbstractRegistrate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import java.util.concurrent.CompletableFuture;

public class RegistrateDataMapProvider extends DataMapProvider implements RegistrateProvider {

	private final AbstractRegistrate<?> parent;

	protected RegistrateDataMapProvider(AbstractRegistrate<?> parent, PackOutput output, CompletableFuture<HolderLookup.Provider> pvd) {
		super(output, pvd);
		this.parent = parent;
	}

	@Override
	public LogicalSide getSide() {
		return LogicalSide.SERVER;
	}

	@Override
	protected void gather() {
		parent.genData(ProviderType.DATA_MAP, this);
	}

}
