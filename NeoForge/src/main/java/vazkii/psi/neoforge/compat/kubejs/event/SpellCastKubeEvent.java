/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCastEvent;
import vazkii.psi.api.spell.SpellContext;

public class SpellCastKubeEvent extends PsiKubeEvent {

	private final SpellCastEvent event;

	public SpellCastKubeEvent(SpellCastEvent event) {
		super(event);
		this.event = event;
	}

	public Spell getSpell() {
		return event.spell;
	}

	public SpellContext getContext() {
		return event.context;
	}

	public Player getPlayer() {
		return event.player;
	}

	public IPlayerData getPlayerData() {
		return event.playerData;
	}

	public ItemStack getCad() {
		return event.cad;
	}

	public ItemStack getBullet() {
		return event.bullet;
	}

}
