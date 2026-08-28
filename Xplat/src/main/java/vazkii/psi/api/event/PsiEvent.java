/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.event;

public abstract class PsiEvent {

	private boolean canceled;

	public final boolean isCanceled() {
		return canceled;
	}

	public final void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

}
