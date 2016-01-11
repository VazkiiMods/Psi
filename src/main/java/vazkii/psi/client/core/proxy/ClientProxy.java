/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [08/01/2016, 21:23:11 (GMT)]
 */
package vazkii.psi.client.core.proxy;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.psi.client.core.handler.HUDHandler;
import vazkii.psi.client.core.handler.ModelHandler;
import vazkii.psi.common.core.proxy.CommonProxy;
import vazkii.psi.common.item.ItemMaterial;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.component.ItemCADAssembly;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		
		ModelHandler.init();
		
		MinecraftForge.EVENT_BUS.register(new HUDHandler());
	}
	
}
