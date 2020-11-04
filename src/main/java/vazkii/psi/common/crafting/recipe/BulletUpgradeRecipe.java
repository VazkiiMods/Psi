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

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import vazkii.psi.common.item.ItemSpellBullet;

public class BulletUpgradeRecipe implements ICraftingRecipe {
	public static final IRecipeSerializer<BulletUpgradeRecipe> SERIALIZER = new Serializer();

	private final ShapelessRecipe compose;

	public BulletUpgradeRecipe(ShapelessRecipe compose) {
		this.compose = compose;
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		return compose.matches(inv, worldIn);
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		ItemStack output = compose.getCraftingResult(inv);
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.getItem() instanceof ItemSpellBullet) {
				output.setTag(stack.getTag());
			}
		}
		return output;
	}

	@Override
	public boolean canFit(int width, int height) {
		return compose.canFit(width, height);
	}

	@Override
	public ItemStack getRecipeOutput() {
		return compose.getRecipeOutput();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		return compose.getRemainingItems(inv);
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return compose.getIngredients();
	}

	@Override
	public boolean isDynamic() {
		return compose.isDynamic();
	}

	@Override
	public String getGroup() {
		return compose.getGroup();
	}

	@Override
	public ItemStack getIcon() {
		return compose.getIcon();
	}

	@Override
	public ResourceLocation getId() {
		return compose.getId();
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BulletUpgradeRecipe> {
		@Override
		public BulletUpgradeRecipe read(ResourceLocation recipeId, JsonObject json) {
			return new BulletUpgradeRecipe(CRAFTING_SHAPELESS.read(recipeId, json));
		}

		@Override
		public BulletUpgradeRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			return new BulletUpgradeRecipe(CRAFTING_SHAPELESS.read(recipeId, buffer));
		}

		@Override
		public void write(PacketBuffer buffer, BulletUpgradeRecipe recipe) {
			CRAFTING_SHAPELESS.write(buffer, recipe.compose);
		}
	}

}
