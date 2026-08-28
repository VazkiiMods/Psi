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
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.capability.PsiCapabilities;
import vazkii.psi.common.core.handler.PsiPlayerData;

public record MessageChangeSocketableSlot(int slot) implements CustomPacketPayload {

	public static final ResourceLocation ID = PsiAPI.location("message_change_socketable_slot");
	public static final CustomPacketPayload.Type<MessageChangeSocketableSlot> TYPE = new Type<>(ID);

	public static final StreamCodec<RegistryFriendlyByteBuf, MessageChangeSocketableSlot> CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, MessageChangeSocketableSlot::slot,
			MessageChangeSocketableSlot::new);

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(Player player) {
		ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
		ISocketable socketable = PsiCapabilities.socketable(stack);
		if(!stack.isEmpty() && socketable != null) {
			socketable.setSelectedSlot(slot);
		} else {
			stack = player.getItemInHand(InteractionHand.OFF_HAND);
			socketable = PsiCapabilities.socketable(stack);
			if(!stack.isEmpty() && socketable != null) {
				socketable.setSelectedSlot(slot);
			}
		}
		PsiPlayerData.get(player).stopLoopcast();
	}

}
