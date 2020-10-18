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

import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;

import static vazkii.psi.common.Psi.location;

public class BlockModels extends BlockStateProvider {
	public BlockModels(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, LibMisc.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlock(ModBlocks.psidustBlock, models().cubeAll(ModBlocks.psidustBlock.getRegistryName().getPath(), location("blocks/psidust_block")));
		simpleBlock(ModBlocks.psimetalBlock, models().cubeAll(ModBlocks.psimetalBlock.getRegistryName().getPath(), location("blocks/psimetal_block")));
		simpleBlock(ModBlocks.psigemBlock, models().cubeAll(ModBlocks.psigemBlock.getRegistryName().getPath(), location("blocks/psigem_block")));
		simpleBlock(ModBlocks.psimetalPlateBlack, models().cubeAll(ModBlocks.psimetalPlateBlack.getRegistryName().getPath(), location("blocks/psimetal_plate_black")));
		simpleBlock(ModBlocks.psimetalPlateWhite, models().cubeAll(ModBlocks.psimetalPlateWhite.getRegistryName().getPath(), location("blocks/psimetal_plate_white")));
		simpleBlock(ModBlocks.psimetalPlateBlackLight, models().cubeBottomTop(ModBlocks.psimetalPlateBlackLight.getRegistryName().getPath(),
				location("blocks/psimetal_plate_black_light"), location("blocks/psimetal_plate_black"), location("blocks/psimetal_plate_black")));
		simpleBlock(ModBlocks.psimetalPlateWhiteLight, models().cubeBottomTop(ModBlocks.psimetalPlateWhiteLight.getRegistryName().getPath(),
				location("blocks/psimetal_plate_white_light"), location("blocks/psimetal_plate_white"), location("blocks/psimetal_plate_white")));
		simpleBlock(ModBlocks.psimetalEbony, models().cubeAll(ModBlocks.psimetalEbony.getRegistryName().getPath(), location("blocks/ebony_psimetal_block")));
		simpleBlock(ModBlocks.psimetalIvory, models().cubeAll(ModBlocks.psimetalIvory.getRegistryName().getPath(), location("blocks/ivory_psimetal_block")));
		simpleBlock(ModBlocks.conjured, models().withExistingParent(ModBlocks.conjured.getRegistryName().getPath(), "block/block").texture("particle", location("blocks/empty")));
	}

	@Nonnull
	@Override
	public String getName() {
		return "Psi blockstates and block models";
	}
}
