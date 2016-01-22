/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
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
	}
}
