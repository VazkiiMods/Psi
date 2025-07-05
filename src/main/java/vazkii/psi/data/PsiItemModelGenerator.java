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
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.lib.LibMisc;

public class PsiItemModelGenerator extends ItemModelProvider {
	public PsiItemModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, LibMisc.MOD_ID, existingFileHelper);
	}

	private void pointToBlock(Item item) {
		String name = BuiltInRegistries.ITEM.getKey(item).getPath();
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

	@NotNull
	@Override
	public String getName() {
		return "Psi item models";
	}
}
