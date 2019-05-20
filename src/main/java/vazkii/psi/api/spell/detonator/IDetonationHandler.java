/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * <p>
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * <p>
 * File Created @ [May 19, 2019, 23:14 AM (EST)]
 */
package vazkii.psi.api.spell.detonator;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 * The handler for an object's detonation behavior.
 *
 * Typically only seen on entities, but can be implemented
 */
public interface IDetonationHandler {

	@CapabilityInject(IDetonationHandler.class)
	Capability<IDetonationHandler> CAPABILITY = null;

	static boolean canBeDetonated(Entity entity) {
		return entity.hasCapability(CAPABILITY, null);
	}

	static IDetonationHandler detonator(Entity entity) {
		return entity.getCapability(CAPABILITY, null);
	}

	void detonate();
}
