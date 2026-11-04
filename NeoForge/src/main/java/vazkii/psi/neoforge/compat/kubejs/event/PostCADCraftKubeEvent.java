/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.event;

import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.cad.ITileCADAssembler;
import vazkii.psi.api.cad.PostCADCraftEvent;

public class PostCADCraftKubeEvent extends PsiKubeEvent {

	private final PostCADCraftEvent event;

	public PostCADCraftKubeEvent(PostCADCraftEvent event) {
		super(event);
		this.event = event;
	}

	public ITileCADAssembler getAssembler() {
		return event.getAssembler();
	}

	public ItemStack getCad() {
		return event.getCad();
	}

}
