/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.piece;

import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.StatLabel;

import java.util.EnumMap;
import java.util.Map;

public record ScriptPieceStats(Map<EnumSpellStat, Integer> values) {

	public static ScriptPieceStats of(int complexity, int potency, int cost, int projection, int bandwidth) {
		Map<EnumSpellStat, Integer> values = new EnumMap<>(EnumSpellStat.class);
		values.put(EnumSpellStat.COMPLEXITY, complexity);
		values.put(EnumSpellStat.POTENCY, potency);
		values.put(EnumSpellStat.COST, cost);
		values.put(EnumSpellStat.PROJECTION, projection);
		values.put(EnumSpellStat.BANDWIDTH, bandwidth);
		return new ScriptPieceStats(values);
	}

	public void addTo(SpellMetadata meta) throws SpellCompilationException {
		for(Map.Entry<EnumSpellStat, Integer> entry : values.entrySet()) {
			meta.addStat(entry.getKey(), entry.getValue());
		}
	}

	public void label(SpellPiece piece) {
		for(Map.Entry<EnumSpellStat, Integer> entry : values.entrySet()) {
			piece.setStatLabel(entry.getKey(), entry.getValue() == 0 ? null : new StatLabel(entry.getValue()));
		}
	}

}
