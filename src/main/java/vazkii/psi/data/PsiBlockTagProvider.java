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
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.ModTags;

public class PsiBlockTagProvider extends BlockTagsProvider {

	public PsiBlockTagProvider(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper) {
		super(dataGenerator, LibMisc.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(ModTags.Blocks.BLOCK_PSIMETAL).add(ModBlocks.psimetalBlock);
		tag(ModTags.Blocks.BLOCK_PSIGEM).add(ModBlocks.psigemBlock);
		tag(ModTags.Blocks.BLOCK_EBONY_PSIMETAL).add(ModBlocks.psimetalEbony);
		tag(ModTags.Blocks.BLOCK_IVORY_PSIMETAL).add(ModBlocks.psimetalIvory);

		tag(Tags.Blocks.STORAGE_BLOCKS).add(ModBlocks.psimetalBlock);
		tag(Tags.Blocks.STORAGE_BLOCKS).add(ModBlocks.psigemBlock);
		tag(Tags.Blocks.STORAGE_BLOCKS).add(ModBlocks.psimetalEbony);
		tag(Tags.Blocks.STORAGE_BLOCKS).add(ModBlocks.psimetalIvory);
	}

	@Override
	public String getName() {
		return "Psi block tags";
	}
}
