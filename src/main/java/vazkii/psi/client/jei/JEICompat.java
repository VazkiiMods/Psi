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
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.*;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.recipe.ITrickRecipe;
import vazkii.psi.client.jei.crafting.BulletToDriveExtension;
import vazkii.psi.client.jei.crafting.DriveDuplicateExtension;
import vazkii.psi.client.jei.tricks.TrickCraftingCategory;
import vazkii.psi.common.Psi;
import vazkii.psi.common.crafting.recipe.BulletToDriveRecipe;
import vazkii.psi.common.crafting.recipe.DriveDuplicateRecipe;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.StringJoiner;

@JeiPlugin
public class JEICompat implements IModPlugin {
	private static final ResourceLocation UID = Psi.location("main");
	public static IJeiHelpers helpers;

	@NotNull
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
		registration.getCraftingCategory().addExtension(BulletToDriveRecipe.class, new BulletToDriveExtension());
		registration.getCraftingCategory().addExtension(DriveDuplicateRecipe.class, new DriveDuplicateExtension());
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		List<ITrickRecipe> trickRecipes = new ArrayList<>();

		for(var holder : Minecraft.getInstance().level.getRecipeManager().getRecipes()) {
			switch(holder.value()) {
			case ITrickRecipe recipe -> trickRecipes.add(recipe);
			default -> {
			}
			}
		}

		registration.addRecipes(TrickCraftingCategory.TYPE, trickRecipes);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		List<ItemStack> stacks = ItemCAD.getCreativeTabItems();
		for(ItemStack stack : stacks) {
			registration.addRecipeCatalyst(stack, TrickCraftingCategory.TYPE);
		}
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.registerSubtypeInterpreter(ModItems.cad.get(), Cad.INSTANCE);
	}

	private static class Cad implements ISubtypeInterpreter<ItemStack> {
		public static final Cad INSTANCE = new Cad();

		private Cad() {}

		public String getSubtypeData(ItemStack itemStack, UidContext context) {
			ItemCAD cad = (ItemCAD) itemStack.getItem();

			List<String> strings = new ArrayList<>();
			for(EnumCADComponent c : EnumSet.allOf(EnumCADComponent.class)) {
				String s = c.getName() + "." + cad.getComponentInSlot(itemStack, c).getItem().getDescriptionId();
				strings.add(s);
			}

			StringJoiner joiner = new StringJoiner(",", "[", "]");
			strings.sort(null);
			for(String s : strings) {
				joiner.add(s);
			}
			return joiner.toString();
		}

		@Deprecated(since = "19.9.0")
		@Override
		public @NotNull String getLegacyStringSubtypeInfo(@NotNull ItemStack ingredient, @NotNull UidContext context) {
			return "";
		}
	}
}
