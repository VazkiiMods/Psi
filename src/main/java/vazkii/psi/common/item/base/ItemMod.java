/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * {todo-put-license-here}
 * 
 * File Created @ [08/01/2016, 21:47:43 (GMT)]
 */
package vazkii.psi.common.item.base;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.psi.common.core.PsiCreativeTab;
import vazkii.psi.common.lib.LibResources;

public class ItemMod extends Item implements IVariantHolder {

	public static final List<IVariantHolder> variantHolders = new ArrayList();
	
	private final String[] variants;
	private final String bareName;
	
	public ItemMod(String name, String... variants) {
		setUnlocalizedName(name);
		setCreativeTab(PsiCreativeTab.INSTANCE);
		if(variants.length > 1)
			setHasSubtypes(true);
		
		if(variants.length == 0)
			variants = new String[] { name };
		
		bareName = name;
		this.variants = variants;
		variantHolders.add(this);
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
	
	public static void tooltipIfShift(EntityPlayer playerIn, List<String> tooltip, Runnable r) {
		if(GuiScreen.isShiftKeyDown())
			r.run();
		else addToTooltip(tooltip, "psimisc.shiftForInfo");
	}
	
	public static void addToTooltip(List<String> tooltip, String s, Object... format) {
		s = local(s).replaceAll("&", "\u00a7");
		
		if(format != null && format.length > 0)
			s = String.format(s, format);
		
		tooltip.add(s);
	}

	@Override
	public ItemMeshDefinition getCustomMeshDefinition() {
		return null;
	}
	
	public static String local(String s) {
		return StatCollector.translateToLocal(s);
	}
	
}
