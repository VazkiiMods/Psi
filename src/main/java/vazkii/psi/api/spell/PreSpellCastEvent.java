/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import vazkii.psi.api.internal.IPlayerData;

import javax.annotation.Nullable;

/**
 * Posted before a spell executes.
 * <p>
 * This event is {@link Cancelable}.
 */
@Cancelable
public class PreSpellCastEvent extends Event {

	private int cost;
	private float sound;
	private int particles;
	private int cooldown;

	private Spell spell;
	private SpellContext context;

	private final Player player;
	private final IPlayerData playerData;
	private final ItemStack cad;
	private final ItemStack bullet;

	@Nullable
	private String cancellationMessage = "psimisc.canceled_spell";

	public PreSpellCastEvent(int cost, float sound, int particles, int cooldown, Spell spell, SpellContext context, Player player, IPlayerData playerData, ItemStack cad, ItemStack bullet) {
		this.cost = cost;
		this.sound = sound;
		this.particles = particles;
		this.cooldown = cooldown;
		this.spell = spell;
		this.context = context;
		this.player = player;
		this.playerData = playerData;
		this.cad = cad;
		this.bullet = bullet;
	}

	@Nullable
	public String getCancellationMessage() {
		return cancellationMessage;
	}

	public void setCancellationMessage(@Nullable String cancellationMessage) {
		this.cancellationMessage = cancellationMessage;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public float getSound() {
		return sound;
	}

	public void setSound(float sound) {
		this.sound = sound;
	}

	public int getParticles() {
		return particles;
	}

	public void setParticles(int particles) {
		this.particles = particles;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public Spell getSpell() {
		return spell;
	}

	public void setSpell(Spell spell) {
		this.spell = spell;
		this.context.setSpell(spell);
	}

	public SpellContext getContext() {
		return context;
	}

	public void setContext(SpellContext context) {
		this.context = context;
	}

	public Player getPlayer() {
		return player;
	}

	public IPlayerData getPlayerData() {
		return playerData;
	}

	public ItemStack getCad() {
		return cad;
	}

	public ItemStack getBullet() {
		return bullet;
	}
}
