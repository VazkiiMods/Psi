/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [13/01/2016, 23:25:53 (GMT)]
 */
package vazkii.psi.client.core.handler;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.cad.ISocketableController;
import vazkii.psi.client.gui.GuiLeveling;
import vazkii.psi.client.gui.GuiSocketSelect;

public class KeybindHandler {

	public static KeyBinding keybind;

	public static void init() {
		keybind = new KeyBinding("psimisc.keybind", Keyboard.KEY_C, "key.categories.inventory");
		ClientRegistry.registerKeyBinding(keybind);
	}

	public static void keyDown() {
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack stack = mc.thePlayer.getCurrentEquippedItem();
		if(stack != null && (stack.getItem() instanceof ISocketable || stack.getItem() instanceof ISocketableController))
			mc.displayGuiScreen(new GuiSocketSelect(stack));
		else mc.displayGuiScreen(new GuiLeveling());
	}

}
