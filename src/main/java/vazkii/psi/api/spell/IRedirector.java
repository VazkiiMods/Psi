/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 15:20:01 (GMT)]
 */
package vazkii.psi.api.spell;

/**
 * Interface for a SpellPiece that redirects parameters. Used for Connectors.
 */
public interface IRedirector extends IGenericRedirector {

	SpellParam.Side getRedirectionSide();

	@Override
	default SpellParam.Side remapSide(SpellParam.Side inputSide) {
		return getRedirectionSide();
	}
}
