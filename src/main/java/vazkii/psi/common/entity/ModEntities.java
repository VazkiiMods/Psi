/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [20/01/2016, 23:07:34 (GMT)]
 */
package vazkii.psi.common.entity;

import net.minecraftforge.fml.common.registry.EntityRegistry;
import vazkii.psi.common.Psi;
import vazkii.psi.common.lib.LibEntityNames;

public final class ModEntities {

	public static void init() {
		int id = 0;

		EntityRegistry.registerModEntity(EntitySpellProjectile.class, LibEntityNames.SPELL_PROJECTILE, id++, Psi.instance, 256, 10, true);
		EntityRegistry.registerModEntity(EntitySpellCircle.class, LibEntityNames.SPELL_CIRCLE, id++, Psi.instance, 256, 64, false);
		EntityRegistry.registerModEntity(EntitySpellGrenade.class, LibEntityNames.SPELL_GRENADE, id++, Psi.instance, 256, 10, true);
	}
}
