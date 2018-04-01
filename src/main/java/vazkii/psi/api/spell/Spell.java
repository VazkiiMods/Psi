/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 15:17:25 (GMT)]
 */
package vazkii.psi.api.spell;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Basic class for a spell. Not much to see here.
 */
public final class Spell {

	private static final String TAG_VALID = "validSpell";
	public static final String TAG_SPELL_NAME = "spellName";
	public static final String TAG_UUID = "uuid";

	public SpellGrid grid = new SpellGrid(this);
	public String name = "";
	public UUID uuid;

	public Spell() {
		uuid = UUID.randomUUID();
	}

	@SideOnly(Side.CLIENT)
	public void draw() {
		grid.draw();
	}

	public static Spell createFromNBT(NBTTagCompound cmp) {
		if(!cmp.getBoolean(TAG_VALID))
			return null;

		Spell spell = new Spell();
		spell.readFromNBT(cmp);
		return spell;
	}

	public void readFromNBT(NBTTagCompound cmp) {
		name = cmp.getString(TAG_SPELL_NAME);

		if(cmp.hasKey(TAG_UUID)) {
			uuid = UUID.fromString(cmp.getString(TAG_UUID));
		}

		grid.readFromNBT(cmp);
	}

	public void writeToNBT(NBTTagCompound cmp) {
		cmp.setBoolean(TAG_VALID, true);
		cmp.setString(TAG_SPELL_NAME, name);

		cmp.setString(TAG_UUID, uuid.toString());

		grid.writeToNBT(cmp);
	}

	public Spell copy() {
		NBTTagCompound cmp = new NBTTagCompound();
		writeToNBT(cmp);
		return createFromNBT(cmp);
	}

}
