/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.jei.tricks;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.recipe.ITrickRecipe;
import vazkii.psi.common.Psi;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrickCraftingCategory implements IRecipeCategory<ITrickRecipe> {
	public static final RecipeType<ITrickRecipe> TYPE = RecipeType.create(LibMisc.MOD_ID, "trick", ITrickRecipe.class);

	private static final int trickX = 43;
	private static final int trickY = 24;

	private final Map<ResourceLocation, IDrawable> trickIcons = new HashMap<>();

	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawable programmerHover;

	private final IGuiHelper helper;

	public TrickCraftingCategory(IGuiHelper helper) {
		this.helper = helper;
		background = helper.createDrawable(Psi.location("textures/gui/jei/trick.png"), 0, 0, 96, 41);
		icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.psidust));
		programmerHover = helper.createDrawable(Psi.location("textures/gui/programmer.png"), 16, 184, 16, 16);
	}

	private static boolean onTrick(double mouseX, double mouseY) {
		return (mouseX >= trickX && mouseX <= trickX + 16 && mouseY >= trickY && mouseY <= trickY + 16);
	}

	@Override
	public RecipeType<ITrickRecipe> getRecipeType() {
		return TYPE;
	}

	@NotNull
	@Override
	public Component getTitle() {
		return Component.literal(I18n.get("jei." + LibMisc.MOD_ID + ".category.trick"));
	}

	@NotNull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@NotNull
	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void draw(ITrickRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		if(recipe.getPiece() != null) {
			IDrawable trickIcon = trickIcons.computeIfAbsent(recipe.getPiece().registryKey,
					key -> new DrawablePiece(recipe.getPiece()));

			trickIcon.draw(guiGraphics, trickX, trickY);

			if(onTrick(mouseX, mouseY)) {
				programmerHover.draw(guiGraphics, trickX, trickY);
			}
		}
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, ITrickRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if(recipe.getPiece() != null && onTrick(mouseX, mouseY)) {
			List<Component> tooltips = new ArrayList<>();
			recipe.getPiece().getTooltip(tooltips);
			tooltip.addAll(tooltips);
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ITrickRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 6).addIngredients(recipe.getInput());
		builder.addSlot(RecipeIngredientRole.CATALYST, 22, 24).addItemStack(recipe.getAssembly());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 74, 6).addItemStack(recipe.getResultItem(RegistryAccess.EMPTY));
	}
}
