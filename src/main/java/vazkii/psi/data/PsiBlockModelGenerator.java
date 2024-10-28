/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;

import static vazkii.psi.common.Psi.location;

public class PsiBlockModelGenerator extends BlockStateProvider {
	public PsiBlockModelGenerator(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, LibMisc.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlock(ModBlocks.psidustBlock, models().cubeAll(ForgeRegistries.BLOCKS.getKey(ModBlocks.psidustBlock).getPath(), location("block/psidust_block")));
		simpleBlock(ModBlocks.psimetalBlock, models().cubeAll(ForgeRegistries.BLOCKS.getKey(ModBlocks.psimetalBlock).getPath(), location("block/psimetal_block")));
		simpleBlock(ModBlocks.psigemBlock, models().cubeAll(ForgeRegistries.BLOCKS.getKey(ModBlocks.psigemBlock).getPath(), location("block/psigem_block")));
		simpleBlock(ModBlocks.psimetalPlateBlack, models().cubeAll(ForgeRegistries.BLOCKS.getKey(ModBlocks.psimetalPlateBlack).getPath(), location("block/psimetal_plate_black")));
		simpleBlock(ModBlocks.psimetalPlateWhite, models().cubeAll(ForgeRegistries.BLOCKS.getKey(ModBlocks.psimetalPlateWhite).getPath(), location("block/psimetal_plate_white")));
		simpleBlock(ModBlocks.psimetalPlateBlackLight, models().cubeBottomTop(ForgeRegistries.BLOCKS.getKey(ModBlocks.psimetalPlateBlackLight).getPath(),
				location("block/psimetal_plate_black_light"), location("block/psimetal_plate_black"), location("block/psimetal_plate_black")));
		simpleBlock(ModBlocks.psimetalPlateWhiteLight, models().cubeBottomTop(ForgeRegistries.BLOCKS.getKey(ModBlocks.psimetalPlateWhiteLight).getPath(),
				location("block/psimetal_plate_white_light"), location("block/psimetal_plate_white"), location("block/psimetal_plate_white")));
		simpleBlock(ModBlocks.psimetalEbony, models().cubeAll(ForgeRegistries.BLOCKS.getKey(ModBlocks.psimetalEbony).getPath(), location("block/ebony_psimetal_block")));
		simpleBlock(ModBlocks.psimetalIvory, models().cubeAll(ForgeRegistries.BLOCKS.getKey(ModBlocks.psimetalIvory).getPath(), location("block/ivory_psimetal_block")));
		simpleBlock(ModBlocks.conjured, models().withExistingParent(ForgeRegistries.BLOCKS.getKey(ModBlocks.conjured).getPath(), "block/block").texture("particle", location("block/empty")));
	}

	@Nonnull
	@Override
	public String getName() {
		return "Psi blockstates and block models";
	}
}
