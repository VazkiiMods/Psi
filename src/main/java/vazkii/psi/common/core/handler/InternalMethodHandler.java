/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.psi.api.internal.IInternalMethodHandler;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.CompiledSpell;
import vazkii.psi.api.spell.ISpellCache;
import vazkii.psi.api.spell.ISpellCompiler;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.spell.SpellCache;
import vazkii.psi.common.spell.SpellCompiler;

import java.util.List;

public final class InternalMethodHandler implements IInternalMethodHandler {

	@Override
	public IPlayerData getDataForPlayer(Player player) {
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
			PlayerDataHandler.delayedContexts.add(context);
		}
	}

	@Override
	public void setCrashData(CompiledSpell spell, SpellPiece piece) {
		CrashReportHandler.setSpell(spell, piece);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderTooltip(GuiGraphics graphics, int x, int y, List<Component> tooltipData, int color, int color2, int width, int height) {
		if(!tooltipData.isEmpty()) {
			Font fontRenderer = Minecraft.getInstance().font;
			Screen screen = Minecraft.getInstance().screen;
			assert screen != null;
			graphics.renderTooltip(fontRenderer, tooltipData, java.util.Optional.empty(), x, y);//TODO Fix color/color2? Is it needed?
			//GuiUtils.drawHoveringText(ms, tooltipData, x, y, width, height, -1, color2, color, color, fontRenderer);
		}
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
