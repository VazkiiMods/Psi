/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

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

	public MessageSpellModified(PacketBuffer buf) {
		this.pos = buf.readBlockPos();
		this.spell = readSpell(buf);
	}

	private static Spell readSpell(PacketBuffer buf) {
		CompoundNBT cmp = buf.readCompoundTag();
		return Spell.createFromNBT(cmp);
	}

	private static void writeSpell(PacketBuffer buf, Spell spell) {
		CompoundNBT cmp = new CompoundNBT();
		if (spell != null) {
			spell.writeToNBT(cmp);
		}

		buf.writeCompoundTag(cmp);
	}

	public void encode(PacketBuffer buf) {
		buf.writeBlockPos(pos);
		writeSpell(buf, spell);
	}

	public void receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			TileEntity te = context.get().getSender().world.getTileEntity(pos);
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
