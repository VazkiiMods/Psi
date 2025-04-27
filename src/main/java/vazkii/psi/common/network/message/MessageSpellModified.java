/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import vazkii.psi.api.internal.VanillaPacketDispatcher;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.lib.LibMisc;

public record MessageSpellModified(BlockPos pos, Spell spell) implements CustomPacketPayload {

	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "message_spell_modified");
	public static final CustomPacketPayload.Type<MessageSpellModified> TYPE = new Type<>(ID);

	public static final StreamCodec<RegistryFriendlyByteBuf, MessageSpellModified> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, MessageSpellModified::pos,
			Spell.STREAM_CODEC, MessageSpellModified::spell,
			MessageSpellModified::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			BlockEntity te = ctx.player().level().getBlockEntity(pos);
			if(te instanceof TileProgrammer tile) {
				if(tile.playerLock == null || tile.playerLock.isEmpty() || tile.playerLock.equals(ctx.player().getName().getString())) {
					tile.spell = spell;
					tile.onSpellChanged();
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
				}
			}
		});
	}
}
