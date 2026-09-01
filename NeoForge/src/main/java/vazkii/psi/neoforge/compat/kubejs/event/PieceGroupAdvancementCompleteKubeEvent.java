/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.spell.PieceGroupAdvancementComplete;
import vazkii.psi.api.spell.SpellPiece;

public class PieceGroupAdvancementCompleteKubeEvent extends PsiKubeEvent {

	private final PieceGroupAdvancementComplete event;

	public PieceGroupAdvancementCompleteKubeEvent(PieceGroupAdvancementComplete event) {
		super(event);
		this.event = event;
	}

	public ResourceLocation getPieceGroup() {
		return event.getPieceGroup();
	}

	@Nullable
	public SpellPiece getPiece() {
		return event.getPiece();
	}

	public Player getPlayer() {
		return event.getPlayerEntity();
	}

}
