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
 * Interface for a SpellPiece that redirects parameters. Used for Connectors.
 */
public interface IRedirector extends IGenericRedirector {

	SpellParam.Side getRedirectionSide();

	@Override
	default SpellParam.Side remapSide(SpellParam.Side inputSide) {
		return getRedirectionSide();
	}
}
