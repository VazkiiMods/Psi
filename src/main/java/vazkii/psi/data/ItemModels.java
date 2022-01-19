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
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class ItemModels extends ItemModelProvider {
	public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, LibMisc.MOD_ID, existingFileHelper);
	}

	private void pointToBlock(Item item) {
		String name = item.getRegistryName().getPath();
		getBuilder(name).parent(new ModelFile.UncheckedModelFile(Psi.location("block/" + name)));
	}

	@Override
	protected void registerModels() {
		pointToBlock(ModBlocks.psidustBlock.asItem());
		pointToBlock(ModBlocks.psimetalBlock.asItem());
		pointToBlock(ModBlocks.psigemBlock.asItem());
		pointToBlock(ModBlocks.psimetalPlateBlack.asItem());
		pointToBlock(ModBlocks.psimetalPlateWhite.asItem());
		pointToBlock(ModBlocks.psimetalPlateBlackLight.asItem());
		pointToBlock(ModBlocks.psimetalPlateWhiteLight.asItem());
		pointToBlock(ModBlocks.psimetalEbony.asItem());
		pointToBlock(ModBlocks.psimetalIvory.asItem());
		pointToBlock(ModBlocks.conjured.asItem());
	}

	@Nonnull
	@Override
	public String getName() {
		return "Psi item models";
	}
}
