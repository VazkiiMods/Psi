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

import vazkii.psi.api.cad.CADStatEvent;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;

public class CADStatKubeEvent extends PsiKubeEvent {

	private final CADStatEvent event;

	public CADStatKubeEvent(CADStatEvent event) {
		super(event);
		this.event = event;
	}

	public EnumCADComponent getStatProvider() {
		return event.getStatProvider();
	}

	public EnumCADStat getStat() {
		return event.getStat();
	}

	public ItemStack getCad() {
		return event.getCad();
	}

	public ItemStack getComponent() {
		return event.getComponent();
	}

	public int getStatValue() {
		return event.getStatValue();
	}

	public void setStatValue(int statValue) {
		event.setStatValue(statValue);
	}

}
