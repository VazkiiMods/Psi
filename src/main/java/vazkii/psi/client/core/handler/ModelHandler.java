/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [08/01/2016, 23:02:00 (GMT)]
 */
package vazkii.psi.client.core.handler;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameData;
import vazkii.psi.common.block.base.IVariantEnumHolder;
import vazkii.psi.common.item.base.IExtraVariantHolder;
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
		else {
			Item i = (Item) holder;
			registerModels(i, holder.getVariants(), false);
			if(holder instanceof IExtraVariantHolder) {
				IExtraVariantHolder extra = (IExtraVariantHolder) holder;
				registerModels(i, extra.getExtraVariants(), true);
			}
		}
	}

	public static void registerModels(Item item, String[] variants, boolean extra) {
		if(item instanceof ItemBlock && ((ItemBlock) item).getBlock() instanceof IVariantEnumHolder) {
			IVariantEnumHolder holder = (IVariantEnumHolder) ((ItemBlock) item).getBlock();
			Class clazz = holder.getVariantEnum();
			registerVariantsDefaulted(item, (Block) holder, clazz, IVariantEnumHolder.HEADER);
			return;
		}

		for(int i = 0; i < variants.length; i++) {
			String name = LibResources.PREFIX_MOD + variants[i];
			ModelResourceLocation loc = new ModelResourceLocation(name, "inventory");
			if(!extra) {
				ModelLoader.setCustomModelResourceLocation(item, i, loc);
				resourceLocations.put(getKey(item, i), loc);
			} else {
				ModelBakery.registerItemVariants(item, loc);
				resourceLocations.put(variants[i], loc);
			}
		}
	}

	private static <T extends Enum<T> & IStringSerializable> void registerVariantsDefaulted(Item item, Block b, Class<T> enumclazz, String variantHeader) {
		String baseName = GameData.getBlockRegistry().getNameForObject(b).toString();
		for(T e : enumclazz.getEnumConstants()) {
			String variantName = variantHeader + "=" + e.getName();
			ModelResourceLocation loc = new ModelResourceLocation(baseName, variantName);
			int i = e.ordinal();
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
		return "i_" + item.getRegistryName() + "@" + meta;
	}


}
