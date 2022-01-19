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

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import vazkii.psi.api.spell.piece.PieceCraftingTrick;

import javax.annotation.Nullable;

public class DimensionTrickRecipe extends TrickRecipe {
	public static final IRecipeSerializer<DimensionTrickRecipe> SERIALIZER = new Serializer();
	private final RegistryKey<World> dimensionKey;

	public DimensionTrickRecipe(ResourceLocation id, @Nullable PieceCraftingTrick piece, Ingredient input, ItemStack output, ItemStack cad, RegistryKey<World> dimensionKey) {
		super(id, piece, input, output, cad);
		this.dimensionKey = dimensionKey;
	}

	public DimensionTrickRecipe(ResourceLocation id, @Nullable PieceCraftingTrick piece, Ingredient input, ItemStack output, ItemStack cad, ResourceLocation dimensionKey) {
		this(id, piece, input, output, cad, RegistryKey.create(Registry.DIMENSION_REGISTRY, dimensionKey));
	}

	@Override
	public boolean matches(RecipeWrapper inv, World world) {
		return super.matches(inv, world) && world.dimension() == dimensionKey;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<DimensionTrickRecipe> {
		@Override
		public DimensionTrickRecipe fromJson(ResourceLocation id, JsonObject json) {
			TrickRecipe recipe = TrickRecipe.SERIALIZER.fromJson(id, json);
			ResourceLocation dimensionId = new ResourceLocation(JSONUtils.getAsString(json, "dimension"));
			return new DimensionTrickRecipe(id, recipe.getPiece(), recipe.getInput(), recipe.getResultItem(), recipe.getAssembly(), dimensionId);
		}

		@Nullable
		@Override
		public DimensionTrickRecipe fromNetwork(ResourceLocation id, PacketBuffer buf) {
			TrickRecipe recipe = TrickRecipe.SERIALIZER.fromNetwork(id, buf);
			ResourceLocation dimensionId = buf.readResourceLocation();
			return new DimensionTrickRecipe(id, recipe.getPiece(), recipe.getInput(), recipe.getResultItem(), recipe.getAssembly(), dimensionId);
		}

		@Override
		public void toNetwork(PacketBuffer buf, DimensionTrickRecipe recipe) {
			TrickRecipe.SERIALIZER.toNetwork(buf, recipe);
			buf.writeResourceLocation(recipe.dimensionKey.location());
		}
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

}
