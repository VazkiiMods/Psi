/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.arl.util.RenderHelper;
import vazkii.psi.api.internal.IInternalMethodHandler;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.CompiledSpell;
import vazkii.psi.api.spell.ISpellCache;
import vazkii.psi.api.spell.ISpellCompiler;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.spell.SpellCache;
import vazkii.psi.common.spell.SpellCompiler;

import java.util.List;
import java.util.stream.Collectors;

public final class InternalMethodHandler implements IInternalMethodHandler {

	@Override
	public IPlayerData getDataForPlayer(PlayerEntity player) {
		return PlayerDataHandler.get(player);
	}

	@Override
	public ResourceLocation getProgrammerTexture() {
		return GuiProgrammer.texture;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public RenderType getProgrammerLayer() {
		return GuiProgrammer.LAYER;

	}

	@Override
	public ISpellCompiler getCompiler(Spell spell) {
		return new SpellCompiler(spell);
	}

	@Override
	public ISpellCache getSpellCache() {
		return SpellCache.instance;
	}

	@Override
	public void delayContext(SpellContext context) {
		if (!context.caster.world.isRemote) {
			PlayerDataHandler.delayedContexts.add(context);
		}
	}

	@Override
	public void setCrashData(CompiledSpell spell, SpellPiece piece) {
		CrashReportHandler.setSpell(spell, piece);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderTooltip(int x, int y, List<ITextComponent> tooltipData, int color, int color2) {
		RenderHelper.renderTooltip(x, y, tooltipData.stream().map(ITextComponent::getFormattedText).collect(Collectors.toList()), color, color2);
	}

	@Override
	public ItemStack createDefaultCAD(List<ItemStack> components) {
		return ItemCAD.makeCAD(components);
	}

	@Override
	public ItemStack createCAD(ItemStack base, List<ItemStack> components) {
		return ItemCAD.makeCAD(base, components);
	}
}
