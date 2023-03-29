/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

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

	public void build(Consumer<FinishedRecipe> consumer) {
		this.build(consumer, ForgeRegistries.ITEMS.getKey(output.getItem()));
	}

	public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
		consumer.accept(new Result(id, output, input, cadAssembly, trick));
	}

	public static class Result implements FinishedRecipe {
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
		public void serializeRecipeData(@Nonnull JsonObject json) {
			json.add("input", input.toJson());
			json.add("output", serializeStack(output));
			json.add("cad", serializeStack(cadAssembly));
			if (trick != null) {
				json.addProperty("trick", trick.toString());
			}
		}

		@Nonnull
		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Nonnull
		@Override
		public RecipeSerializer<?> getType() {
			return Objects.requireNonNull(ForgeRegistries.RECIPE_SERIALIZERS.getValue(ITrickRecipe.TYPE_ID));
		}

		@Nullable
		@Override
		public JsonObject serializeAdvancement() {
			return null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementId() {
			return null;
		}
	}

	// https://github.com/Vazkii/Botania/blob/39fd0ed3b885a31720bd402996e06c94b5823b01/src/main/java/vazkii/botania/common/core/helper/ItemNBTHelper.java#L158
	/**
	 * Serializes the given stack such that {@link net.minecraftforge.common.crafting.CraftingHelper#getItemStack}
	 * would be able to read the result back
	 */
	private static JsonObject serializeStack(ItemStack stack) {
		CompoundTag nbt = stack.save(new CompoundTag());
		byte c = nbt.getByte("Count");
		if (c != 1) {
			nbt.putByte("count", c);
		}
		nbt.remove("Count");
		renameTag(nbt, "id", "item");
		renameTag(nbt, "tag", "nbt");
		Dynamic<Tag> dyn = new Dynamic<>(NbtOps.INSTANCE, nbt);
		return dyn.convert(JsonOps.INSTANCE).getValue().getAsJsonObject();
	}

	private static void renameTag(CompoundTag nbt, String oldName, String newName) {
		Tag tag = nbt.get(oldName);
		if (tag != null) {
			nbt.remove(oldName);
			nbt.put(newName, tag);
		}
	}

}
