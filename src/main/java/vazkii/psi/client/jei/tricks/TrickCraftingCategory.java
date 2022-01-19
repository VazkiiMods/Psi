/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.jei.tricks;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import vazkii.psi.api.ClientPsiAPI;
import vazkii.psi.api.recipe.ITrickRecipe;
import vazkii.psi.common.Psi;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrickCraftingCategory implements IRecipeCategory<ITrickRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(LibMisc.MOD_ID, "trick");

	private static final int INPUT_SLOT = 0;
	private static final int CAD_SLOT = 1;
	private static final int OUTPUT_SLOT = 2;

	private static final int trickX = 43;
	private static final int trickY = 24;

	private final Map<ResourceLocation, IDrawable> trickIcons = new HashMap<>();

	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawable programmerHover;

	private final IGuiHelper helper;

	public TrickCraftingCategory(IGuiHelper helper) {
		this.helper = helper;
		background = helper.createDrawable(new ResourceLocation(LibMisc.MOD_ID, "textures/gui/jei/trick.png"), 0, 0, 96, 41);
		icon = helper.createDrawableIngredient(new ItemStack(ModItems.psidust));
		programmerHover = helper.createDrawable(new ResourceLocation("psi", "textures/gui/programmer.png"), 16, 184, 16, 16);
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends ITrickRecipe> getRecipeClass() {
		return ITrickRecipe.class;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return I18n.get("jei." + LibMisc.MOD_ID + ".category.trick");
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Nonnull
	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(ITrickRecipe recipe, IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
				ImmutableList.copyOf(recipe.getInput().getItems()), ImmutableList.of(recipe.getAssembly())
		));
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
	}

	@Override
	public void draw(ITrickRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		if (recipe.getPiece() != null) {
			IDrawable trickIcon = trickIcons.computeIfAbsent(recipe.getPiece().registryKey,
					key -> {
						RenderMaterial mat = ClientPsiAPI.getSpellPieceMaterial(key);
						if (mat == null) {
							Psi.logger.warn("Not rendering complex (or missing) render for {}", key);
							return helper.createBlankDrawable(16, 16);
						}
						return new DrawableTAS(mat.sprite());
					});

			trickIcon.draw(matrixStack, trickX, trickY);

			if (onTrick(mouseX, mouseY)) {
				programmerHover.draw(matrixStack, trickX, trickY);
			}
		}
	}

	@Nonnull
	@Override
	public List<ITextComponent> getTooltipStrings(ITrickRecipe recipe, double mouseX, double mouseY) {
		if (recipe.getPiece() != null && onTrick(mouseX, mouseY)) {
			List<ITextComponent> tooltip = new ArrayList<>();
			recipe.getPiece().getTooltip(tooltip);
			return tooltip;
		}
		return Collections.emptyList();
	}

	private static boolean onTrick(double mouseX, double mouseY) {
		return (mouseX >= trickX && mouseX <= trickX + 16 && mouseY >= trickY && mouseY <= trickY + 16);
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull ITrickRecipe recipe, @Nonnull IIngredients ingredients) {
		recipeLayout.getItemStacks().init(INPUT_SLOT, true, 0, 5);
		recipeLayout.getItemStacks().init(CAD_SLOT, true, 21, 23);
		recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 73, 5);

		recipeLayout.getItemStacks().set(ingredients);
	}
}
