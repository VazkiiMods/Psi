/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.*;

import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import vazkii.psi.client.jei.crafting.BulletToDriveExtension;
import vazkii.psi.client.jei.crafting.DriveDuplicateExtension;
import vazkii.psi.client.jei.tricks.TrickCraftingCategory;
import vazkii.psi.common.crafting.ModCraftingRecipes;
import vazkii.psi.common.crafting.recipe.BulletToDriveRecipe;
import vazkii.psi.common.crafting.recipe.DriveDuplicateRecipe;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;

@JeiPlugin
public class JEICompat implements IModPlugin {
	private static final ResourceLocation UID = new ResourceLocation(LibMisc.MOD_ID, "main");
	public static IJeiHelpers helpers;

	@Nonnull
	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		helpers = registry.getJeiHelpers();
		registry.addRecipeCategories(new TrickCraftingCategory(helpers.getGuiHelper()));
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
		registration.getCraftingCategory().addCategoryExtension(BulletToDriveRecipe.class, BulletToDriveExtension::new);
		registration.getCraftingCategory().addCategoryExtension(DriveDuplicateRecipe.class, DriveDuplicateExtension::new);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(TrickCraftingCategory.TYPE, Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ModCraftingRecipes.TRICK_RECIPE_TYPE));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		NonNullList<ItemStack> stacks = NonNullList.create();
		ModItems.cad.fillItemCategory(CreativeModeTab.TAB_SEARCH, stacks);
		for(ItemStack stack : stacks) {
			registration.addRecipeCatalyst(stack, TrickCraftingCategory.TYPE);
		}
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.useNbtForSubtypes(ModItems.cad);
	}
}
