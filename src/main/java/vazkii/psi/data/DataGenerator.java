/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import vazkii.psi.common.lib.LibMisc;

@EventBusSubscriber(modid = LibMisc.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		if(event.includeServer()) {
			PsiBlockTagProvider blockTagProvider = new PsiBlockTagProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), existingFileHelper);
			event.getGenerator().addProvider(true, blockTagProvider);
			event.getGenerator().addProvider(true, new PsiDamageTypeTagsProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), existingFileHelper));
			event.getGenerator().addProvider(true, new PsiItemTagProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), blockTagProvider.contentsGetter(), existingFileHelper));
			event.getGenerator().addProvider(true, new PsiRecipeGenerator(event.getGenerator().getPackOutput(), event.getLookupProvider()));
		}

		if(event.includeClient()) {
			event.getGenerator().addProvider(true, new PsiBlockModelGenerator(event.getGenerator().getPackOutput(), event.getExistingFileHelper()));
			event.getGenerator().addProvider(true, new PsiItemModelGenerator(event.getGenerator().getPackOutput(), event.getExistingFileHelper()));
		}
	}
}
