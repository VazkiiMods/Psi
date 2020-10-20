/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;

import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.lib.ModTags;

public class BlockTagProvider extends BlockTagsProvider {
	public BlockTagProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void registerTags() {
		getOrCreateBuilder(ModTags.Blocks.BLOCK_PSIMETAL).addItemEntry(ModBlocks.psimetalBlock);
		getOrCreateBuilder(ModTags.Blocks.BLOCK_PSIGEM).addItemEntry(ModBlocks.psigemBlock);
		getOrCreateBuilder(ModTags.Blocks.BLOCK_EBONY_PSIMETAL).addItemEntry(ModBlocks.psimetalEbony);
		getOrCreateBuilder(ModTags.Blocks.BLOCK_IVORY_PSIMETAL).addItemEntry(ModBlocks.psimetalIvory);
	}

	@Override
	public String getName() {
		return "Psi block tags";
	}
}
