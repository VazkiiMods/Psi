/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import com.google.gson.JsonObject;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

import javax.annotation.Nullable;

import java.util.function.Consumer;

/**
 * Wrapper over a generated recipe to transform it. See Botania's data generators for more examples.
 */
public class WrapperResult implements FinishedRecipe {
	private final FinishedRecipe delegate;
	@Nullable
	private final RecipeSerializer<?> type;
	@Nullable
	private final Consumer<JsonObject> transform;

	/**
	 * Wraps recipe consumer with one that swaps the recipe type to a different one.
	 */
	public static Consumer<FinishedRecipe> ofType(RecipeSerializer<?> type, Consumer<FinishedRecipe> parent) {
		return recipe -> parent.accept(new WrapperResult(recipe, type, null));
	}

	/**
	 * Transforms the resulting recipe json with the specified action, eg. adding NBT to an item result.
	 */
	public static Consumer<FinishedRecipe> transformJson(Consumer<FinishedRecipe> parent, Consumer<JsonObject> transform) {
		return recipe -> parent.accept(new WrapperResult(recipe, null, transform));
	}

	private WrapperResult(FinishedRecipe delegate, @Nullable RecipeSerializer<?> type, @Nullable Consumer<JsonObject> transform) {
		this.delegate = delegate;
		this.type = type;
		this.transform = transform;
	}

	@Override
	public void serializeRecipeData(JsonObject json) {
		delegate.serializeRecipeData(json);
		if (transform != null) {
			transform.accept(json);
		}
	}

	@Override
	public JsonObject serializeRecipe() {
		if (type == null) {
			return FinishedRecipe.super.serializeRecipe();
		}
		JsonObject jsonobject = new JsonObject();
		jsonobject.addProperty("type", Registry.RECIPE_SERIALIZER.getKey(this.type).toString());
		this.serializeRecipeData(jsonobject);
		return jsonobject;
	}

	@Override
	public ResourceLocation getId() {
		return delegate.getId();
	}

	@Override
	public RecipeSerializer<?> getType() {
		return type != null ? type : delegate.getType();
	}

	@Nullable
	@Override
	public JsonObject serializeAdvancement() {
		return delegate.serializeAdvancement();
	}

	@Nullable
	@Override
	public ResourceLocation getAdvancementId() {
		return delegate.getAdvancementId();
	}
}
