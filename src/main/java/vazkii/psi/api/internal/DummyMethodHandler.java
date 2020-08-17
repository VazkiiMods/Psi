/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.internal;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.psi.api.spell.CompiledSpell;
import vazkii.psi.api.spell.ISpellCache;
import vazkii.psi.api.spell.ISpellCompiler;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellPiece;

import java.util.List;

/**
 * This is a dummy class. You'll never interact with it, it's just here so
 * in case something goes really wrong the field isn't null.
 */
public final class DummyMethodHandler implements IInternalMethodHandler {

	@Override
	public IPlayerData getDataForPlayer(PlayerEntity player) {
		return new DummyPlayerData();
	}

	@Override
	public ResourceLocation getProgrammerTexture() {
		return new ResourceLocation("");
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public RenderType getProgrammerLayer() {
		return null;
	}

	@Override
	public ISpellCompiler getCompiler(Spell spell) {
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
	@OnlyIn(Dist.CLIENT)
	public void renderTooltip(int x, int y, List<ITextComponent> tooltipData, int color, int color2, int width, int height) {
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
}
