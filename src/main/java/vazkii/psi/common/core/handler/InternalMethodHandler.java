/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [13/01/2016, 17:06:45 (GMT)]
 */
package vazkii.psi.common.core.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.arl.util.RenderHelper;
import vazkii.psi.api.internal.IInternalMethodHandler;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.spell.*;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.spell.SpellCache;
import vazkii.psi.common.spell.SpellCompiler;

import java.util.ArrayList;
import java.util.List;

public final class InternalMethodHandler implements IInternalMethodHandler {

	@Override
	public IPlayerData getDataForPlayer(PlayerEntity player) {
		return PlayerDataHandler.get(player);
	}

	@Override
	public ResourceLocation getProgrammerTexture() {
		//return GuiProgrammer.texture;
		return new ResourceLocation("missingno");
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
		if (!context.caster.world.isRemote)
			PlayerDataHandler.delayedContexts.add(context);
	}

	@Override
	public void setCrashData(CompiledSpell spell, SpellPiece piece) {
		CrashReportHandler.setSpell(spell, piece);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderTooltip(int x, int y, List<ITextComponent> tooltipData, int color, int color2) {
		List<String> badVazkii = new ArrayList<>();
		for (ITextComponent component : tooltipData)
			badVazkii.add(component.toString());
		RenderHelper.renderTooltip(x, y, badVazkii, color, color2);
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
