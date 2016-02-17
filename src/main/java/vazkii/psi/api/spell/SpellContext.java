/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 19:56:25 (GMT)]
 */
package vazkii.psi.api.spell;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.MathHelper;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.CompiledSpell.Action;

/**
 * Context for a spell. Used for casting it.
 */
public final class SpellContext {

	/**
	 * The maximum distance from the spell's {@link #focalPoint} a piece of the spell can interact with.<br>
	 * This should be checked against in any tricks that affect parts of the world given a position
	 * or entity.
	 * @see {@link #isInRadius(Entity)}, {@link #isInRadius(Vector3)}, {@link #isInRadius(double, double, double)}
	 */
	public static final double MAX_DISTANCE = 32;

	/**
	 * The player casting this spell.
	 */
	public EntityPlayer caster;

	/**
	 * The focal point of this spell. This can be the same as {@link #caster}, but will often be different,
	 * like in cases where the spell is executed through a projectile bullet.
	 */
	public Entity focalPoint;

	/**
	 * The compiled spell to execute.
	 */
	public CompiledSpell cspell;

	/**
	 * The loopcast index of this context. This is always 0 when the spell is cast as not a
	 * loopcast. Increments every time for each loopcast iteration.
	 */
	public int loopcastIndex = 0;

	// Tool stuff. Only available if the spell is casted from a Psimetal Tool
	public ItemStack tool;
	public MovingObjectPosition positionBroken;
	// Sword stuff
	public EntityLivingBase attackedEntity;
	

	// Runtime information, do not mess with.
	public Object[][] evaluatedObjects = new Object[SpellGrid.GRID_SIZE][SpellGrid.GRID_SIZE];
	public Stack<Action> actions = null; 

	public boolean stopped = false;
	public int delay = 0;

	/**
	 * A map for custom data where addon authors can put stuff.
	 */
	public Map<String, Object> customData = new HashMap();

	/**
	 * Sets the {@link #caster} and returns itself. This also calls {@link #setFocalPoint(Entity)}.
	 */
	public SpellContext setPlayer(EntityPlayer player) {
		caster = player;
		return setFocalPoint(player);
	}

	/**
	 * Sets the focal point and returns itself.
	 */
	public SpellContext setFocalPoint(Entity e) {
		focalPoint = e;
		return this;
	}

	/**
	 * Set the compiled spell and returns itself. This should only be called
	 * when you already have a compiled spell, for some reason. For any other case,
	 * use {@link #setSpell(Spell)}.
	 */
	public SpellContext setCompiledSpell(CompiledSpell spell) {
		cspell = spell;
		return this;
	}

	/**
	 * Compiles the passed in spell and passes it to {@link #setCompiledSpell(CompiledSpell)}, returns itself.
	 * This will compile a spell or poll the spell cache for it.
	 */
	public SpellContext setSpell(Spell spell) {
		setCompiledSpell(PsiAPI.internalHandler.getSpellCache().getCompiledSpell(spell));
		return this;
	}

	public SpellContext setLoopcastIndex(int i) {
		loopcastIndex = i;
		return this;
	}

	public boolean isValid() {
		return cspell != null;
	}

	public boolean shouldSuppressErrors() {
		return isValid() && cspell.metadata.errorsSuppressed;
	}

	/**
	 * Used to check if a vector is within this context's radius.
	 * @see #MAX_DISTANCE
	 */
	public boolean isInRadius(Vector3 vec) {
		return isInRadius(vec.x, vec.y, vec.z);
	}

	/**
	 * Used to check if an entity is within this context's radius.
	 * @see #MAX_DISTANCE
	 */
	public boolean isInRadius(Entity e) {
		if(e == null)
			return false;
		if(e == focalPoint || e == caster)
			return true;

		return isInRadius(e.posX, e.posY, e.posZ);
	}

	/**
	 * Used to check if an (x,y,z) position is within this context's radius.
	 * @see #MAX_DISTANCE
	 */
	public boolean isInRadius(double x, double y, double z) {
		return MathHelper.pointDistanceSpace(x, y, z, focalPoint.posX, focalPoint.posY, focalPoint.posZ) <= MAX_DISTANCE;
	}

}
