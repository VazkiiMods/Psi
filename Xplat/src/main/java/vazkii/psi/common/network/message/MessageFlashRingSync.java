package vazkii.psi.common.network.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.item.FlashRingSpellTarget;

public record MessageFlashRingSync(Spell spell) implements CustomPacketPayload {

	public static final ResourceLocation ID = PsiAPI.location("message_flash_ring_sync");
	public static final CustomPacketPayload.Type<MessageFlashRingSync> TYPE = new CustomPacketPayload.Type<>(ID);

	public static final StreamCodec<RegistryFriendlyByteBuf, MessageFlashRingSync> CODEC = StreamCodec.composite(
			Spell.STREAM_CODEC, MessageFlashRingSync::spell,
			MessageFlashRingSync::new);

	@Override
	public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(Player player) {
		ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
		if(!setSpell(player, stack)) {
			setSpell(player, player.getItemInHand(InteractionHand.OFF_HAND));
		}
	}

	private boolean setSpell(Player player, ItemStack stack) {
		if(!stack.isEmpty() && stack.getItem() instanceof FlashRingSpellTarget target) {
			target.setEditedSpell(player, stack, spell);
			return true;
		}
		return false;
	}
}
