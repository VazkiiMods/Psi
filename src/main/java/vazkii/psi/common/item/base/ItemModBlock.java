/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [09/01/2016, 22:42:36 (GMT)]
 */
package vazkii.psi.common.item.base;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.psi.common.block.base.IPsiBlock;
import vazkii.psi.common.lib.LibResources;

public class ItemModBlock extends ItemBlock implements IVariantHolder {

	private IPsiBlock psiBlock;

	public ItemModBlock(Block block) {
		super(block);
		psiBlock = (IPsiBlock) block;

		ItemMod.variantHolders.add(this);
		if(getVariants().length > 1)
			setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public ItemBlock setUnlocalizedName(String par1Str) {
		GameRegistry.registerItem(this, par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		int dmg = par1ItemStack.getItemDamage();
		String[] variants = getVariants();

		String name;
		if(dmg >= variants.length)
			name = psiBlock.getBareName();
		else name = variants[dmg];

		return "tile." + LibResources.PREFIX_MOD + name;
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		String[] variants = getVariants();
		for(int i = 0; i < variants.length; i++)
			subItems.add(new ItemStack(itemIn, 1, i));
	}

	@Override
	public String[] getVariants() {
		return psiBlock.getVariants();
	}

	@Override
	public ItemMeshDefinition getCustomMeshDefinition() {
		return psiBlock.getCustomMeshDefinition();
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return psiBlock.getBlockRarity(stack);
	}

}
