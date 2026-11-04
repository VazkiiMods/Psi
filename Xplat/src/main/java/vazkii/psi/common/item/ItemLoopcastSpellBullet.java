/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerData;
import vazkii.psi.common.core.handler.PsiPlayerData;
import vazkii.psi.common.platform.PsiPlayerDataSync;

import java.util.ArrayList;

public class ItemLoopcastSpellBullet extends ItemSpellBullet {

	public ItemLoopcastSpellBullet(Properties properties) {
		super(properties);
	}

	@Override
	public ArrayList<Entity> castSpell(ItemStack stack, SpellContext context) {
		PlayerData data = PsiPlayerData.get(context.caster);
		if(!data.loopcasting || context.castFrom != data.loopcastHand) {
			data.loopcasting = true;
			data.loopcastHand = context.castFrom;
			data.lastTickLoopcastStack = null;
			data.loopcastTime = 1;
			data.loopcastAmount = 0;
			data.loopcastFadeTime = 0;
			context.cspell.safeExecute(context);
			if(context.caster instanceof ServerPlayer) {
				PsiPlayerDataSync.sendLoopcast((ServerPlayer) context.caster, data.loopcasting, data.loopcastHand);
			}
		}
		return new ArrayList<>();
	}

	@Override
	public void predictSpell(ItemStack stack, SpellContext context) {
		PlayerData data = PsiPlayerData.get(context.caster);
		if(!data.loopcasting || context.castFrom != data.loopcastHand) {
			data.loopcasting = true;
			data.loopcastHand = context.castFrom;
			data.lastTickLoopcastStack = null;
			data.loopcastTime = 1;
			data.loopcastAmount = 0;
			data.loopcastFadeTime = 0;
			context.cspell.safePredict(context);
		}
	}

	@Override
	public boolean loopcastSpell(ItemStack stack, SpellContext context) {
		context.cspell.safeExecute(context);
		return true;
	}

	@Override
	public boolean predictLoopcastSpell(ItemStack stack, SpellContext context) {
		context.cspell.safePredict(context);
		return true;
	}

	@Override
	public String getBulletType() {
		return "loopcast";
	}

	@Override
	public boolean isCADOnlyContainer(ItemStack stack) {
		return true;
	}
}
