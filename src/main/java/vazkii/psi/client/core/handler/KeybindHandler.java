/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import org.jetbrains.annotations.NotNull;

import vazkii.patchouli.api.PatchouliAPI;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.cad.ISocketableController;
import vazkii.psi.client.gui.GuiSocketSelect;
import vazkii.psi.common.lib.LibResources;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;

@EventBusSubscriber(value = Dist.CLIENT, modid = PsiAPI.MOD_ID)
@OnlyIn(Dist.CLIENT)
public class KeybindHandler {
	public static final KeyMapping keybind = new KeyMapping("psimisc.keybind", GLFW_KEY_C, "key.categories.psi");

	@SubscribeEvent
	public static void register(RegisterKeyMappingsEvent event) {
		event.register(keybind);
	}

	public static void keyDown() {
		Minecraft mc = Minecraft.getInstance();
		if(mc.screen != null || mc.player == null) {
			return;
		}

		ItemStack stack = mc.player.getItemInHand(InteractionHand.MAIN_HAND);
		if(isSocketable(mc.player, stack)) {
			mc.setScreen(new GuiSocketSelect(stack));
			return;
		}

		stack = mc.player.getItemInHand(InteractionHand.OFF_HAND);
		if(isSocketable(mc.player, stack)) {
			mc.setScreen(new GuiSocketSelect(stack));
			return;
		}

		PatchouliAPI.get().openBookGUI(LibResources.PATCHOULI_BOOK);
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

	private static boolean isSocketable(@NotNull Player player, @NotNull ItemStack stack) {
		return !stack.isEmpty() && (ISocketable.isSocketable(stack) || isSocketableController(player, stack));
	}

}
