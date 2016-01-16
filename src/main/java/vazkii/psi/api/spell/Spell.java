/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [16/01/2016, 15:17:25 (GMT)]
 */
package vazkii.psi.api.spell;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class Spell {

	private static final int SPELL_GRID_SIZE = 9;
	public SpellGrid grid = new SpellGrid(this, SPELL_GRID_SIZE, SPELL_GRID_SIZE);
	
	@SideOnly(Side.CLIENT)
	public void draw() {
		
	}
	
	public static Spell createFromNBT(NBTTagCompound cmp) {
		Spell spell = new Spell();
		spell.readFromNBT(cmp);
		return spell;
	}
	
	public void readFromNBT(NBTTagCompound cmp) {
		grid.readFromNBT(cmp);
	}
	
	public void writeToNBT(NBTTagCompound cmp) {
		grid.writeToNBT(cmp);
	}
	
}
