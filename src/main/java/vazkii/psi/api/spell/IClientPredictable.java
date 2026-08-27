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
 * Explicitly opts a spell piece into speculative execution on the casting client.
 * Implementations must only make changes that normal server synchronization can
 * reconcile. The server remains authoritative and executes the piece normally.
 * Merely registering a spell piece never enables prediction.
 */
public interface IClientPredictable {

	/**
	 * Executes the recoverable, client-only form of this piece's effect.
	 */
	Object executePrediction(SpellContext context) throws SpellRuntimeException;

}
