/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import vazkii.psi.common.PsiBootstrap;
import vazkii.psi.common.PsiCommonRuntime;
import vazkii.psi.common.core.handler.AdditiveMotionHandler;
import vazkii.psi.common.core.handler.DelayedSpellHandler;

public final class FabricPsi implements ModInitializer {

	@Override
	public void onInitialize() {
		FiberPsiConfig.setup();
		PsiBootstrap.initialize();
		PsiCommonRuntime.initialize();
		FabricPsiGameplayEvents.register();
		ServerTickEvents.END_SERVER_TICK.register(server -> DelayedSpellHandler.tick());
		ServerTickEvents.END_WORLD_TICK.register(AdditiveMotionHandler::onLevelTick);
	}

}
