/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketableController;
import vazkii.psi.common.core.handler.PsiPlayerData;

public record MessageChangeControllerSlot(int controlSlot, int slot) implements CustomPacketPayload {

	public static final ResourceLocation ID = PsiAPI.location("message_change_controller_slot");
	public static final CustomPacketPayload.Type<MessageChangeControllerSlot> TYPE = new Type<>(ID);

	public static final StreamCodec<RegistryFriendlyByteBuf, MessageChangeControllerSlot> CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, MessageChangeControllerSlot::controlSlot,
			ByteBufCodecs.INT, MessageChangeControllerSlot::slot,
			MessageChangeControllerSlot::new);

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(Player player) {
		ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
		if(!stack.isEmpty() && stack.getItem() instanceof ISocketableController controller) {
			controller.setSelectedSlot(player, stack, controlSlot, slot);
		} else {
			stack = player.getItemInHand(InteractionHand.OFF_HAND);
			if(!stack.isEmpty() && stack.getItem() instanceof ISocketableController controller) {
				controller.setSelectedSlot(player, stack, controlSlot, slot);
			}
		}
		PsiPlayerData.get(player).stopLoopcast();
	}

}
