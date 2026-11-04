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

import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.cad.CADTakeEvent;
import vazkii.psi.api.cad.ITileCADAssembler;

public class CADTakeKubeEvent extends PsiKubeEvent {

	private final CADTakeEvent event;

	public CADTakeKubeEvent(CADTakeEvent event) {
		super(event);
		this.event = event;
	}

	@Nullable
	public String getCancellationMessage() {
		return event.getCancellationMessage();
	}

	public void setCancellationMessage(@Nullable String cancellationMessage) {
		event.setCancellationMessage(cancellationMessage);
	}

	public float getSound() {
		return event.getSound();
	}

	public void setSound(float sound) {
		event.setSound(sound);
	}

	public ITileCADAssembler getAssembler() {
		return event.getAssembler();
	}

	public ItemStack getCad() {
		return event.getCad();
	}

	public Player getPlayer() {
		return event.getPlayer();
	}

}
