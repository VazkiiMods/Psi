/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge;

import com.mojang.serialization.MapCodec;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.neoforge.data.MagicalPsiCondition;

public final class NeoForgeRecipeConditions {
	private static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONS =
			DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, PsiAPI.MOD_ID);

	static {
		CONDITIONS.register("magipsi_enabled", () -> MagicalPsiCondition.CODEC);
	}

	private NeoForgeRecipeConditions() {}

	public static void register(IEventBus bus) {
		CONDITIONS.register(bus);
	}
}
