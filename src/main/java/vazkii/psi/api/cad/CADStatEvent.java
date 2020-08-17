/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * This event is posted when a CAD's stats are queried.
 *
 * The results of this event are not cached.
 * The firing of an {@link CADStatEvent} does not necessarily mean that
 * anything has changed, meaning you shouldn't take in-world actions based on this event.
 *
 * This event is not {@link Cancelable}.
 */
public class CADStatEvent extends Event {

	private final EnumCADComponent statProvider;
	private final EnumCADStat stat;

	private final ItemStack cad;
	private final ItemStack component;

	private int statValue;

	public CADStatEvent(EnumCADStat stat, ItemStack cad, ItemStack component, int statValue) {
		this.statProvider = stat.getSourceType();
		this.stat = stat;
		this.cad = cad;
		this.component = component;
		this.statValue = statValue;
	}

	/**
	 * The type of CAD Component the {@link #getComponent()} is.
	 */
	public EnumCADComponent getStatProvider() {
		return statProvider;
	}

	/**
	 * The stat being evaluated.
	 */
	public EnumCADStat getStat() {
		return stat;
	}

	/**
	 * The CAD which is having its stats evaluated.
	 */
	public ItemStack getCad() {
		return cad;
	}

	/**
	 * The component stack the initial stat value comes from.
	 *
	 * This stack <i>can</i> be empty.
	 */
	public ItemStack getComponent() {
		return component;
	}

	/**
	 * The value of the CAD's stat of type {@link #getStat()}.
	 */
	public int getStatValue() {
		return statValue;
	}

	/**
	 * Modify the value that the CAD has as its stat of type {@link #getStat()}.
	 */
	public void setStatValue(int statValue) {
		this.statValue = statValue;
	}
}
