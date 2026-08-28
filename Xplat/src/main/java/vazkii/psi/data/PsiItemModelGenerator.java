/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import com.google.gson.JsonObject;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import vazkii.psi.common.PsiMod;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.block.base.ModConjuredBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PsiItemModelGenerator implements DataProvider {
	private final PackOutput.PathProvider modelPath;

	public PsiItemModelGenerator(PackOutput output) {
		modelPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		List<CompletableFuture<?>> saves = new ArrayList<>();
		pointToBlock(output, saves, ModBlocks.psidustBlock.get().asItem());
		pointToBlock(output, saves, ModBlocks.psimetalBlock.get().asItem());
		pointToBlock(output, saves, ModBlocks.psigemBlock.get().asItem());
		pointToBlock(output, saves, ModBlocks.psimetalPlateBlack.get().asItem());
		pointToBlock(output, saves, ModBlocks.psimetalPlateWhite.get().asItem());
		pointToBlock(output, saves, ModBlocks.psimetalPlateBlackLight.get().asItem());
		pointToBlock(output, saves, ModBlocks.psimetalPlateWhiteLight.get().asItem());
		pointToBlock(output, saves, ModBlocks.psimetalEbony.get().asItem());
		pointToBlock(output, saves, ModBlocks.psimetalIvory.get().asItem());
		pointToBlock(output, saves, ModConjuredBlock.BLOCK.get().asItem());
		return CompletableFuture.allOf(saves.toArray(CompletableFuture[]::new));
	}

	private void pointToBlock(CachedOutput output, List<CompletableFuture<?>> saves, Item item) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
		JsonObject model = new JsonObject();
		model.addProperty("parent", PsiMod.location("block/" + id.getPath()).toString());
		saves.add(DataProvider.saveStable(output, model,
				modelPath.json(PsiMod.location("item/" + id.getPath()))));
	}

	@Override
	public String getName() {
		return "Psi item models";
	}
}
