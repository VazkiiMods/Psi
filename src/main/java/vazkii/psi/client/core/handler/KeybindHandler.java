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

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;
import vazkii.psi.api.cad.ISocketableCapability;
import vazkii.psi.api.cad.ISocketableController;
import vazkii.psi.client.gui.GuiLeveling;
import vazkii.psi.client.gui.GuiSocketSelect;

public class KeybindHandler {

	public static KeyBinding keybind;

	public static void init() {
		keybind = new KeyBinding("psimisc.keybind", Keyboard.KEY_C, "key.categories.inventory");
		ClientRegistry.registerKeyBinding(keybind);
	}

	private static boolean isSocketableController(PlayerEntity player, ItemStack stack) {
		if (!(stack.getItem() instanceof ISocketableController))
			return false;

		ItemStack[] stacks = ((ISocketableController) stack.getItem()).getControlledStacks(player, stack);

		for (ItemStack controlled : stacks) {
			if (!controlled.isEmpty() && ISocketableCapability.isSocketable(controlled))
				return true;
		}

		return false;
	}

	public static void keyDown() {
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack stack = mc.player.getHeldItem(Hand.MAIN_HAND);
		
		if(mc.currentScreen == null) {
			if(!stack.isEmpty() && (ISocketableCapability.isSocketable(stack) || isSocketableController(mc.player, stack)))
				mc.displayGuiScreen(new GuiSocketSelect(stack));
			else {
				stack = mc.player.getHeldItem(Hand.OFF_HAND);
				if(!stack.isEmpty() && (ISocketableCapability.isSocketable(stack) || isSocketableController(mc.player, stack)))
					mc.displayGuiScreen(new GuiSocketSelect(stack));
				else mc.displayGuiScreen(new GuiLeveling());
			}
		}
	}

}
