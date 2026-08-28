/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.gui;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.event.PsiEvent;

/**
 * Posted when a part of the HUD is about to be rendered
 * <p>
 * This event is {@link ICancellableEvent}.
 * Canceling it will result in that HUD part not being rendered
 */
public class RenderPsiHudEvent extends PsiEvent {

	@NotNull
	private final PsiHudElementType type;

	public RenderPsiHudEvent(@NotNull PsiHudElementType type) {
		this.type = type;
	}

	@NotNull
	public PsiHudElementType getType() {
		return type;
	}
}
