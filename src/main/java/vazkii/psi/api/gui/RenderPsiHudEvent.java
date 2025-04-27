/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.gui;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

import javax.annotation.Nonnull;

/**
 * Posted when a part of the HUD is about to be rendered
 * <p>
 * This event is {@link Cancelable}.
 * Canceling it will result in that HUD part not being rendered
 */
@OnlyIn(Dist.CLIENT)
public class RenderPsiHudEvent extends Event implements ICancellableEvent {

	@Nonnull
	private final PsiHudElementType type;

	public RenderPsiHudEvent(@Nonnull PsiHudElementType type) {
		this.type = type;
	}

	@Nonnull
	public PsiHudElementType getType() {
		return type;
	}
}
