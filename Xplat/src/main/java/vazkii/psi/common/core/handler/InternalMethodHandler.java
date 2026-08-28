/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.internal.IInternalMethodHandler;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;
import vazkii.psi.common.crafting.recipe.DimensionTrickRecipe;
import vazkii.psi.common.crafting.recipe.TrickRecipe;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModDataComponents;
import vazkii.psi.common.network.PsiNetwork;
import vazkii.psi.common.network.message.MessageSpellError;
import vazkii.psi.common.spell.SpellCache;
import vazkii.psi.common.spell.SpellCompiler;

import java.util.List;

public final class InternalMethodHandler implements IInternalMethodHandler {

	@Override
	public IPlayerData getDataForPlayer(Player player) {
		return PsiPlayerData.get(player);
	}

	@Override
	public ISpellCompiler getCompiler() {
		return new SpellCompiler();
	}

	@Override
	public ISpellCache getSpellCache() {
		return SpellCache.instance;
	}

	@Override
	public void delayContext(SpellContext context) {
		if(!context.caster.level().isClientSide) {
			DelayedSpellHandler.delay(context);
		}
	}

	@Override
	public void setCrashData(CompiledSpell spell, SpellPiece piece) {
		CrashReportHandler.setSpell(spell, piece);
	}

	@Override
	public ItemStack createDefaultCAD(List<ItemStack> components) {
		return ItemCAD.makeCAD(components);
	}

	@Override
	public ItemStack createCAD(ItemStack base, List<ItemStack> components) {
		return ItemCAD.makeCAD(base, components);
	}

	@Override
	public List<Item> getCADComponents(ItemStack cad) {
		return cad.get(ModDataComponents.COMPONENTS.get());
	}

	@Override
	public void setCADComponents(ItemStack cad, List<Item> components) {
		cad.set(ModDataComponents.COMPONENTS.get(), components);
	}

	@Override
	public void sendSpellError(Player player, int x, int y) {
		PsiNetwork.sendToPlayer((ServerPlayer) player,
				new MessageSpellError("psi.spellerror.position", x, y));
	}

	@Override
	public Recipe<?> createTrickRecipe(@Nullable PieceCraftingTrick piece, Ingredient input, ItemStack output,
			ItemStack cadAssembly, @Nullable ResourceKey<Level> dimension) {
		return dimension == null
				? new TrickRecipe(piece, input, output, cadAssembly)
				: new DimensionTrickRecipe(piece, input, output, cadAssembly, dimension);
	}
}
