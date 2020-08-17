/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import vazkii.psi.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		if (event.includeServer()) {
			BlockTagProvider blockTagProvider = new BlockTagProvider(event.getGenerator());
			event.getGenerator().addProvider(blockTagProvider);
			event.getGenerator().addProvider(new ItemTagProvider(event.getGenerator(), blockTagProvider));
			event.getGenerator().addProvider(new RecipeGenerator(event.getGenerator()));
			event.getGenerator().addProvider(new TrickRecipeGenerator(event.getGenerator()));
		}
		if (event.includeClient()) {
			event.getGenerator().addProvider(new BlockModels(event.getGenerator(), event.getExistingFileHelper()));
			event.getGenerator().addProvider(new ItemModels(event.getGenerator(), event.getExistingFileHelper()));
		}
	}
}
