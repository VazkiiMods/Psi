/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.other;

import vazkii.psi.api.spell.*;

public class PieceErrorSuppressor extends SpellPiece {

    public PieceErrorSuppressor(Spell spell) {
        super(spell);
    }

    @Override
    public String getSortingName() {
        return "00000000001";
    }

    @Override
    public void addToMetadata(SpellMetadata meta) {
        meta.errorsSuppressed = true;
    }

    @Override
    public EnumPieceType getPieceType() {
        return EnumPieceType.MODIFIER;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Void.class;
    }

    @Override
    public Object evaluate() {
        return null;
    }

    @Override
    public Object execute(SpellContext context) {
        return null;
    }

}
