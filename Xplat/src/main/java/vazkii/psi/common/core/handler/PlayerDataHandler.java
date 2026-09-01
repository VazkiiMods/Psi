/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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
import vazkii.psi.api.event.PsiEvents;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.spell.PieceGroupAdvancementComplete;
import vazkii.psi.api.spell.SpellPieceGroup;
import vazkii.psi.common.network.PsiNetwork;
import vazkii.psi.common.network.message.MessageTriggerJumpSpell;
import vazkii.psi.common.platform.PsiPlayerDataSync;

import java.util.function.Predicate;

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
		postAdvancementUnlocks(player, get(player)::hasAdvancement);
	}

	public static void onAdvancementCompleted(ServerPlayer player, ResourceLocation advancement) {
		postAdvancementUnlocks(player, advancement::equals);
	}

	private static void postAdvancementUnlocks(ServerPlayer player, Predicate<ResourceLocation> completed) {
		PlayerData data = get(player);
		Registry<SpellPieceGroup> groups = player.registryAccess().registryOrThrow(PsiAPI.SPELL_PIECE_GROUP_REGISTRY_KEY);
		for(Holder.Reference<SpellPieceGroup> group : groups.holders().toList()) {
			if(!(group.value().unlock() instanceof SpellPieceGroup.Unlock.Advancement(ResourceLocation required)) || !completed.test(required)) {
				continue;
			}

			ResourceLocation groupId = group.key().location();
			if(!data.hasAdvancement(groupId)) {
				PsiEvents.post(new PieceGroupAdvancementComplete(null, player, groupId));
			}
		}
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
