/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import net.minecraft.world.item.ItemStack;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Metadata for a given spell. Basically just a fancy holder for a map of the spell's
 * stats.
 */
public final class SpellMetadata {

	private final Map<EnumSpellStat, Integer> stats = new EnumMap<>(EnumSpellStat.class);
	private final Map<EnumSpellStat, Double> statMultipliers = new EnumMap<>(EnumSpellStat.class);
	/**
	 * Should errors from this spell not be sent to the player's chat?
	 */
	public boolean errorsSuppressed = false;
	private final Set<String> flags = new HashSet<>();

	public SpellMetadata() {
		for(EnumSpellStat stat : EnumSpellStat.class.getEnumConstants()) {
			stats.put(stat, 0);
			statMultipliers.put(stat, 1.0);
		}
	}

	/**
	 * Adds a stat to the metadata, incrementing over the previous value.
	 */
	public void addStat(EnumSpellStat stat, int val) throws SpellCompilationException {
		int curr = stats.get(stat);
		boolean overflow = false;
		try {
			setStat(stat, Math.addExact(val, curr));
		} catch (ArithmeticException exception) {
			overflow = true;
		}
		if(overflow) {
			throw new SpellCompilationException(SpellCompilationException.STAT_OVERFLOW);
		}
	}

	/**
	 * Sets a stat's value. No consideration over the previous value is done, so
	 * unless you really want to be weird, use {@link #addStat(EnumSpellStat, int)} instead.
	 */
	public void setStat(EnumSpellStat stat, int val) {
		stats.put(stat, val);
	}

	/**
	 * Gets a stat's value (including multiplier) from the metadata.
	 */
	public int getStat(EnumSpellStat stat) {

		return (int) (stats.get(stat) * statMultipliers.get(stat));
	}

	/**
	 * Multiplies current stat multiplier by new value.
	 */
	public void compoundStatMultiplier(EnumSpellStat stat, double val) {
		double curr = statMultipliers.get(stat);
		setStatMultiplier(stat, val * curr);
	}

	/**
	 * Adds to stat multiplier metadata, incrementing over the previous value.
	 */
	public void addStatMultiplier(EnumSpellStat stat, double val) {
		double curr = statMultipliers.get(stat);
		setStatMultiplier(stat, val + curr);
	}

	/**
	 * Sets a stat's multiplier. No consideration over the previous value is done, so
	 * unless you really want to be weird, use {@link #addStatMultiplier(EnumSpellStat, double)} instead.
	 */
	public void setStatMultiplier(EnumSpellStat stat, double val) {
		statMultipliers.put(stat, val);
	}

	/**
	 * Gets a stat's multiplier from the metadata.
	 */
	public double getStatMultiplier(EnumSpellStat stat) {
		return statMultipliers.get(stat);
	}

	/**
	 * Should be equivalent to EnumSpellStat.class.getEnumConstants()
	 */
	public Set<EnumSpellStat> getStatSet() {
		return stats.keySet();
	}

	/**
	 * Sets a flag in the metadata.
	 */
	public void setFlag(String flag, boolean val) {
		if(val) {
			flags.add(flag);
		} else {
			flags.remove(flag);
		}
	}

	/**
	 * Returns if a flag exists in the metadata.
	 */
	public boolean getFlag(String flag) {
		return flags.contains(flag);
	}

	/**
	 * Evaluates this metadata's stats against a passed in stack (whose item must be an implementation
	 * of {@link ICAD}). Returns true if the stats are equal to or above the CAD.
	 */
	public boolean evaluateAgainst(ItemStack stack) {
		if(stack == null || !(stack.getItem() instanceof ICAD cad)) {
			return false;
		}

		for(EnumSpellStat stat : stats.keySet()) {
			EnumCADStat cadStat = stat.getTarget();
			if(cadStat == null) {
				continue;
			}

			int statVal = (int) Math.ceil(stats.get(stat) * statMultipliers.get(stat));
			int cadVal = cad.getStatValue(stack, cadStat);
			if(cadVal != -1 && cadVal < statVal) {
				return false;
			}
		}

		return true;
	}

}
