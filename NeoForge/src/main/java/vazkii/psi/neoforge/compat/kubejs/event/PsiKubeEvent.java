/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.event;

import vazkii.psi.api.event.PsiEvent;

import dev.latvian.mods.kubejs.event.KubeEvent;

public abstract class PsiKubeEvent implements KubeEvent {

	private final PsiEvent event;

	protected PsiKubeEvent(PsiEvent event) {
		this.event = event;
	}

	public boolean isCanceled() {
		return event.isCanceled();
	}

	public void setCanceled(boolean canceled) {
		event.setCanceled(canceled);
	}

}
