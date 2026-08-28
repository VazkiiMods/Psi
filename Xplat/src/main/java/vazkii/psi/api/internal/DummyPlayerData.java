/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.internal;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.spell.SpellPiece;

/**
 * This is a dummy class. You'll never interact with it, it's just here so
 * in case something goes really wrong the field isn't null.
 */
public class DummyPlayerData implements IPlayerData {

	@Override
	public int getTotalPsi() {
		return 0;
	}

	@Override
	public int getAvailablePsi() {
		return 0;
	}

	@Override
	public int getLastAvailablePsi() {
		return 0;
	}

	@Override
	public int getRegenCooldown() {
		return 0;
	}

	@Override
	public int getRegenPerTick() {
		return 0;
	}

	@Override
	public boolean isOverflowed() {
		return false;
	}

	@Override
	public void deductPsi(int psi, int cd, boolean sync, boolean shatter) {
		// NO-OP
	}

	@Override
	public boolean isPieceGroupUnlocked(ResourceLocation group, @Nullable ResourceLocation piece) {
		return false;
	}

	@Override
	public void unlockPieceGroup(ResourceLocation group) {
		// NO-OP
	}

	@Override
	public void markPieceExecuted(SpellPiece piece) {
		// NO-OP
	}

	@Override
	public CompoundTag getCustomData() {
		return null;
	}

	@Override
	public void save() {
		// NO-OP
	}

}
