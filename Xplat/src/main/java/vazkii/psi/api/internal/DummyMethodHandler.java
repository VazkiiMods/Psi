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

/**
 * This is a dummy class. You'll never interact with it, it's just here so
 * in case something goes really wrong the field isn't null.
 */
public final class DummyMethodHandler implements IInternalMethodHandler {

	@Override
	public IPlayerData getDataForPlayer(Player player) {
		return new DummyPlayerData();
	}

	@Override
	public ISpellCompiler getCompiler() {
		return null;
	}

	@Override
	public ISpellCache getSpellCache() {
		return null;
	}

	@Override
	public void delayContext(SpellContext context) {
		// NO-OP
	}

	@Override
	public void setCrashData(CompiledSpell spell, SpellPiece piece) {
		// NO-OP
	}

	@Override
	public ItemStack createDefaultCAD(List<ItemStack> components) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack createCAD(ItemStack base, List<ItemStack> components) {
		return ItemStack.EMPTY;
	}

	@Override
	public List<Item> getCADComponents(ItemStack cad) {
		return List.of();
	}

	@Override
	public void setCADComponents(ItemStack cad, List<Item> components) {
		// NO-OP
	}

	@Override
	public void sendSpellError(Player player, int x, int y) {
		// NO-OP
	}

	@Override
	public Recipe<?> createTrickRecipe(@Nullable PieceCraftingTrick piece, Ingredient input, ItemStack output,
			ItemStack cadAssembly, @Nullable ResourceKey<Level> dimension) {
		throw new IllegalStateException("Psi has not initialized its internal method handler");
	}
}
