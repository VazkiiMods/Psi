/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [06/02/2016, 20:09:22 (GMT)]
 */
package vazkii.psi.common.item.tool;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.item.base.ItemModTool;
import vazkii.psi.common.item.base.ModItems;

public class ItemPsimetalTool extends ItemModTool {

	protected ItemPsimetalTool(String name, float attackDamage, Set<Block> effectiveBlocks, String... variants) {
		super(name, attackDamage, PsiAPI.PSIMETAL_MATERIAL, effectiveBlocks, variants);
	}
	
	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return par2ItemStack.getItem() == ModItems.material && par2ItemStack.getItemDamage() == 1 ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}
	
}
