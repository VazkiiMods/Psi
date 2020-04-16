package vazkii.psi.common.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;

import javax.annotation.Nullable;

public class DimensionTrickRecipe extends TrickRecipe {
	public static final IRecipeSerializer<DimensionTrickRecipe> SERIALIZER = new Serializer();
	private final ResourceLocation dimensionId;

	public DimensionTrickRecipe(ResourceLocation id, @Nullable PieceCraftingTrick piece, Ingredient input, ItemStack output, ItemStack cad, ResourceLocation dimensionId) {
		super(id, piece, input, output, cad);
		this.dimensionId = dimensionId;
	}

	@Override
	public boolean matches(RecipeWrapper inv, World world) {
		return super.matches(inv, world) && dimensionId.equals(world.getDimension().getType().getRegistryName());
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<DimensionTrickRecipe> {
		@Override
		public DimensionTrickRecipe read(ResourceLocation id, JsonObject json) {
			TrickRecipe recipe = TrickRecipe.SERIALIZER.read(id, json);
			ResourceLocation dimensionId = new ResourceLocation(JSONUtils.getString(json, "dimension"));
			return new DimensionTrickRecipe(id, recipe.getPiece(), recipe.getInput(), recipe.getRecipeOutput(), recipe.getAssembly(), dimensionId);
		}

		@Nullable
		@Override
		public DimensionTrickRecipe read(ResourceLocation id, PacketBuffer buf) {
			TrickRecipe recipe = TrickRecipe.SERIALIZER.read(id, buf);
			ResourceLocation dimensionId = buf.readResourceLocation();
			return new DimensionTrickRecipe(id, recipe.getPiece(), recipe.getInput(), recipe.getRecipeOutput(), recipe.getAssembly(), dimensionId);
		}

		@Override
		public void write(PacketBuffer buf, DimensionTrickRecipe recipe) {
			TrickRecipe.SERIALIZER.write(buf, recipe);
			buf.writeResourceLocation(recipe.dimensionId);
		}
	}
}
