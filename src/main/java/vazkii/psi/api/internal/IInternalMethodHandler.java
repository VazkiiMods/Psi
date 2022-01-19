/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.internal;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.psi.api.spell.CompiledSpell;
import vazkii.psi.api.spell.ISpellCache;
import vazkii.psi.api.spell.ISpellCompiler;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellPiece;

import java.util.List;

public interface IInternalMethodHandler {

	/**
	 * Gets the player data for a given player. Player Data contains info such as the
	 * player's Psi value or level.
	 */
	IPlayerData getDataForPlayer(Player player);

	/**
	 * Gets the texture for the programmer. Used for drawing the arrows in a SpellPiece's
	 * params.
	 */
	ResourceLocation getProgrammerTexture();

	/**
	 * Gets the render layer for the programmer.
	 */
	@OnlyIn(Dist.CLIENT)
	RenderType getProgrammerLayer();

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
	 * Renders a tooltip with the specified colors at the given x,y position
	 */
	@OnlyIn(Dist.CLIENT)
	void renderTooltip(PoseStack ms, int x, int y, List<Component> tooltipData, int color, int color2, int width, int height);

	/**
	 * Creates a CAD with the given components
	 */
	ItemStack createDefaultCAD(List<ItemStack> components);

	/**
	 * Creates a CAD with the Assembly ItemStack as a base and the components array as its components
	 */
	ItemStack createCAD(ItemStack base, List<ItemStack> components);
}
