/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import vazkii.psi.api.cad.EnumCADStat;

import java.util.Locale;

/**
 * An Enum defining all spell stats and the CAD stats to compare against.
 */
public enum EnumSpellStat {

    COMPLEXITY(EnumCADStat.COMPLEXITY),
    POTENCY(EnumCADStat.POTENCY),
    COST(null),
    PROJECTION(EnumCADStat.PROJECTION),
    BANDWIDTH(EnumCADStat.BANDWIDTH);

    private final EnumCADStat target;

    EnumSpellStat(EnumCADStat target) {
        this.target = target;
    }

    public EnumCADStat getTarget() {
        return target;
    }

    public String getName() {
        return "psi.spellstat." + name().toLowerCase(Locale.ROOT);
    }

    public String getDesc() {
        return getName() + ".desc";
    }

}
