/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
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

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.ModBlocks;

public class PsiItemModelGenerator extends ItemModelProvider {
	public PsiItemModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, PsiAPI.MOD_ID, existingFileHelper);
	}

	private void pointToBlock(Item item) {
		String name = BuiltInRegistries.ITEM.getKey(item).getPath();
		getBuilder(name).parent(new ModelFile.UncheckedModelFile(Psi.location("block/" + name)));
	}

	@Override
	protected void registerModels() {
		pointToBlock(ModBlocks.psidustBlock.get().asItem());
		pointToBlock(ModBlocks.psimetalBlock.get().asItem());
		pointToBlock(ModBlocks.psigemBlock.get().asItem());
		pointToBlock(ModBlocks.psimetalPlateBlack.get().asItem());
		pointToBlock(ModBlocks.psimetalPlateWhite.get().asItem());
		pointToBlock(ModBlocks.psimetalPlateBlackLight.get().asItem());
		pointToBlock(ModBlocks.psimetalPlateWhiteLight.get().asItem());
		pointToBlock(ModBlocks.psimetalEbony.get().asItem());
		pointToBlock(ModBlocks.psimetalIvory.get().asItem());
		pointToBlock(ModBlocks.conjured.get().asItem());
	}

	@NotNull
	@Override
	public String getName() {
		return "Psi item models";
	}
}
