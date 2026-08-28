/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.internal;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;

import java.util.List;

public interface IInternalMethodHandler {

	/**
	 * Gets the player data for a given player. Player Data contains info such as the
	 * player's Psi value or level.
	 */
	IPlayerData getDataForPlayer(Player player);

	/**
	 * Gets an instance of a spell compiler. In most cases, you should use {@link #getSpellCache()} instead.
	 */
	ISpellCompiler getCompiler();

	/**
	 * Gets the singleton instance of the spell cache.
	 */
	ISpellCache getSpellCache();

	/**
	 * Delays a spell context.
	 */
	void delayContext(SpellContext context);

	/**
	 * Sets the crash handler data, in case the spell hard-crashes.
	 */
	void setCrashData(CompiledSpell spell, SpellPiece piece);

	/**
	 * Creates a CAD with the given components
	 */
	ItemStack createDefaultCAD(List<ItemStack> components);

	/**
	 * Creates a CAD with the Assembly ItemStack as a base and the components array as its components
	 */
	ItemStack createCAD(ItemStack base, List<ItemStack> components);

	List<Item> getCADComponents(ItemStack cad);

	void setCADComponents(ItemStack cad, List<Item> components);

	void sendSpellError(Player player, int x, int y);

	Recipe<?> createTrickRecipe(@Nullable PieceCraftingTrick piece, Ingredient input, ItemStack output,
			ItemStack cadAssembly, @Nullable ResourceKey<Level> dimension);
}
