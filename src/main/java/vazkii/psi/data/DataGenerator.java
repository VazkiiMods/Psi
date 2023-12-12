/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.psi.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		if(event.includeServer()) {
			PsiBlockTagProvider blockTagProvider = new PsiBlockTagProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), existingFileHelper);
			event.getGenerator().addProvider(true, blockTagProvider);
			event.getGenerator().addProvider(true, new PsiDamageTypeTagsProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), existingFileHelper));
			event.getGenerator().addProvider(true, new PsiItemTagProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), blockTagProvider.contentsGetter(), existingFileHelper));
			event.getGenerator().addProvider(true, new PsiRecipeGenerator(event.getGenerator().getPackOutput()));
			event.getGenerator().addProvider(true, new PsiTrickRecipeGenerator(event.getGenerator().getPackOutput()));
		}

		if(event.includeClient()) {
			event.getGenerator().addProvider(true, new PsiBlockModelGenerator(event.getGenerator().getPackOutput(), event.getExistingFileHelper()));
			event.getGenerator().addProvider(true, new PsiItemModelGenerator(event.getGenerator().getPackOutput(), event.getExistingFileHelper()));
		}
	}
}
