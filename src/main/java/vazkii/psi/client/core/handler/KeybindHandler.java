/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import vazkii.patchouli.api.PatchouliAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.cad.ISocketableController;
import vazkii.psi.client.gui.GuiSocketSelect;
import vazkii.psi.common.lib.LibResources;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;

public class KeybindHandler {

	public static KeyBinding keybind;

	public static void init() {
		keybind = new KeyBinding("psimisc.keybind", GLFW_KEY_C, "key.categories.inventory");
		ClientRegistry.registerKeyBinding(keybind);
	}

	private static boolean isSocketableController(PlayerEntity player, ItemStack stack) {
		if (!(stack.getItem() instanceof ISocketableController)) {
			return false;
		}

		ItemStack[] stacks = ((ISocketableController) stack.getItem()).getControlledStacks(player, stack);

		for (ItemStack controlled : stacks) {
			if (!controlled.isEmpty() && ISocketable.isSocketable(controlled)) {
				return true;
			}
		}

		return false;
	}

	public static void keyDown() {
		Minecraft mc = Minecraft.getInstance();
		ItemStack stack = mc.player.getItemInHand(Hand.MAIN_HAND);

		if (mc.screen == null) {
			if (!stack.isEmpty() && (ISocketable.isSocketable(stack) || isSocketableController(mc.player, stack))) {
				mc.setScreen(new GuiSocketSelect(stack));
			} else {
				stack = mc.player.getItemInHand(Hand.OFF_HAND);
				if (!stack.isEmpty() && (ISocketable.isSocketable(stack) || isSocketableController(mc.player, stack))) {
					mc.setScreen(new GuiSocketSelect(stack));
				} else {
					PatchouliAPI.instance.openBookGUI(LibResources.PATCHOULI_BOOK);
				}

			}
		}
	}

}
