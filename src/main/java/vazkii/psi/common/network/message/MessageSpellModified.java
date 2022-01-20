/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraftforge.network.NetworkEvent;

import vazkii.psi.api.internal.VanillaPacketDispatcher;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.block.tile.TileProgrammer;

import java.util.function.Supplier;

public class MessageSpellModified {

	private final BlockPos pos;
	private final Spell spell;

	public MessageSpellModified(BlockPos pos, Spell spell) {
		this.pos = pos;
		this.spell = spell;
	}

	public MessageSpellModified(FriendlyByteBuf buf) {
		this.pos = buf.readBlockPos();
		this.spell = readSpell(buf);
	}

	private static Spell readSpell(FriendlyByteBuf buf) {
		CompoundTag cmp = buf.readNbt();
		return Spell.createFromNBT(cmp);
	}

	private static void writeSpell(FriendlyByteBuf buf, Spell spell) {
		CompoundTag cmp = new CompoundTag();
		if (spell != null) {
			spell.writeToNBT(cmp);
		}

		buf.writeNbt(cmp);
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		writeSpell(buf, spell);
	}

	public void receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			BlockEntity te = context.get().getSender().level.getBlockEntity(pos);
			if (te instanceof TileProgrammer) {
				TileProgrammer tile = (TileProgrammer) te;
				if (tile.playerLock == null || tile.playerLock.isEmpty() || tile.playerLock.equals(context.get().getSender().getName().getString())) {
					tile.spell = spell;
					tile.onSpellChanged();
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
				}
			}
		});
	}

}
