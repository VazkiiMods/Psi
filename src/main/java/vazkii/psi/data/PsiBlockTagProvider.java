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
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.lib.ModTags;

import java.util.concurrent.CompletableFuture;

public class PsiBlockTagProvider extends BlockTagsProvider {

	public PsiBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, PsiAPI.MOD_ID, existingFileHelper);
	}

	@Override
	public @NotNull String getName() {
		return "Psi block tags";
	}

	@Override
	protected void addTags(HolderLookup.Provider pProvider) {
		tag(ModTags.Blocks.BLOCK_PSIMETAL).add(ModBlocks.psimetalBlock.get());
		tag(ModTags.Blocks.BLOCK_PSIGEM).add(ModBlocks.psigemBlock.get());
		tag(ModTags.Blocks.BLOCK_EBONY_PSIMETAL).add(ModBlocks.psimetalEbony.get());
		tag(ModTags.Blocks.BLOCK_IVORY_PSIMETAL).add(ModBlocks.psimetalIvory.get());

		tag(Tags.Blocks.STORAGE_BLOCKS).add(ModBlocks.psimetalBlock.get());
		tag(Tags.Blocks.STORAGE_BLOCKS).add(ModBlocks.psigemBlock.get());
		tag(Tags.Blocks.STORAGE_BLOCKS).add(ModBlocks.psimetalEbony.get());
		tag(Tags.Blocks.STORAGE_BLOCKS).add(ModBlocks.psimetalIvory.get());

		tag(BlockTags.AIR).add(ModBlocks.conjured.get());

		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.cadAssembler.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.programmer.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.psidustBlock.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.psimetalBlock.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.psigemBlock.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.psimetalPlateBlack.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.psimetalPlateBlackLight.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.psimetalPlateWhite.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.psimetalPlateWhiteLight.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.psimetalEbony.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.psimetalIvory.get());

		tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.psimetalBlock.get());
		tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.psigemBlock.get());
		tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.psimetalPlateBlack.get());
		tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.psimetalPlateBlackLight.get());
		tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.psimetalPlateWhite.get());
		tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.psimetalPlateWhiteLight.get());
		tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.psimetalEbony.get());
		tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.psimetalIvory.get());
	}
}
