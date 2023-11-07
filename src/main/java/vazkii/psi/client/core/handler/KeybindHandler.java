/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.core.handler;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import vazkii.patchouli.api.PatchouliAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.cad.ISocketableController;
import vazkii.psi.client.gui.GuiSocketSelect;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID, bus = Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class KeybindHandler {
	public static KeyMapping keybind = new KeyMapping("psimisc.keybind", GLFW_KEY_C, "key.categories.psi");

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void register(RegisterKeyMappingsEvent event) {
		event.register(keybind);
	}

	private static boolean isSocketableController(Player player, ItemStack stack) {
		if(!(stack.getItem() instanceof ISocketableController)) {
			return false;
		}

		ItemStack[] stacks = ((ISocketableController) stack.getItem()).getControlledStacks(player, stack);

		for(ItemStack controlled : stacks) {
			if(!controlled.isEmpty() && ISocketable.isSocketable(controlled)) {
				return true;
			}
		}

		return false;
	}

	public static void keyDown() {
		Minecraft mc = Minecraft.getInstance();
		ItemStack stack = mc.player.getItemInHand(InteractionHand.MAIN_HAND);

		if(mc.screen == null) {
			if(!stack.isEmpty() && (ISocketable.isSocketable(stack) || isSocketableController(mc.player, stack))) {
				mc.setScreen(new GuiSocketSelect(stack));
			} else {
				stack = mc.player.getItemInHand(InteractionHand.OFF_HAND);
				if(!stack.isEmpty() && (ISocketable.isSocketable(stack) || isSocketableController(mc.player, stack))) {
					mc.setScreen(new GuiSocketSelect(stack));
				} else {
					PatchouliAPI.get().openBookGUI(LibResources.PATCHOULI_BOOK);
				}

			}
		}
	}

}
