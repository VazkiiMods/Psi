/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

/**
 * Interface for a SpellPiece that proxies for another piece. Used for Connectors.
 */
public interface IGenericRedirector {

	SpellParam.Side remapSide(SpellParam.Side inputSide);

}
