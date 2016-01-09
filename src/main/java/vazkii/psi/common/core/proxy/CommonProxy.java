/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * {todo-put-license-here}
 * 
 * File Created @ [08/01/2016, 21:22:59 (GMT)]
 */
package vazkii.psi.common.core.proxy;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.item.ModItems;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.init(event.getSuggestedConfigurationFile());
		
		ModItems.init();
		
		registerModels();
	}
	
	public void registerModels() {
		// NO-OP
	}
	
}
