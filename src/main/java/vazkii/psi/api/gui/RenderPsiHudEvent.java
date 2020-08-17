/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nonnull;

/**
 * Posted when a part of the HUD is about to be rendered
 * <p>
 * This event is {@link Cancelable}.
 * Canceling it will result in that HUD part not being rendered
 */
@OnlyIn(Dist.CLIENT)
@Cancelable
public class RenderPsiHudEvent extends Event {

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
