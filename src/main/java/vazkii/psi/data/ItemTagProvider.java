/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;

import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.ModTags;

public class ItemTagProvider extends ItemTagsProvider {
	public ItemTagProvider(DataGenerator generator, BlockTagProvider blockTagProvider) {
		super(generator, blockTagProvider);
	}

	@Override
	protected void registerTags() {
		getOrCreateTagBuilder(ModTags.PSIDUST).add(ModItems.psidust);
		getOrCreateTagBuilder(ModTags.EBONY_SUBSTANCE).add(ModItems.ebonySubstance);
		getOrCreateTagBuilder(ModTags.IVORY_SUBSTANCE).add(ModItems.ivorySubstance);

		getOrCreateTagBuilder(ModTags.INGOT_PSIMETAL).add(ModItems.psimetal);
		copy(ModTags.Blocks.BLOCK_PSIMETAL, ModTags.BLOCK_PSIMETAL);

		getOrCreateTagBuilder(ModTags.GEM_PSIGEM).add(ModItems.psigem);
		copy(ModTags.Blocks.BLOCK_PSIGEM, ModTags.BLOCK_PSIGEM);

		getOrCreateTagBuilder(ModTags.INGOT_EBONY_PSIMETAL).add(ModItems.ebonyPsimetal);
		copy(ModTags.Blocks.BLOCK_EBONY_PSIMETAL, ModTags.BLOCK_EBONY_PSIMETAL);
		getOrCreateTagBuilder(ModTags.INGOT_IVORY_PSIMETAL).add(ModItems.ivoryPsimetal);
		copy(ModTags.Blocks.BLOCK_IVORY_PSIMETAL, ModTags.BLOCK_IVORY_PSIMETAL);
	}

	@Override
	public String getName() {
		return "Psi item tags";
	}
}
