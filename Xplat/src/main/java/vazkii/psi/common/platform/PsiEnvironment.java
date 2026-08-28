/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.platform;

import vazkii.psi.common.platform.PsiPlatform.ModInfo;

import java.util.Optional;

public final class PsiEnvironment {
	private static final PsiPlatform PLATFORM = PsiServices.load(PsiPlatform.class);

	private PsiEnvironment() {}

	public static boolean isDataGeneration() {
		return PLATFORM.isDataGeneration();
	}

	public static int maxCraftingGridSlots() {
		return PLATFORM.maxCraftingGridSlots();
	}

	public static boolean isModLoaded(String modId) {
		return PLATFORM.findMod(modId).isPresent();
	}

	public static Optional<ModInfo> findMod(String modId) {
		return PLATFORM.findMod(modId);
	}

	public static boolean isMagical() {
		return isModLoaded("magipsi");
	}
}
