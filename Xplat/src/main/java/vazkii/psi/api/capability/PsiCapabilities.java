/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.capability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADData;
import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.ISpellImmune;
import vazkii.psi.api.spell.detonator.IDetonationHandler;
import vazkii.psi.common.core.handler.PsiPlayerData;
import vazkii.psi.common.platform.PsiLookups;

public final class PsiCapabilities {

	public static final ResourceLocation SPELL_IMMUNE = PsiAPI.location("spell_immune");
	public static final ResourceLocation DETONATION_HANDLER = PsiAPI.location("detonation_handler");
	public static final ResourceLocation PSI_BAR_DISPLAY = PsiAPI.location("psi_bar_display");
	public static final ResourceLocation SPELL_ACCEPTOR = PsiAPI.location("spell_acceptor");
	public static final ResourceLocation CAD_DATA = PsiAPI.location("cad_data");
	public static final ResourceLocation SOCKETABLE = PsiAPI.location("socketable");
	public static final ResourceLocation PLAYER_DATA = PsiAPI.location("player_data");

	private PsiCapabilities() {}

	@Nullable
	public static ISpellImmune spellImmune(Entity entity) {
		return PsiLookups.findEntity(SPELL_IMMUNE, ISpellImmune.class, entity);
	}

	@Nullable
	public static IDetonationHandler detonationHandler(Entity entity) {
		return PsiLookups.findEntity(DETONATION_HANDLER, IDetonationHandler.class, entity);
	}

	@Nullable
	public static IPsiBarDisplay psiBarDisplay(ItemStack stack) {
		return PsiLookups.findItem(PSI_BAR_DISPLAY, IPsiBarDisplay.class, stack);
	}

	@Nullable
	public static ISpellAcceptor spellAcceptor(ItemStack stack) {
		return PsiLookups.findItem(SPELL_ACCEPTOR, ISpellAcceptor.class, stack);
	}

	@Nullable
	public static ICADData cadData(ItemStack stack) {
		return PsiLookups.findItem(CAD_DATA, ICADData.class, stack);
	}

	@Nullable
	public static ISocketable socketable(ItemStack stack) {
		return PsiLookups.findItem(SOCKETABLE, ISocketable.class, stack);
	}

	public static IPlayerData playerData(Player player) {
		return PsiPlayerData.get(player);
	}

}
