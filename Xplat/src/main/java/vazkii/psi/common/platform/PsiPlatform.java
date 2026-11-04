/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.platform;

import java.util.Optional;

public interface PsiPlatform {

	void initialize();

	boolean isDataGeneration();

	int maxCraftingGridSlots();

	Optional<ModInfo> findMod(String modId);

	record ModInfo(String id, String name, String version) {
	}

}
