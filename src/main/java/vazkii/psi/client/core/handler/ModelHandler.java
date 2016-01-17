/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [08/01/2016, 23:02:00 (GMT)]
 */
package vazkii.psi.client.core.handler;

import java.util.HashMap;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import vazkii.psi.common.item.base.IVariantHolder;
import vazkii.psi.common.item.base.ItemMod;
import vazkii.psi.common.lib.LibResources;

public class ModelHandler {

	public static HashMap<String, ModelResourceLocation> resourceLocations = new HashMap();
	
	public static void init() {
		for(IVariantHolder holder : ItemMod.variantHolders)
			registerModels(holder);
	}

	public static void registerModels(IVariantHolder holder) {
		ItemMeshDefinition def = holder.getCustomMeshDefinition();
		if(def != null)
			ModelLoader.setCustomMeshDefinition((Item) holder, def);
		else registerModels((Item) holder, holder.getVariants());
	}
	
	public static void registerModels(Item item, String[] variants) {
		for(int i = 0; i < variants.length; i++) {
			String name = LibResources.PREFIX_MOD + variants[i];
			ModelResourceLocation loc = new ModelResourceLocation(name, "inventory");
			ModelLoader.setCustomModelResourceLocation(item, i, loc);
			resourceLocations.put(getKey(item, i), loc);
		}
	}
	
	public static ModelResourceLocation getModelLocation(ItemStack stack) {
		if(stack == null)
			return null;
		
		return getModelLocation(stack.getItem(), stack.getItemDamage());
	}
	
	public static ModelResourceLocation getModelLocation(Item item, int meta) {
		String key = getKey(item, meta);
		if(resourceLocations.containsKey(key))
			return resourceLocations.get(key);
		
		return null;
	}
	
	private static String getKey(Item item, int meta) {
		return item.getRegistryName() + "@" + meta;
	}
	

}
