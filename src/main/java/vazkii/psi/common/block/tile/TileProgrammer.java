/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [16/01/2016, 16:27:55 (GMT)]
 */
package vazkii.psi.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.block.tile.base.TileMod;

public class TileProgrammer extends TileMod {

	private static final String TAG_SPELL = "spell";
	
	public Spell spell;
	public boolean enabled;
	
	public boolean isEnabled() {
		return spell != null && !spell.grid.isEmpty();
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
	}
	
	@Override
	public void readSharedNBT(NBTTagCompound cmp) {
		super.readSharedNBT(cmp);
		
		NBTTagCompound spellCmp = cmp.getCompoundTag(TAG_SPELL);
		if(spell == null)
			spell = Spell.createFromNBT(spellCmp);
		else spell.readFromNBT(spellCmp);
	}
	
}
