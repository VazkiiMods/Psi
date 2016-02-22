/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
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
		addBlock(ModBlocks.programmer);

		addItem(ModItems.material);

		addItem(ModItems.cadAssembly);
		addItem(ModItems.cadCore);
		addItem(ModItems.cadSocket);
		addItem(ModItems.cadBattery);
		addItem(ModItems.cadColorizer);

		addItem(ModItems.spellBullet);
		addItem(ModItems.detonator);
		addItem(ModItems.spellDrive);
		addItem(ModItems.exosuitController);
		addItem(ModItems.exosuitSensor);
		addItem(ModItems.vectorRuler);

		addItem(ModItems.cad);

		addItem(ModItems.psimetalShovel);
		addItem(ModItems.psimetalPickaxe);
		addItem(ModItems.psimetalAxe);
		addItem(ModItems.psimetalSword);
		addItem(ModItems.psimetalExosuitHelmet);
		addItem(ModItems.psimetalExosuitChestplate);
		addItem(ModItems.psimetalExosuitLeggings);
		addItem(ModItems.psimetalExosuitBoots);

		addBlock(ModBlocks.psiDecorative);
	}

	private void addItem(Item item) {
		item.getSubItems(item, this, list);
	}

	private void addBlock(Block block) {
		addItem(Item.getItemFromBlock(block));
	}

}
