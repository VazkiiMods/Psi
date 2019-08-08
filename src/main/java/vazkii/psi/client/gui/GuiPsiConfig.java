/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [11/01/2016, 19:57:44 (GMT)]
 */
package vazkii.psi.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.lib.LibMisc;

public class GuiPsiConfig extends GuiConfig {

	public GuiPsiConfig(Screen parentScreen) {
		super(parentScreen, new ConfigElement(ConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), LibMisc.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));
	}

}