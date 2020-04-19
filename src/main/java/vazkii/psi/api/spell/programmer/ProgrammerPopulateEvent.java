/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell.programmer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraftforge.eventbus.api.Event;

import vazkii.psi.api.spell.SpellPiece;

import javax.annotation.Nonnull;

@Event.HasResult
public class ProgrammerPopulateEvent extends Event {

	@Nonnull
	private final PlayerEntity entity;
	@Nonnull
	private SimpleRegistry<Class<? extends SpellPiece>> spellPieceRegistry;

	public ProgrammerPopulateEvent(PlayerEntity entity, SimpleRegistry<Class<? extends SpellPiece>> registry) {
		this.entity = entity;
		this.spellPieceRegistry = registry;
	}

	@Nonnull
	public PlayerEntity getPlayer() {
		return entity;
	}

	@Nonnull
	public SimpleRegistry<Class<? extends SpellPiece>> getSpellPieceRegistry() {
		return spellPieceRegistry;
	}

	@Nonnull
	public void setSpellPieceRegistry(SimpleRegistry<Class<? extends SpellPiece>> registry) {
		spellPieceRegistry = registry;
	}

}
