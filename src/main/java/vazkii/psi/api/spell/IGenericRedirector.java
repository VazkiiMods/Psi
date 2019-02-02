/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * <p>
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * <p>
 * File Created @ [Feb 02, 2019, 12:01 AM (EST)]
 */
package vazkii.psi.api.spell;

/**
 * Interface for a SpellPiece that proxies for another piece. Used for Connectors.
 */
public interface IGenericRedirector {

	SpellParam.Side remapSide(SpellParam.Side inputSide);

}
