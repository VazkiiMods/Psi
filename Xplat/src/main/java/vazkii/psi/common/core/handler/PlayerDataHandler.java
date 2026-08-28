/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.common.network.PsiNetwork;
import vazkii.psi.common.network.message.MessageTriggerJumpSpell;
import vazkii.psi.common.platform.PsiPlayerDataSync;

public final class PlayerDataHandler {
	private PlayerDataHandler() {}

	@NotNull
	public static PlayerData get(Player player) {
		return PsiPlayerData.get(player);
	}

	public static void onPlayerTick(Player player) {
		if(player == null || player.isSpectator()) {
			return;
		}

		ItemStack cadStack = PsiAPI.getPlayerCAD(player);
		if(!cadStack.isEmpty() && cadStack.getItem() instanceof ICAD cad && PsiAPI.canCADBeUpdated(player)) {
			cad.incrementTime(cadStack);
		}

		PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.TICK));
		get(player).tick();
	}

	public static void onEntityDamage(LivingEntity entity, DamageSource source, float damage) {
		if(!(entity instanceof Player player)) {
			return;
		}

		get(player).damage(damage);
		LivingEntity attacker = source.getEntity() instanceof LivingEntity living ? living : null;
		PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.DAMAGE, damage, attacker));
		if(source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.IN_FIRE)) {
			PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.ON_FIRE));
		}
	}

	public static boolean blocksArmorStandInteraction(Player player, Entity target, InteractionHand hand) {
		if(!player.isSecondaryUseActive() || !(target instanceof ArmorStand)) {
			return false;
		}
		ItemStack held = player.getItemInHand(hand);
		return PsiAPI.getPlayerCAD(player) == held;
	}

	public static void onPlayerLogin(ServerPlayer player) {
		CompoundTag dataTag = new CompoundTag();
		get(player).writeToNBT(dataTag);
		PsiPlayerDataSync.sendFull(player, dataTag);
	}

	public static void onEntityJump(Player player) {
		if(player.level().isClientSide && !player.isSpectator()) {
			PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.JUMP));
			PsiNetwork.sendToServer(new MessageTriggerJumpSpell());
		}
	}

	public static void onChangeDimension(Player player) {
		get(player).eidosChangelog.clear();
	}
}
