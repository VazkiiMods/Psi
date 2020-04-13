package vazkii.psi.api.recipe;

import com.google.gson.JsonObject;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.psi.common.crafting.recipe.TrickRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Json recipe builder for Psi trick recipes.
 */
public class TrickRecipeBuilder {
	private Ingredient input;
	private ItemStack output;
	private ItemStack cadAssembly;
	private ResourceLocation trick;

	private TrickRecipeBuilder(ItemStack output) {
		this.output = output;
	}

	public static TrickRecipeBuilder of(ItemStack output) {
		output.setCount(1);
		return new TrickRecipeBuilder(output);
	}

	public static TrickRecipeBuilder of(IItemProvider output) {
		return new TrickRecipeBuilder(new ItemStack(output.asItem()));
	}

	public TrickRecipeBuilder input(Ingredient input) {
		this.input = input;
		return this;
	}

	public TrickRecipeBuilder input(ItemStack... input) {
		this.input = Ingredient.fromStacks(input);
		return this;
	}

	public TrickRecipeBuilder input(Tag<Item> input) {
		this.input = Ingredient.fromTag(input);
		return this;
	}

	public TrickRecipeBuilder input(IItemProvider... input) {
		this.input = Ingredient.fromItems(input);
		return this;
	}

	public TrickRecipeBuilder cad(IItemProvider input) {
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

	public void build(Consumer<IFinishedRecipe> consumer) {
		this.build(consumer, output.getItem().getRegistryName());
	}

	public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
		consumer.accept(new Result(id, output, input, cadAssembly, trick));
	}

	public static class Result implements IFinishedRecipe {
		private final Ingredient input;
		private final ItemStack output;
		private final ItemStack cadAssembly;
		private final ResourceLocation trick;
		private final ResourceLocation id;

		protected Result(ResourceLocation id, TrickRecipeBuilder builder) {
			this(id, builder.output, builder.input, builder.cadAssembly, builder.trick);
		}

		protected Result(ResourceLocation id, ItemStack output, Ingredient input, ItemStack cadAssembly, ResourceLocation trick) {
			this.input = input;
			this.output = output;
			this.cadAssembly = cadAssembly;
			this.trick = trick;
			this.id = id;
		}

		@Override
		public void serialize(@Nonnull JsonObject json) {
			json.add("input", input.serialize());
			json.add("output", serializeStack(output));
			json.add("cad", serializeStack(cadAssembly));
			if (trick != null) {
				json.addProperty("trick", trick.toString());
			}
		}

		@Nonnull
		@Override
		public ResourceLocation getID() {
			return id;
		}

		@Nonnull
		@Override
		public IRecipeSerializer<?> getSerializer() {
			return Objects.requireNonNull(ForgeRegistries.RECIPE_SERIALIZERS.getValue(TrickRecipe.TYPE_ID));
		}

		@Nullable
		@Override
		public JsonObject getAdvancementJson() {
			return null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementID() {
			return null;
		}
	}

	// https://github.com/Vazkii/Botania/blob/39fd0ed3b885a31720bd402996e06c94b5823b01/src/main/java/vazkii/botania/common/core/helper/ItemNBTHelper.java#L158
	/**
	 * Serializes the given stack such that {@link net.minecraftforge.common.crafting.CraftingHelper#getItemStack}
	 * would be able to read the result back
	 */
	private static JsonObject serializeStack(ItemStack stack) {
		CompoundNBT nbt = stack.write(new CompoundNBT());
		byte c = nbt.getByte("Count");
		if (c != 1) {
			nbt.putByte("count", c);
		}
		nbt.remove("Count");
		renameTag(nbt, "id", "item");
		renameTag(nbt, "tag", "nbt");
		Dynamic<INBT> dyn = new Dynamic<>(NBTDynamicOps.INSTANCE, nbt);
		return dyn.convert(JsonOps.INSTANCE).getValue().getAsJsonObject();
	}

	private static void renameTag(CompoundNBT nbt, String oldName, String newName) {
		INBT tag = nbt.get(oldName);
		if (tag != null) {
			nbt.remove(oldName);
			nbt.put(newName, tag);
		}
	}

}
