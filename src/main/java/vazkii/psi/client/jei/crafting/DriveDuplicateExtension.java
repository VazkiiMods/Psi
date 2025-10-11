/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.jei.crafting;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import vazkii.psi.common.crafting.recipe.DriveDuplicateRecipe;
import vazkii.psi.common.item.ItemSpellDrive;
import vazkii.psi.common.item.base.ModItems;

public class DriveDuplicateExtension implements ICraftingCategoryExtension<DriveDuplicateRecipe> {

	@Override
	public void setRecipe(RecipeHolder<DriveDuplicateRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper helper, IFocusGroup focuses) {
		ItemStack drive = new ItemStack(ModItems.spellDrive.get());

		focuses.getFocuses(VanillaTypes.ITEM_STACK)
				.filter(focus -> focus.getTypedValue().getIngredient().getItem() instanceof ItemSpellDrive)
				.findFirst()
				.map(focus -> focus.getTypedValue().getIngredient())
				.filter(stack -> ItemSpellDrive.getSpell(stack) != null)
				.ifPresent(stack -> ItemSpellDrive.setSpell(drive, ItemSpellDrive.getSpell(stack)));

		helper.createAndSetInputs(builder, ImmutableList.of(ImmutableList.of(drive), ImmutableList.of(new ItemStack(ModItems.spellDrive.get()))), 0, 0);
		helper.createAndSetOutputs(builder, ImmutableList.of(drive));
	}

	@Override
	public void drawInfo(RecipeHolder<DriveDuplicateRecipe> recipeHolder, int recipeWidth, int recipeHeight, GuiGraphics graphics, double mouseX, double mouseY) {
		graphics.drawString(Minecraft.getInstance().font, Component.translatable("jei.psi.spell_copy"), 57, 46, 0x808080);
		//RenderSystem.enableAlphaTest(); // Prevents state leak affecting the shapeless icon
	}
}
