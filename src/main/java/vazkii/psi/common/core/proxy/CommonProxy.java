/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [08/01/2016, 21:22:59 (GMT)]
 */
package vazkii.psi.common.core.proxy;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.network.GuiHandler;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.init(event.getSuggestedConfigurationFile());
		
		ModItems.init();
		ModBlocks.init();
		
		registerModels();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(Psi.instance, new GuiHandler());
	}
	
	public void registerModels() {
		// NO-OP
	}
	
}
