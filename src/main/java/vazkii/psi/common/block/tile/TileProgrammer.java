/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 16:27:55 (GMT)]
 */
package vazkii.psi.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.block.tile.base.TileMod;
import vazkii.psi.common.spell.SpellCompiler;

public class TileProgrammer extends TileMod {

	private static final String TAG_SPELL = "spell";
	private static final String TAG_PLAYER_LOCK = "playerLock";

	public Spell spell;
	public boolean enabled;

	public String playerLock = "";

	public boolean isEnabled() {
		return spell != null && !spell.grid.isEmpty();
	}

	public boolean canCompile() {
		return isEnabled() && !new SpellCompiler(spell).isErrored();
	}

	public void onSpellChanged() {
		boolean wasEnabled = enabled;
		enabled = isEnabled();
		if(wasEnabled != enabled)
			worldObj.markBlockRangeForRenderUpdate(pos, pos);
	}

	@Override
	public void writeSharedNBT(NBTTagCompound cmp) {
		super.writeSharedNBT(cmp);

		NBTTagCompound spellCmp = new NBTTagCompound();
		if(spell != null)
			spell.writeToNBT(spellCmp);
		cmp.setTag(TAG_SPELL, spellCmp);
		cmp.setString(TAG_PLAYER_LOCK, playerLock);
	}

	@Override
	public void readSharedNBT(NBTTagCompound cmp) {
		super.readSharedNBT(cmp);

		NBTTagCompound spellCmp = cmp.getCompoundTag(TAG_SPELL);
		if(spell == null)
			spell = Spell.createFromNBT(spellCmp);
		else spell.readFromNBT(spellCmp);
		playerLock = cmp.getString(TAG_PLAYER_LOCK);
	}

}
