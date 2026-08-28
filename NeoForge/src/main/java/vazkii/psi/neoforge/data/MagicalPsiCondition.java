/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.data;

import com.mojang.serialization.MapCodec;

import net.neoforged.neoforge.common.conditions.ICondition;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.common.platform.PsiEnvironment;

public final class MagicalPsiCondition implements ICondition {
	public static final MagicalPsiCondition INSTANCE = new MagicalPsiCondition();

	public static final MapCodec<MagicalPsiCondition> CODEC = MapCodec.unit(INSTANCE).stable();

	private MagicalPsiCondition() {}

	@Override
	public boolean test(IContext condition) {
		return PsiEnvironment.isMagical();
	}

	@Override
	public @NotNull MapCodec<? extends ICondition> codec() {
		return CODEC;
	}

	public String toString() {
		return "magipsi_enabled";
	}
}
