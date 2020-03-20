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

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

/**
 * Basic class for a spell. Not much to see here.
 */
public final class Spell {

	private static final String TAG_VALID = "validSpell";
	public static final String TAG_SPELL_NAME = "spellName";
	public static final String TAG_UUID_MOST = "uuidMost";
	public static final String TAG_UUID_LEAST = "uuidLeast";

	public final SpellGrid grid = new SpellGrid(this);
	public String name = "";
	public UUID uuid;

	public Spell() {
		uuid = UUID.randomUUID();
	}

	@OnlyIn(Dist.CLIENT)
	public void draw(MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		grid.draw(ms, buffers, light);
	}

	public static Spell createFromNBT(CompoundNBT cmp) {
		if(cmp == null || !cmp.getBoolean(TAG_VALID))
			return null;

		Spell spell = new Spell();
		spell.readFromNBT(cmp);
		return spell;
	}

	public void readFromNBT(CompoundNBT cmp) {
		name = cmp.getString(TAG_SPELL_NAME);

		if (cmp.contains(TAG_UUID_MOST)) {
			long uuidMost = cmp.getLong(TAG_UUID_MOST);
			long uuidLeast = cmp.getLong(TAG_UUID_LEAST);
			if (uuid.getMostSignificantBits() != uuidMost || uuid.getLeastSignificantBits() != uuidLeast)
				uuid = new UUID(uuidMost, uuidLeast);
		}

		grid.readFromNBT(cmp);
	}

	public void writeToNBT(CompoundNBT cmp) {
		cmp.putBoolean(TAG_VALID, true);
		cmp.putString(TAG_SPELL_NAME, name);

		cmp.putLong(TAG_UUID_MOST, uuid.getMostSignificantBits());
		cmp.putLong(TAG_UUID_LEAST, uuid.getLeastSignificantBits());

		grid.writeToNBT(cmp);
	}

	public Spell copy() {
		CompoundNBT cmp = new CompoundNBT();
		writeToNBT(cmp);
		return createFromNBT(cmp);
	}

}
