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
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;

import static vazkii.psi.common.Psi.location;

public class PsiBlockModelGenerator extends BlockStateProvider {
	public PsiBlockModelGenerator(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, LibMisc.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlock(ModBlocks.psidustBlock, models().cubeAll(ForgeRegistries.BLOCKS.getKey(ModBlocks.psidustBlock).getPath(), location("blocks/psidust_block")));
		simpleBlock(ModBlocks.psimetalBlock, models().cubeAll(ForgeRegistries.BLOCKS.getKey(ModBlocks.psimetalBlock).getPath(), location("blocks/psimetal_block")));
		simpleBlock(ModBlocks.psigemBlock, models().cubeAll(ForgeRegistries.BLOCKS.getKey(ModBlocks.psigemBlock).getPath(), location("blocks/psigem_block")));
		simpleBlock(ModBlocks.psimetalPlateBlack, models().cubeAll(ForgeRegistries.BLOCKS.getKey(ModBlocks.psimetalPlateBlack).getPath(), location("blocks/psimetal_plate_black")));
		simpleBlock(ModBlocks.psimetalPlateWhite, models().cubeAll(ForgeRegistries.BLOCKS.getKey(ModBlocks.psimetalPlateWhite).getPath(), location("blocks/psimetal_plate_white")));
		simpleBlock(ModBlocks.psimetalPlateBlackLight, models().cubeBottomTop(ForgeRegistries.BLOCKS.getKey(ModBlocks.psimetalPlateBlackLight).getPath(),
				location("blocks/psimetal_plate_black_light"), location("blocks/psimetal_plate_black"), location("blocks/psimetal_plate_black")));
		simpleBlock(ModBlocks.psimetalPlateWhiteLight, models().cubeBottomTop(ForgeRegistries.BLOCKS.getKey(ModBlocks.psimetalPlateWhiteLight).getPath(),
				location("blocks/psimetal_plate_white_light"), location("blocks/psimetal_plate_white"), location("blocks/psimetal_plate_white")));
		simpleBlock(ModBlocks.psimetalEbony, models().cubeAll(ForgeRegistries.BLOCKS.getKey(ModBlocks.psimetalEbony).getPath(), location("blocks/ebony_psimetal_block")));
		simpleBlock(ModBlocks.psimetalIvory, models().cubeAll(ForgeRegistries.BLOCKS.getKey(ModBlocks.psimetalIvory).getPath(), location("blocks/ivory_psimetal_block")));
		simpleBlock(ModBlocks.conjured, models().withExistingParent(ForgeRegistries.BLOCKS.getKey(ModBlocks.conjured).getPath(), "block/block").texture("particle", location("blocks/empty")));
	}

	@Nonnull
	@Override
	public String getName() {
		return "Psi blockstates and block models";
	}
}
