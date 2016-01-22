/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [16/01/2016, 19:56:25 (GMT)]
 */
package vazkii.psi.api.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.common.spell.SpellCache;

public final class SpellContext {

	public static final double MAX_DISTANCE = 32;
	
	public EntityPlayer caster;
	public Entity focalPoint; 
	public CompiledSpell cspell;
	
	public SpellContext setPlayer(EntityPlayer player) {
		caster = player;
		return setFocalPoint(player);
	}
	
	public SpellContext setFocalPoint(Entity e) {
		focalPoint = e;
		return this;
	}
	
	public SpellContext setCompiledSpell(CompiledSpell spell) {
		cspell = spell;
		return this;
	}
	
	public SpellContext setSpell(Spell spell) {
		setCompiledSpell(SpellCache.instance.getCompiledSpell(spell));
		return this;
	}

	public boolean isValid() {
		return cspell != null;
	}
	
	public boolean isInRadius(Vector3 vec) {
		return isInRadius(vec.x, vec.y, vec.z);
	}
	
	public boolean isInRadius(Entity e) {
		return isInRadius(e.posX, e.posY, e.posZ); 
	}
	
	public boolean isInRadius(double x, double y, double z) {
		return pointDistanceSpace(x, y, z, focalPoint.posX, focalPoint.posY, focalPoint.posZ) <= MAX_DISTANCE; 
	}
	
	public static double pointDistanceSpace(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2));
	}
	
}
