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

import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.PieceKnowledgeEvent;

public class PieceKnowledgeKubeEvent extends PsiKubeEvent {

	private final PieceKnowledgeEvent event;

	public PieceKnowledgeKubeEvent(PieceKnowledgeEvent event) {
		super(event);
		this.event = event;
	}

	public ResourceLocation getPieceGroup() {
		return event.getPieceGroup();
	}

	@Nullable
	public ResourceLocation getPieceName() {
		return event.getPieceName();
	}

	public Player getPlayer() {
		return event.getPlayer();
	}

	public IPlayerData getData() {
		return event.getData();
	}

	public boolean isUnlocked() {
		return event.isUnlocked();
	}

}
