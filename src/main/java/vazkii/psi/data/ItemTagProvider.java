/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
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
		getOrCreateBuilder(ModTags.PSIDUST).addItemEntry(ModItems.psidust);
		getOrCreateBuilder(ModTags.EBONY_SUBSTANCE).addItemEntry(ModItems.ebonySubstance);
		getOrCreateBuilder(ModTags.IVORY_SUBSTANCE).addItemEntry(ModItems.ivorySubstance);

		getOrCreateBuilder(ModTags.INGOT_PSIMETAL).addItemEntry(ModItems.psimetal);
		copy(ModTags.Blocks.BLOCK_PSIMETAL, ModTags.BLOCK_PSIMETAL);

		getOrCreateBuilder(ModTags.GEM_PSIGEM).addItemEntry(ModItems.psigem);
		copy(ModTags.Blocks.BLOCK_PSIGEM, ModTags.BLOCK_PSIGEM);

		getOrCreateBuilder(ModTags.INGOT_EBONY_PSIMETAL).addItemEntry(ModItems.ebonyPsimetal);
		copy(ModTags.Blocks.BLOCK_EBONY_PSIMETAL, ModTags.BLOCK_EBONY_PSIMETAL);
		getOrCreateBuilder(ModTags.INGOT_IVORY_PSIMETAL).addItemEntry(ModItems.ivoryPsimetal);
		copy(ModTags.Blocks.BLOCK_IVORY_PSIMETAL, ModTags.BLOCK_IVORY_PSIMETAL);
	}

	@Override
	public String getName() {
		return "Psi item tags";
	}
}
