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

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.client.jei.tricks.TrickCraftingCategory;
import vazkii.psi.common.crafting.ModCraftingRecipes;
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
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipes(ModCraftingRecipes.TRICK_RECIPE_TYPE).values(), TrickCraftingCategory.UID);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		NonNullList<ItemStack> stacks = NonNullList.create();
		ModItems.cad.fillItemGroup(ItemGroup.SEARCH, stacks);
		for (ItemStack stack : stacks)
			registration.addRecipeCatalyst(stack, TrickCraftingCategory.UID);
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.useNbtForSubtypes(ModItems.cad);
	}
}
