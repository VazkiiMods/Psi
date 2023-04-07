/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.mixin.client;

import net.minecraft.client.renderer.RenderStateShard;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderStateShard.class)
public interface AccessorRenderState {
	@Accessor("TRANSLUCENT_TRANSPARENCY")
	static RenderStateShard.TransparencyStateShard getTranslucentTransprency() {
		throw new IllegalStateException();
	}
}
