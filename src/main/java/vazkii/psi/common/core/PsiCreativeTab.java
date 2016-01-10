/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [08/01/2016, 21:49:46 (GMT)]
 */
package vazkii.psi.common.core;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;

public class PsiCreativeTab extends CreativeTabs {

	public static PsiCreativeTab INSTANCE = new PsiCreativeTab();
	List list;

	public PsiCreativeTab() {
		super(LibMisc.MOD_ID);
		setNoTitle();
		setBackgroundImageName(LibResources.GUI_CREATIVE);
	}

	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(ModItems.cadAssembly);
	}

	@Override
	public Item getTabIconItem() {
		return getIconItemStack().getItem();
	}

	@Override
	public boolean hasSearchBar() {
		return true;
	}
	
	@Override
	public void displayAllReleventItems(List<ItemStack> p_78018_1_) {
		list = p_78018_1_;
		
		addBlock(ModBlocks.cadAssembler);
		
		addItem(ModItems.material);
		
		addItem(ModItems.cadAssembly);
		addItem(ModItems.cadCore);
		addItem(ModItems.cadSocket);
		addItem(ModItems.cadBattery);
		addItem(ModItems.spellBullet);
		
		addItem(ModItems.cad);
	}

	private void addItem(Item item) {
		item.getSubItems(item, this, list);
	}

	private void addBlock(Block block) {
		ItemStack stack = new ItemStack(block);
		block.getSubBlocks(stack.getItem(), this, list);
	}

	private void addStack(ItemStack stack) {
		list.add(stack);
	}
	
}
