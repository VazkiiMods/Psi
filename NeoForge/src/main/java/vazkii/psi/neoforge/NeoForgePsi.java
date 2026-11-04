/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.PsiBootstrap;
import vazkii.psi.common.PsiCommonRuntime;
import vazkii.psi.neoforge.client.NeoForgePsiClient;
import vazkii.psi.neoforge.platform.NeoForgePsiPlatform;

@Mod(PsiAPI.MOD_ID)
public final class NeoForgePsi {

	public NeoForgePsi(IEventBus bus, Dist dist, ModContainer container) {
		NeoForgePsiPlatform.prepare(bus);
		NeoForgePsiConfig.setup(container, dist);
		PsiBootstrap.initialize();
		PsiCommonRuntime.prepare();
		NeoForgeRecipeConditions.register(bus);
		bus.addListener(this::commonSetup);
		if(dist.isClient()) {
			NeoForgePsiClient.initialize(bus);
		}
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		PsiCommonRuntime.initialize();
	}

}
