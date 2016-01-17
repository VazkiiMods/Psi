/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [16/01/2016, 18:55:08 (GMT)]
 */
package vazkii.psi.common.network.message;

import net.minecraft.util.BlockPos;
import vazkii.psi.api.internal.VanillaPacketDispatcher;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.block.tile.TileProgrammer;

public class MessageSpellModified extends TileEntityMessage<TileProgrammer> {

	public Spell spell;
	
	public MessageSpellModified() { }
	
	public MessageSpellModified(BlockPos pos, Spell spell) {
		super(pos);
		this.spell = spell;
	}
	
	@Override
	public Runnable getAction() {
		return () -> {
			if(tile != null) {
				tile.spell = spell;
				tile.onSpellChanged();
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
			}
		};
	}
	
}
