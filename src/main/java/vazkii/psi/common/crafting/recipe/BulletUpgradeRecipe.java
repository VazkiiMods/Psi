/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.crafting.recipe;

import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

import vazkii.psi.common.item.ItemSpellBullet;

public class BulletUpgradeRecipe implements CraftingRecipe {
	public static final RecipeSerializer<BulletUpgradeRecipe> SERIALIZER = new Serializer();

	private final ShapelessRecipe compose;

	public BulletUpgradeRecipe(ShapelessRecipe compose) {
		this.compose = compose;
	}

	@Override
	public boolean matches(CraftingContainer inv, Level worldIn) {
		return compose.matches(inv, worldIn);
	}

	@Override
	public ItemStack assemble(CraftingContainer inv) {
		ItemStack output = compose.assemble(inv);
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (stack.getItem() instanceof ItemSpellBullet) {
				output.setTag(stack.getTag());
			}
		}
		return output;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return compose.canCraftInDimensions(width, height);
	}

	@Override
	public ItemStack getResultItem() {
		return compose.getResultItem();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
		return compose.getRemainingItems(inv);
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return compose.getIngredients();
	}

	@Override
	public boolean isSpecial() {
		return compose.isSpecial();
	}

	@Override
	public String getGroup() {
		return compose.getGroup();
	}

	@Override
	public ItemStack getToastSymbol() {
		return compose.getToastSymbol();
	}

	@Override
	public ResourceLocation getId() {
		return compose.getId();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	private static class Serializer implements RecipeSerializer<BulletUpgradeRecipe> {
		@Override
		public BulletUpgradeRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			return new BulletUpgradeRecipe(SHAPELESS_RECIPE.fromJson(recipeId, json));
		}

		@Override
		public BulletUpgradeRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			return new BulletUpgradeRecipe(SHAPELESS_RECIPE.fromNetwork(recipeId, buffer));
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, BulletUpgradeRecipe recipe) {
			SHAPELESS_RECIPE.toNetwork(buffer, recipe.compose);
		}
	}

}
