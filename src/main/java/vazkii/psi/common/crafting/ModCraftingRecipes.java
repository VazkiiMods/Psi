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
import net.minecraftforge.common.Tags;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibPieceNames;

public class ModCraftingRecipes {

	public static void init() {
        PsiAPI.registerTrickRecipe("", Tags.Items.DUSTS_REDSTONE, new ItemStack(ModItems.psidust), new ItemStack(ModItems.cadAssemblyIron));

        PsiAPI.registerTrickRecipe(LibPieceNames.TRICK_INFUSION, "ingotGold",
                new ItemStack(ModItems.psimetal),
                new ItemStack(ModItems.cadAssemblyIron));
        PsiAPI.registerTrickRecipe(LibPieceNames.TRICK_GREATER_INFUSION, "gemDiamond",
                new ItemStack(ModItems.psimetal),
                new ItemStack(ModItems.cadAssemblyPsimetal));
        PsiAPI.registerTrickRecipe(LibPieceNames.TRICK_EBONY_IVORY, new ItemStack(Items.COAL),
                new ItemStack(ModItems.ebonySubstance),
                new ItemStack(ModItems.cadAssemblyPsimetal));
        PsiAPI.registerTrickRecipe(LibPieceNames.TRICK_EBONY_IVORY, "gemQuartz",
                new ItemStack(ModItems.ivoryPsimetal),
                new ItemStack(ModItems.cadAssemblyPsimetal));

	}



}
