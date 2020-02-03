/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [13/01/2016, 17:01:34 (GMT)]
 */
package vazkii.psi.api.internal;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.spell.*;

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
	public void renderTooltip(int x, int y, List<ITextComponent> tooltipData, int color, int color2) {
		// NO-OP
	}

	@Override
	public String localize(String key, Object... format) {
		return key;
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
