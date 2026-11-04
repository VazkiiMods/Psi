/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.fabric.platform;

import net.fabricmc.loader.api.FabricLoader;

import vazkii.psi.common.platform.PsiPlatform;

import java.util.Optional;

public final class FabricPsiPlatform implements PsiPlatform {

	@Override
	public void initialize() {}

	@Override
	public boolean isDataGeneration() {
		return System.getProperty("fabric-api.datagen") != null;
	}

	@Override
	public int maxCraftingGridSlots() {
		return 9;
	}

	@Override
	public Optional<ModInfo> findMod(String modId) {
		return FabricLoader.getInstance().getModContainer(modId)
				.map(container -> new ModInfo(
						container.getMetadata().getId(),
						container.getMetadata().getName(),
						container.getMetadata().getVersion().getFriendlyString()));
	}

}
