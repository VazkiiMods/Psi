/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * Basic class for a spell. Not much to see here.
 */
public final class Spell {

	private static final String TAG_VALID = "validSpell";
	public static final String TAG_SPELL_NAME = "spellName";
	public static final String TAG_UUID_MOST = "uuidMost";
	public static final String TAG_UUID_LEAST = "uuidLeast";
	public static final String TAG_MODS_REQUIRED = "modsRequired";
	public static final String TAG_MOD_NAME = "modName";
	public static final String TAG_MOD_VERSION = "modVersion";

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

	@Nullable
	public static Spell createFromNBT(CompoundNBT cmp) {
		if (cmp == null || !cmp.getBoolean(TAG_VALID)) {
			return null;
		}

		Spell spell = new Spell();
		spell.readFromNBT(cmp);
		return spell;
	}

	public void readFromNBT(CompoundNBT cmp) {
		name = cmp.getString(TAG_SPELL_NAME);

		if (cmp.contains(TAG_UUID_MOST)) {
			long uuidMost = cmp.getLong(TAG_UUID_MOST);
			long uuidLeast = cmp.getLong(TAG_UUID_LEAST);
			if (uuid.getMostSignificantBits() != uuidMost || uuid.getLeastSignificantBits() != uuidLeast) {
				uuid = new UUID(uuidMost, uuidLeast);
			}
		}

		grid.readFromNBT(cmp);
	}

	public Set<String> getPieceNamespaces() {
		Set<String> temp = Collections.newSetFromMap(new HashMap<>());
		for (SpellPiece[] gridDatum : grid.gridData) {
			for (SpellPiece spellPiece : gridDatum) {
				if (spellPiece != null) {
					temp.add(spellPiece.registryKey.getNamespace());
				}
			}
		}
		return temp;
	}

	public void writeToNBT(CompoundNBT cmp) {
		cmp.putBoolean(TAG_VALID, true);
		cmp.putString(TAG_SPELL_NAME, name);
		ListNBT modList = new ListNBT();
		for (String namespace : getPieceNamespaces()) {
			CompoundNBT nbt = new CompoundNBT();
			nbt.putString(TAG_MOD_NAME, namespace);
			if (ModList.get().getModContainerById(namespace).isPresent()) {
				nbt.putString(TAG_MOD_VERSION, ModList.get().getModContainerById(namespace).get().getModInfo().getVersion().toString());
			}
			modList.add(nbt);
		}
		cmp.put(TAG_MODS_REQUIRED, modList);
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
