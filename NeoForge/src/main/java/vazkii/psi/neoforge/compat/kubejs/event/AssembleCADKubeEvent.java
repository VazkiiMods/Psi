/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.cad.AssembleCADEvent;
import vazkii.psi.api.cad.ITileCADAssembler;

public class AssembleCADKubeEvent extends PsiKubeEvent {

	private final AssembleCADEvent event;

	public AssembleCADKubeEvent(AssembleCADEvent event) {
		super(event);
		this.event = event;
	}

	public ITileCADAssembler getAssembler() {
		return event.getAssembler();
	}

	public ItemStack getCad() {
		return event.getCad();
	}

	public void setCad(ItemStack cad) {
		event.setCad(cad);
	}

	public Player getPlayer() {
		return event.getPlayer();
	}

}
