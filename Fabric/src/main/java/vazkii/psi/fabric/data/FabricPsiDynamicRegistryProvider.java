/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;

import vazkii.psi.common.lib.LibResources;

import java.util.concurrent.CompletableFuture;

public class FabricPsiDynamicRegistryProvider extends FabricDynamicRegistryProvider {
	public FabricPsiDynamicRegistryProvider(FabricDataOutput output,
			CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void configure(HolderLookup.Provider registries, Entries entries) {
		entries.add(registries.lookupOrThrow(Registries.DAMAGE_TYPE), LibResources.PSI_OVERLOAD);
	}

	@Override
	public String getName() {
		return "Psi dynamic registries";
	}
}
