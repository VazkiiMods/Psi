/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.psi.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		if (event.includeServer()) {
			BlockTagProvider blockTagProvider = new BlockTagProvider(event.getGenerator());
			event.getGenerator().addProvider(true, blockTagProvider);
			event.getGenerator().addProvider(true, new ItemTagProvider(event.getGenerator(), blockTagProvider));
			event.getGenerator().addProvider(true, new RecipeGenerator(event.getGenerator()));
			event.getGenerator().addProvider(true, new TrickRecipeGenerator(event.getGenerator()));
		}
		if (event.includeClient()) {
			event.getGenerator().addProvider(true, new BlockModels(event.getGenerator(), event.getExistingFileHelper()));
			event.getGenerator().addProvider(true, new ItemModels(event.getGenerator(), event.getExistingFileHelper()));
		}
	}
}
