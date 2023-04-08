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
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.ModTags;

public class PsiItemTagProvider extends ItemTagsProvider {
	
	public PsiItemTagProvider(DataGenerator generator, PsiBlockTagProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
		super(generator, blockTagProvider, LibMisc.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(Tags.Items.DUSTS).add(ModItems.psidust);
		tag(Tags.Items.INGOTS).add(ModItems.psimetal);
		tag(Tags.Items.INGOTS).add(ModItems.ebonyPsimetal);
		tag(Tags.Items.INGOTS).add(ModItems.ivoryPsimetal);
		tag(Tags.Items.GEMS).add(ModItems.psigem);

		tag(ModTags.PSIDUST).add(ModItems.psidust);
		tag(ModTags.EBONY_SUBSTANCE).add(ModItems.ebonySubstance);
		tag(ModTags.IVORY_SUBSTANCE).add(ModItems.ivorySubstance);

		tag(ModTags.INGOT_PSIMETAL).add(ModItems.psimetal);
		copy(ModTags.Blocks.BLOCK_PSIMETAL, ModTags.BLOCK_PSIMETAL);

		tag(ModTags.GEM_PSIGEM).add(ModItems.psigem);
		copy(ModTags.Blocks.BLOCK_PSIGEM, ModTags.BLOCK_PSIGEM);

		tag(ModTags.INGOT_EBONY_PSIMETAL).add(ModItems.ebonyPsimetal);
		copy(ModTags.Blocks.BLOCK_EBONY_PSIMETAL, ModTags.BLOCK_EBONY_PSIMETAL);
		tag(ModTags.INGOT_IVORY_PSIMETAL).add(ModItems.ivoryPsimetal);
		copy(ModTags.Blocks.BLOCK_IVORY_PSIMETAL, ModTags.BLOCK_IVORY_PSIMETAL);
		copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
	}

	@Override
	public String getName() {
		return "Psi item tags";
	}
}
