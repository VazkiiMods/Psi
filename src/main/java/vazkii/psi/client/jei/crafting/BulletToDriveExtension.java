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
import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICustomCraftingCategoryExtension;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.common.crafting.recipe.BulletToDriveRecipe;
import vazkii.psi.common.item.ItemSpellBullet;
import vazkii.psi.common.item.ItemSpellDrive;
import vazkii.psi.common.item.base.ModItems;

import java.util.List;

public class BulletToDriveExtension implements ICustomCraftingCategoryExtension {
	private final List<Ingredient> inputs;
	private final BulletToDriveRecipe recipe;

	public BulletToDriveExtension(BulletToDriveRecipe recipe) {
		this.recipe = recipe;

		Item[] bullets = Registry.ITEM.stream()
				.filter(item -> item instanceof ItemSpellBullet)
				.toArray(Item[]::new);
		inputs = ImmutableList.of(
				Ingredient.of(ModItems.spellDrive),
				Ingredient.of(bullets));
	}

	@Override
	public void setIngredients(IIngredients ingredients) {
		ingredients.setInputIngredients(inputs);
		ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(ModItems.spellDrive));
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IIngredients ingredients) {
		recipeLayout.setShapeless();
		recipeLayout.getItemStacks().set(ingredients);

		IFocus<ItemStack> focus = recipeLayout.getFocus(VanillaTypes.ITEM);
		if (focus != null) {
			ItemStack stack = focus.getValue();

			if (stack.getItem() instanceof ItemSpellBullet && ISpellAcceptor.hasSpell(stack)) {
				ItemStack drive = new ItemStack(ModItems.spellDrive);
				ItemSpellDrive.setSpell(drive, ISpellAcceptor.acceptor(stack).getSpell());
				recipeLayout.getItemStacks().set(0, drive);
				recipeLayout.getItemStacks().set(2, stack.copy());
			}
		}
	}

	@Override
	public void drawInfo(int recipeWidth, int recipeHeight, PoseStack matrixStack, double mouseX, double mouseY) {
		Minecraft.getInstance().font.draw(matrixStack, I18n.get("jei.psi.spell_copy"), 57, 46, 0x808080);
		//RenderSystem.enableAlphaTest(); // Prevents state leak affecting the shapeless icon
	}

	@Override
	public ResourceLocation getRegistryName() {
		return recipe.getId();
	}
}
