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
import net.minecraft.world.level.block.Block;

import vazkii.psi.common.PsiMod;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.block.base.ModConjuredBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PsiBlockModelGenerator implements DataProvider {
	private final PackOutput.PathProvider blockStatePath;
	private final PackOutput.PathProvider modelPath;

	public PsiBlockModelGenerator(PackOutput output) {
		blockStatePath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
		modelPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		List<CompletableFuture<?>> saves = new ArrayList<>();
		cubeAll(output, saves, ModBlocks.psidustBlock.get(), "psidust_block");
		cubeAll(output, saves, ModBlocks.psimetalBlock.get(), "psimetal_block");
		cubeAll(output, saves, ModBlocks.psigemBlock.get(), "psigem_block");
		cubeAll(output, saves, ModBlocks.psimetalPlateBlack.get(), "psimetal_plate_black");
		cubeAll(output, saves, ModBlocks.psimetalPlateWhite.get(), "psimetal_plate_white");
		cubeBottomTop(output, saves, ModBlocks.psimetalPlateBlackLight.get(),
				"psimetal_plate_black_light", "psimetal_plate_black", "psimetal_plate_black");
		cubeBottomTop(output, saves, ModBlocks.psimetalPlateWhiteLight.get(),
				"psimetal_plate_white_light", "psimetal_plate_white", "psimetal_plate_white");
		cubeAll(output, saves, ModBlocks.psimetalEbony.get(), "ebony_psimetal_block");
		cubeAll(output, saves, ModBlocks.psimetalIvory.get(), "ivory_psimetal_block");
		conjured(output, saves, ModConjuredBlock.BLOCK.get());
		return CompletableFuture.allOf(saves.toArray(CompletableFuture[]::new));
	}

	private void cubeAll(CachedOutput output, List<CompletableFuture<?>> saves, Block block, String texture) {
		ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
		model(output, saves, id, "minecraft:block/cube_all", "all", PsiMod.location("block/" + texture));
		blockState(output, saves, id);
	}

	private void cubeBottomTop(CachedOutput output, List<CompletableFuture<?>> saves, Block block,
			String side, String bottom, String top) {
		ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
		JsonObject textures = new JsonObject();
		textures.addProperty("bottom", PsiMod.location("block/" + bottom).toString());
		textures.addProperty("side", PsiMod.location("block/" + side).toString());
		textures.addProperty("top", PsiMod.location("block/" + top).toString());
		model(output, saves, id, "minecraft:block/cube_bottom_top", textures);
		blockState(output, saves, id);
	}

	private void conjured(CachedOutput output, List<CompletableFuture<?>> saves, Block block) {
		ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
		model(output, saves, id, "minecraft:block/block", "particle", PsiMod.location("block/empty"));
		blockState(output, saves, id);
	}

	private void model(CachedOutput output, List<CompletableFuture<?>> saves, ResourceLocation id,
			String parent, String textureKey, ResourceLocation texture) {
		JsonObject textures = new JsonObject();
		textures.addProperty(textureKey, texture.toString());
		model(output, saves, id, parent, textures);
	}

	private void model(CachedOutput output, List<CompletableFuture<?>> saves, ResourceLocation id,
			String parent, JsonObject textures) {
		JsonObject model = new JsonObject();
		model.addProperty("parent", parent);
		model.add("textures", textures);
		saves.add(DataProvider.saveStable(output, model, modelPath.json(id.withPrefix("block/"))));
	}

	private void blockState(CachedOutput output, List<CompletableFuture<?>> saves, ResourceLocation id) {
		JsonObject variant = new JsonObject();
		variant.addProperty("model", id.withPrefix("block/").toString());
		JsonObject variants = new JsonObject();
		variants.add("", variant);
		JsonObject blockState = new JsonObject();
		blockState.add("variants", variants);
		saves.add(DataProvider.saveStable(output, blockState, blockStatePath.json(id)));
	}

	@Override
	public String getName() {
		return "Psi blockstates and block models";
	}
}
