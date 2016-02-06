/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [06/02/2016, 20:07:08 (GMT)]
 */
package vazkii.psi.common.item.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.psi.common.core.PsiCreativeTab;
import vazkii.psi.common.lib.LibResources;

public class ItemModTool extends ItemTool implements IVariantHolder {

	private final String[] variants;
	private final String bareName;
	
	protected ItemModTool(String name, float attackDamage, ToolMaterial material, Set<Block> effectiveBlocks, String... variants) {
		super(attackDamage, material, effectiveBlocks);
		setUnlocalizedName(name);
		setCreativeTab(PsiCreativeTab.INSTANCE);
		if(variants.length > 1)
			setHasSubtypes(true);
		
		if(variants.length == 0)
			variants = new String[] { name };
		
		bareName = name;
		this.variants = variants;
		ItemMod.variantHolders.add(this);
	}
	
	@Override
	public Item setUnlocalizedName(String name) {
		super.setUnlocalizedName(name);
		setRegistryName(name);
		GameRegistry.registerItem(this, name);
		
		return this;
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		int dmg = par1ItemStack.getItemDamage();
		String[] variants = getVariants();
		
		String name;
		if(dmg >= variants.length)
			name = bareName;
		else name = variants[dmg];
		
		return "item." + LibResources.PREFIX_MOD + name;
	}
	 
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for(int i = 0; i < getVariants().length; i++)
			subItems.add(new ItemStack(itemIn, 1, i));
	}
	
	@Override
	public String[] getVariants() {
		return variants;
	}
	
	@Override
	public ItemMeshDefinition getCustomMeshDefinition() {
		return null;
	}

}
