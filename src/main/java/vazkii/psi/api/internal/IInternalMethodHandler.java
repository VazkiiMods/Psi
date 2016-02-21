/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [13/01/2016, 16:58:15 (GMT)]
 */
package vazkii.psi.api.internal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.api.spell.ISpellCache;
import vazkii.psi.api.spell.ISpellCompiler;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

public interface IInternalMethodHandler {

	/**
	 * Gets the player data for a given player. Player Data contains info such as the
	 * player's Psi value or level.
	 */
	public IPlayerData getDataForPlayer(EntityPlayer player);

	/**
	 * Gets the texture for the programmer. Used for drawing the arrows in a SpellPiece's
	 * params.
	 */
	public ResourceLocation getProgrammerTexture();

	/**
	 * Gets an instance of a spell compiler. In most cases, you should use {@link #getSpellCache()} instead.
	 */
	public ISpellCompiler getCompiler(Spell spell);

	/**
	 * Gets the singleton instance of the spell cache.
	 */
	public ISpellCache getSpellCache();

	/**
	 * Delays a spell context.
	 */
	public void delayContext(SpellContext context);
}
