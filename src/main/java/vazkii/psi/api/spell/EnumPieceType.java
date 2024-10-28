/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

/**
 * An Enum defining the various spell piece types.
 */
public enum EnumPieceType {

    SELECTOR,
    OPERATOR,
    CONSTANT,
    CONNECTOR,
    MODIFIER, // eg: Error Suppressor
    TRICK,
    ERROR_HANDLER;

    public boolean isTrick() {
        return this == TRICK || this == MODIFIER;
    }

}
