/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [21/12/2018, 18:33:16 (GMT)]
 */
package vazkii.psi.client.jei.tricks;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.recipe.TrickRecipe;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class TrickCraftingCategory implements IRecipeCategory<TrickRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(LibMisc.MOD_ID, "trick");

	private static final int INPUT_SLOT = 0;
	private static final int CAD_SLOT = 1;
	private static final int OUTPUT_SLOT = 2;

	private static final int trickX = 43;
	private static final int trickY = 24;

	private final Map<ResourceLocation, IDrawable> trickIcons = new HashMap<>(); //TODO Switch to ResLoc keys once that gets fixed up

	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawable programmerHover;

	private final IGuiHelper helper;

	public TrickCraftingCategory(IGuiHelper helper) {
		this.helper = helper;
		background = helper.createDrawable(new ResourceLocation(LibMisc.MOD_ID, "textures/gui/jei/trick.png"), 0, 0, 96, 41);
		icon = helper.createDrawableIngredient(new ItemStack(ModItems.cad));
		programmerHover = helper.createDrawable(new ResourceLocation("psi", "textures/gui/programmer.png"), 16, 184, 16, 16);
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends TrickRecipe> getRecipeClass() {
		return TrickRecipe.class;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return I18n.format("jei." + LibMisc.MOD_ID + ".category.trick");
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
	public void setIngredients(TrickRecipe recipe, IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, ImmutableList.of(
			ImmutableList.copyOf(recipe.getInput().getMatchingStacks()), ImmutableList.of(recipe.getCAD())
		));
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
	}

	@Override
	public void draw(TrickRecipe recipe, double mouseX, double mouseY) {
		if (recipe.getPiece() != null) {
			IDrawable trickIcon = trickIcons.computeIfAbsent(recipe.getPiece().registryKey, 
				key -> helper.createDrawable(PsiAPI.simpleSpellTextures.get(key), 0, 0, 256, 256));
			
			RenderSystem.pushMatrix();
			RenderSystem.scalef(0.0625f, 0.0625f, 0.0625f);
			trickIcon.draw(trickX * 16, trickY * 16);
			RenderSystem.color3f(1f, 1f, 1f);
			RenderSystem.popMatrix();

			if (onTrick(mouseX, mouseY))
				programmerHover.draw(trickX, trickY);
		}
	}

	@Nonnull
	@Override
	public List<String> getTooltipStrings(TrickRecipe recipe, double mouseX, double mouseY) {
		if (recipe.getPiece() != null && onTrick(mouseX, mouseY)) {
			List<ITextComponent> tooltip = new ArrayList<>();
			recipe.getPiece().getTooltip(tooltip);
			return tooltip.stream()
				.map(ITextComponent::getFormattedText)
				.collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	private static boolean onTrick(double mouseX, double mouseY) {
		return (mouseX >= trickX && mouseX <= trickX + 16 && mouseY >= trickY && mouseY <= trickY + 16);
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull TrickRecipe recipe, @Nonnull IIngredients ingredients) {
		recipeLayout.getItemStacks().init(INPUT_SLOT, true, 0, 5);
		recipeLayout.getItemStacks().init(CAD_SLOT, true, 21, 23);
		recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 73, 5);

		recipeLayout.getItemStacks().set(ingredients);
	}
}
