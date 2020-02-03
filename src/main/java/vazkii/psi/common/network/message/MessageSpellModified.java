/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 18:55:08 (GMT)]
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

	public MessageSpellModified() { }

	public MessageSpellModified(BlockPos pos, Spell spell) {
		super(pos, TileProgrammer.TYPE);
		this.spell = spell;
	}

	@Override
	public void receive(TileProgrammer tile, NetworkEvent.Context context) {
		context.enqueueWork(() -> {
			if(tile != null) {
				if(tile.playerLock == null || tile.playerLock.isEmpty() || tile.playerLock.equals(context.getSender().getName().getString())) {
					tile.spell = spell;
					tile.onSpellChanged();
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
				}
			}
		});
	}

}
