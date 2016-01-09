/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * {todo-put-license-here}
 * 
 * File Created @ [08/01/2016, 23:02:00 (GMT)]
 */
package vazkii.psi.client.core.handler;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import vazkii.psi.common.item.IVariantHolder;
import vazkii.psi.common.item.ItemMaterial;
import vazkii.psi.common.item.ModItems;
import vazkii.psi.common.item.component.ItemCADAssembly;
import vazkii.psi.common.lib.LibResources;

public class ModelHandler {

	public static void init() {
		registerModels(ModItems.material);

		registerModels(ModItems.cadAssembly);
	}

	public static void registerModels(IVariantHolder holder) {
		registerModels((Item) holder, holder.getVariants());
	}
	
	public static void registerModels(Item item, String[] variants) {
		for (int i = 0; i < variants.length; i++) {
			String name = LibResources.PREFIX_MOD + variants[i];
			ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(name, "inventory"));
		}
	}

}
