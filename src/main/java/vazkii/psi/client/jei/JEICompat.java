/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [21/12/2018, 18:30:00 (GMT)]
 */
package vazkii.psi.client.jei;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.recipe.TrickRecipe;
import vazkii.psi.client.jei.tricks.TrickCraftingCategory;
import vazkii.psi.client.jei.tricks.TrickCraftingRecipeJEI;
import vazkii.psi.common.item.base.ModItems;

@JEIPlugin
public class JEICompat implements IModPlugin {

	public static IJeiHelpers helpers;

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		helpers = registry.getJeiHelpers();
		registry.addRecipeCategories(TrickCraftingCategory.INSTANCE);
	}

	@Override
	public void register(IModRegistry registry) {
		helpers = registry.getJeiHelpers();

		registry.handleRecipes(TrickRecipe.class, TrickCraftingRecipeJEI::new, TrickCraftingCategory.INSTANCE.getUid());
		registry.addRecipes(PsiAPI.trickRecipes, TrickCraftingCategory.INSTANCE.getUid());

		NonNullList<ItemStack> stacks = NonNullList.create();
		ModItems.cad.getSubItems(CreativeTabs.SEARCH, stacks);
		for (ItemStack stack : stacks)
			registry.addRecipeCatalyst(stack, TrickCraftingCategory.INSTANCE.getUid());
	}
}
