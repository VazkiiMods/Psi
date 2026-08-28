/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.capability;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

import vazkii.psi.api.capability.PsiCapabilities;
import vazkii.psi.api.spell.ISpellImmune;
import vazkii.psi.api.spell.detonator.IDetonationHandler;
import vazkii.psi.common.entity.ModEntities;
import vazkii.psi.common.platform.PsiLookups;

import java.util.List;

public final class PsiEntityCapabilities {
	private PsiEntityCapabilities() {}

	public static void register() {
		PsiLookups.registerEntity(PsiCapabilities.SPELL_IMMUNE, ISpellImmune.class,
				entity -> (ISpellImmune) entity, List.of(ModEntities.spellCircle));
		PsiLookups.registerEntity(PsiCapabilities.DETONATION_HANDLER, IDetonationHandler.class,
				entity -> new CapabilityTriggerSensor((Player) entity), List.of(() -> EntityType.PLAYER));
		PsiLookups.registerEntity(PsiCapabilities.DETONATION_HANDLER, IDetonationHandler.class,
				entity -> (IDetonationHandler) entity, List.of(ModEntities.spellCharge));
	}
}
