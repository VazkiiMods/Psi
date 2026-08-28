/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.data.PsiBlockLootProvider;
import vazkii.psi.data.PsiBlockModelGenerator;
import vazkii.psi.data.PsiBlockTagProvider;
import vazkii.psi.data.PsiDamageTypeTagsProvider;
import vazkii.psi.data.PsiDataRegistries;
import vazkii.psi.data.PsiItemModelGenerator;
import vazkii.psi.data.PsiItemTagProvider;
import vazkii.psi.data.PsiRecipeGenerator;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = PsiAPI.MOD_ID)
public class NeoForgePsiDataGenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		event.createDatapackRegistryObjects(PsiDataRegistries.builder());
		net.minecraft.data.DataGenerator generator = event.getGenerator();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		PackOutput packOutput = generator.getPackOutput();

		if(event.includeServer()) {
			PsiBlockTagProvider blockTagProvider = new PsiBlockTagProvider(packOutput, lookupProvider);
			generator.addProvider(true, blockTagProvider);
			generator.addProvider(true, new PsiDamageTypeTagsProvider(packOutput, lookupProvider));
			generator.addProvider(true, new PsiItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter()));
			generator.addProvider(true, new PsiRecipeGenerator(packOutput, lookupProvider));
			generator.addProvider(true, new LootTableProvider(packOutput, Collections.emptySet(),
					List.of(new LootTableProvider.SubProviderEntry(PsiBlockLootProvider::new, LootContextParamSets.BLOCK)), lookupProvider));
		}

		if(event.includeClient()) {
			generator.addProvider(true, new PsiBlockModelGenerator(packOutput));
			generator.addProvider(true, new PsiItemModelGenerator(packOutput));
		}
	}
}
