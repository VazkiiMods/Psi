/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell.programmer;

import net.minecraft.core.MappedRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

import vazkii.psi.api.spell.SpellPiece;

import javax.annotation.Nonnull;

@Event.HasResult
public class ProgrammerPopulateEvent extends Event {

	@Nonnull
	private final Player entity;
	@Nonnull
	private MappedRegistry<Class<? extends SpellPiece>> spellPieceRegistry;

	public ProgrammerPopulateEvent(Player entity, MappedRegistry<Class<? extends SpellPiece>> registry) {
		this.entity = entity;
		this.spellPieceRegistry = registry;
	}

	@Nonnull
	public Player getPlayer() {
		return entity;
	}

	@Nonnull
	public MappedRegistry<Class<? extends SpellPiece>> getSpellPieceRegistry() {
		return spellPieceRegistry;
	}

	@Nonnull
	public void setSpellPieceRegistry(MappedRegistry<Class<? extends SpellPiece>> registry) {
		spellPieceRegistry = registry;
	}

}
