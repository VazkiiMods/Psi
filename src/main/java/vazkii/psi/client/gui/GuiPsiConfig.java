/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [11/01/2016, 19:57:44 (GMT)]
 */
package vazkii.psi.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.lib.LibMisc;

public class GuiPsiConfig extends GuiConfig {

	public GuiPsiConfig(GuiScreen parentScreen) {
		super(parentScreen, new ConfigElement(ConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), LibMisc.MOD_ID, false, false, GuiPsiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));
	}

}