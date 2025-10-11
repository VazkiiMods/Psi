/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import vazkii.psi.api.PsiAPI;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = PsiAPI.MOD_ID)
public class DataGenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		net.minecraft.data.DataGenerator generator = event.getGenerator();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		PackOutput packOutput = generator.getPackOutput();

		if(event.includeServer()) {
			PsiBlockTagProvider blockTagProvider = new PsiBlockTagProvider(packOutput, lookupProvider, existingFileHelper);
			generator.addProvider(true, blockTagProvider);
			generator.addProvider(true, new PsiDamageTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));
			generator.addProvider(true, new PsiItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper));
			generator.addProvider(true, new PsiRecipeGenerator(packOutput, lookupProvider));
			generator.addProvider(true, new LootTableProvider(packOutput, Collections.emptySet(),
					List.of(new LootTableProvider.SubProviderEntry(PsiBlockLootProvider::new, LootContextParamSets.BLOCK)), lookupProvider));
		}

		if(event.includeClient()) {
			generator.addProvider(true, new PsiBlockModelGenerator(packOutput, existingFileHelper));
			generator.addProvider(true, new PsiItemModelGenerator(packOutput, existingFileHelper));
		}
	}
}
