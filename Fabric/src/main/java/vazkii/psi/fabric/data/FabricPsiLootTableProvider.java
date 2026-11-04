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
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import vazkii.psi.data.PsiBlockLootProvider;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class FabricPsiLootTableProvider extends SimpleFabricLootTableProvider {
	private final CompletableFuture<HolderLookup.Provider> registries;

	public FabricPsiLootTableProvider(FabricDataOutput output,
			CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, LootContextParamSets.BLOCK);
		this.registries = registries;
	}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		PsiBlockLootProvider provider = new PsiBlockLootProvider(registries.join());
		provider.generate();
		provider.generatedLootTables().forEach((key, table) -> output.accept(key, table.setRandomSequence(key.location())));
	}
}
