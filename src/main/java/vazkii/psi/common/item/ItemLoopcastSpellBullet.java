/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.LoopcastTrackingHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import java.util.HashSet;
import java.util.Set;

public class ItemLoopcastSpellBullet extends ItemSpellBullet {

	public ItemLoopcastSpellBullet(Properties properties) {
		super(properties);
	}

	@Override
	public Set<Entity> castSpell(ItemStack stack, SpellContext context) {
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(context.caster);
		if (!data.loopcasting || context.castFrom != data.loopcastHand) {
			context.cspell.safeExecute(context);
			data.loopcasting = true;
			data.loopcastHand = context.castFrom;
			data.lastTickLoopcastStack = null;
			if (context.caster instanceof ServerPlayerEntity) {
				LoopcastTrackingHandler.syncForTrackersAndSelf((ServerPlayerEntity) context.caster);
			}
		}
		return new HashSet<>();
	}

	@Override
	public String getBulletType() {
		return "loopcast";
	}

	@Override
	public boolean isCADOnlyContainer(ItemStack stack) {
		return true;
	}

	@Override
	public double getCostModifier(ItemStack stack) {
		return 1.0;
	}
}
