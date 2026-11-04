/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.platform;

import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.data.loading.DatagenModLoader;

import vazkii.psi.common.platform.PsiPlatform;

import java.util.Optional;

public final class NeoForgePsiPlatform implements PsiPlatform {
	private static IEventBus modBus;

	public static void prepare(IEventBus bus) {
		if(modBus != null) {
			throw new IllegalStateException("NeoForge platform was prepared more than once");
		}
		modBus = bus;
	}

	public static IEventBus modBus() {
		if(modBus == null) {
			throw new IllegalStateException("NeoForge platform was used before its mod event bus was prepared");
		}
		return modBus;
	}

	@Override
	public void initialize() {}

	@Override
	public boolean isDataGeneration() {
		return DatagenModLoader.isRunningDataGen();
	}

	@Override
	public int maxCraftingGridSlots() {
		return ShapedRecipePattern.getMaxWidth() * ShapedRecipePattern.getMaxHeight();
	}

	@Override
	public Optional<ModInfo> findMod(String modId) {
		return ModList.get().getModContainerById(modId)
				.map(container -> new ModInfo(
						container.getModId(),
						container.getModInfo().getDisplayName(),
						container.getModInfo().getVersion().toString()));
	}

}
