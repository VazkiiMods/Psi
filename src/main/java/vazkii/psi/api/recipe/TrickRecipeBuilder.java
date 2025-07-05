/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;
import vazkii.psi.common.crafting.recipe.DimensionTrickRecipe;
import vazkii.psi.common.crafting.recipe.TrickRecipe;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Json recipe builder for Psi trick recipes.
 */
public class TrickRecipeBuilder {
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
	private Ingredient input;
	private final ItemStack output;
	private ItemStack cadAssembly;
	private ResourceLocation trick;
	private ResourceKey<Level> dimensionKey = null;

	private TrickRecipeBuilder(ItemStack output) {
		this.output = output;
	}

	public static TrickRecipeBuilder of(ItemStack output) {
		output.setCount(1);
		return new TrickRecipeBuilder(output);
	}

	public static TrickRecipeBuilder of(ItemLike output) {
		return new TrickRecipeBuilder(new ItemStack(output.asItem()));
	}

	public TrickRecipeBuilder input(Ingredient input) {
		this.input = input;
		return this;
	}

	public TrickRecipeBuilder input(ItemStack... input) {
		this.input = Ingredient.of(input);
		return this;
	}

	public TrickRecipeBuilder input(TagKey<Item> input) {
		this.input = Ingredient.of(input);
		return this;
	}

	public TrickRecipeBuilder input(ItemLike... input) {
		this.input = Ingredient.of(input);
		return this;
	}

	public TrickRecipeBuilder cad(ItemLike input) {
		this.cadAssembly = new ItemStack(input.asItem());
		return this;
	}

	public TrickRecipeBuilder cad(ItemStack input) {
		this.cadAssembly = input;
		return this;
	}

	public TrickRecipeBuilder trick(ResourceLocation trick) {
		this.trick = trick;
		return this;
	}

	public TrickRecipeBuilder dimension(ResourceKey<Level> dimensionKey) {
		this.dimensionKey = dimensionKey;
		return this;
	}

	public TrickRecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion) {
		this.criteria.put(pName, pCriterion);
		return this;
	}

	public void build(RecipeOutput consumer) {
		this.build(consumer, BuiltInRegistries.ITEM.getKey(output.getItem()));
	}

	public void build(RecipeOutput consumer, ResourceLocation id) {
		Advancement.Builder advancement$builder = consumer.advancement()
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
				.rewards(AdvancementRewards.Builder.recipe(id))
				.requirements(AdvancementRequirements.Strategy.OR);
		this.criteria.forEach(advancement$builder::addCriterion);
		if(dimensionKey == null)
			consumer.accept(id, new TrickRecipe((PieceCraftingTrick) SpellPiece.create(trick), input, output, cadAssembly), advancement$builder.build(id.withPrefix("recipes/")));
		else
			consumer.accept(id, new DimensionTrickRecipe((PieceCraftingTrick) SpellPiece.create(trick), input, output, cadAssembly, dimensionKey), advancement$builder.build(id.withPrefix("recipes/")));
	}
}
