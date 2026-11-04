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

import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.PreSpellCastEvent;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

public class PreSpellCastKubeEvent extends PsiKubeEvent {

	private final PreSpellCastEvent event;

	public PreSpellCastKubeEvent(PreSpellCastEvent event) {
		super(event);
		this.event = event;
	}

	@Nullable
	public String getCancellationMessage() {
		return event.getCancellationMessage();
	}

	public void setCancellationMessage(@Nullable String cancellationMessage) {
		event.setCancellationMessage(cancellationMessage);
	}

	public int getCost() {
		return event.getCost();
	}

	public void setCost(int cost) {
		event.setCost(cost);
	}

	public float getSound() {
		return event.getSound();
	}

	public void setSound(float sound) {
		event.setSound(sound);
	}

	public int getParticles() {
		return event.getParticles();
	}

	public void setParticles(int particles) {
		event.setParticles(particles);
	}

	public int getCooldown() {
		return event.getCooldown();
	}

	public void setCooldown(int cooldown) {
		event.setCooldown(cooldown);
	}

	public Spell getSpell() {
		return event.getSpell();
	}

	public void setSpell(Spell spell) {
		event.setSpell(spell);
	}

	public SpellContext getContext() {
		return event.getContext();
	}

	public void setContext(SpellContext context) {
		event.setContext(context);
	}

	public Player getPlayer() {
		return event.getPlayer();
	}

	public IPlayerData getPlayerData() {
		return event.getPlayerData();
	}

	public ItemStack getCad() {
		return event.getCad();
	}

	public ItemStack getBullet() {
		return event.getBullet();
	}

}
