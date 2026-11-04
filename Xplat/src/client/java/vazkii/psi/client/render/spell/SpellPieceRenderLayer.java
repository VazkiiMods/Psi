/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.render.spell;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.inventory.InventoryMenu;

import vazkii.psi.api.PsiAPI;

public final class SpellPieceRenderLayer {

	private static RenderType layer;
	private static RenderType programmer;

	private SpellPieceRenderLayer() {}

	public static RenderType get() {
		if(layer == null) {
			layer = RenderType.text(InventoryMenu.BLOCK_ATLAS);
		}
		return layer;
	}

	public static RenderType programmer() {
		if(programmer == null) {
			programmer = RenderType.text(PsiAPI.location("textures/gui/programmer.png"));
		}
		return programmer;
	}

}
