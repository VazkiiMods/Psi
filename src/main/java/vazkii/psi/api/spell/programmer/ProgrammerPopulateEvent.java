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
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.spell.SpellPiece;

public class ProgrammerPopulateEvent extends Event {

	@NotNull
	private final Player entity;
	@NotNull
	private MappedRegistry<Class<? extends SpellPiece>> spellPieceRegistry;

	public ProgrammerPopulateEvent(Player entity, MappedRegistry<Class<? extends SpellPiece>> registry) {
		this.entity = entity;
		this.spellPieceRegistry = registry;
	}

	@NotNull
	public Player getPlayer() {
		return entity;
	}

	@NotNull
	public MappedRegistry<Class<? extends SpellPiece>> getSpellPieceRegistry() {
		return spellPieceRegistry;
	}

	@NotNull
	public void setSpellPieceRegistry(MappedRegistry<Class<? extends SpellPiece>> registry) {
		spellPieceRegistry = registry;
	}

}
