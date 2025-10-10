/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.lib.LibMisc;

import static vazkii.psi.common.Psi.location;

public class PsiBlockModelGenerator extends BlockStateProvider {
	public PsiBlockModelGenerator(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, LibMisc.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlock(ModBlocks.psidustBlock.get(), models().cubeAll(BuiltInRegistries.BLOCK.getKey(ModBlocks.psidustBlock.get()).getPath(), location("block/psidust_block")));
		simpleBlock(ModBlocks.psimetalBlock.get(), models().cubeAll(BuiltInRegistries.BLOCK.getKey(ModBlocks.psimetalBlock.get()).getPath(), location("block/psimetal_block")));
		simpleBlock(ModBlocks.psigemBlock.get(), models().cubeAll(BuiltInRegistries.BLOCK.getKey(ModBlocks.psigemBlock.get()).getPath(), location("block/psigem_block")));
		simpleBlock(ModBlocks.psimetalPlateBlack.get(), models().cubeAll(BuiltInRegistries.BLOCK.getKey(ModBlocks.psimetalPlateBlack.get()).getPath(), location("block/psimetal_plate_black")));
		simpleBlock(ModBlocks.psimetalPlateWhite.get(), models().cubeAll(BuiltInRegistries.BLOCK.getKey(ModBlocks.psimetalPlateWhite.get()).getPath(), location("block/psimetal_plate_white")));
		simpleBlock(ModBlocks.psimetalPlateBlackLight.get(), models().cubeBottomTop(BuiltInRegistries.BLOCK.getKey(ModBlocks.psimetalPlateBlackLight.get()).getPath(),
				location("block/psimetal_plate_black_light"), location("block/psimetal_plate_black"), location("block/psimetal_plate_black")));
		simpleBlock(ModBlocks.psimetalPlateWhiteLight.get(), models().cubeBottomTop(BuiltInRegistries.BLOCK.getKey(ModBlocks.psimetalPlateWhiteLight.get()).getPath(),
				location("block/psimetal_plate_white_light"), location("block/psimetal_plate_white"), location("block/psimetal_plate_white")));
		simpleBlock(ModBlocks.psimetalEbony.get(), models().cubeAll(BuiltInRegistries.BLOCK.getKey(ModBlocks.psimetalEbony.get()).getPath(), location("block/ebony_psimetal_block")));
		simpleBlock(ModBlocks.psimetalIvory.get(), models().cubeAll(BuiltInRegistries.BLOCK.getKey(ModBlocks.psimetalIvory.get()).getPath(), location("block/ivory_psimetal_block")));
		simpleBlock(ModBlocks.conjured.get(), models().withExistingParent(BuiltInRegistries.BLOCK.getKey(ModBlocks.conjured.get()).getPath(), "block/block").texture("particle", location("block/empty")));
	}

	@NotNull
	@Override
	public String getName() {
		return "Psi blockstates and block models";
	}
}
