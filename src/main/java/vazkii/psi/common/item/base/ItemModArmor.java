/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [20/02/2016, 22:18:44 (GMT)]
 */
package vazkii.psi.common.item.base;

import java.util.List;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.psi.common.core.PsiCreativeTab;
import vazkii.psi.common.lib.LibResources;

public class ItemModArmor extends ItemArmor implements IVariantHolder {

	private final String[] variants;
	private final String bareName;

	protected ItemModArmor(String name, ArmorMaterial material, int type, EntityEquipmentSlot slot, String... variants) {
		super(material, type, slot);
		setUnlocalizedName(name);
		setCreativeTab(PsiCreativeTab.INSTANCE);

		if(variants.length == 0)
			variants = new String[] { name };

		bareName = name;
		this.variants = variants;
		ItemMod.variantHolders.add(this);
	}

	@Override
	public Item setUnlocalizedName(String name) {
		super.setUnlocalizedName(name);
		GameRegistry.register(this, new ResourceLocation(LibResources.PREFIX_MOD + name));

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
