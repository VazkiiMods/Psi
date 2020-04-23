/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.arl.network.message.AbstractTEMessage;
import vazkii.psi.api.internal.VanillaPacketDispatcher;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.block.tile.TileProgrammer;

public class MessageSpellModified extends AbstractTEMessage<TileProgrammer> {

	public Spell spell;

	public MessageSpellModified() {}

	public MessageSpellModified(BlockPos pos, Spell spell) {
		super(pos, TileProgrammer.TYPE);
		this.spell = spell;
	}

	@Override
	public void receive(TileProgrammer tile, NetworkEvent.Context context) {
		context.enqueueWork(() -> {
			if (tile != null) {
				if (tile.playerLock == null || tile.playerLock.isEmpty() || tile.playerLock.equals(context.getSender().getName().getString())) {
					tile.spell = spell;
					tile.onSpellChanged();
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
				}
			}
		});
	}

}
