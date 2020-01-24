/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [25/01/2016, 20:38:39 (GMT)]
 */
package vazkii.psi.common.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibPieceNames;

public class ModCraftingRecipes {

	public static void init() {
		PsiAPI.registerTrickRecipe("", "dustRedstone", new ItemStack(ModItems.material), new ItemStack(ModItems.cadAssembly));

		PsiAPI.registerTrickRecipe(LibPieceNames.TRICK_INFUSION, "ingotGold",
				new ItemStack(ModItems.material, 1, 1), 
				new ItemStack(ModItems.cadAssembly));
		PsiAPI.registerTrickRecipe(LibPieceNames.TRICK_GREATER_INFUSION, "gemDiamond",
				new ItemStack(ModItems.material, 1, 2), 
				new ItemStack(ModItems.cadAssembly, 1, 2));
		PsiAPI.registerTrickRecipe(LibPieceNames.TRICK_EBONY_IVORY, new ItemStack(Items.COAL),
				new ItemStack(ModItems.material, 1, 5), 
				new ItemStack(ModItems.cadAssembly, 1, 2));
		PsiAPI.registerTrickRecipe(LibPieceNames.TRICK_EBONY_IVORY, "gemQuartz",
				new ItemStack(ModItems.material, 1, 6), 
				new ItemStack(ModItems.cadAssembly, 1, 2));

	}



}
