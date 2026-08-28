/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.core.handler.ContributorSpellCircleHandler;

import java.util.Locale;

@EventBusSubscriber(modid = PsiAPI.MOD_ID)
public final class NeoForgeContributorEvents {

	private NeoForgeContributorEvents() {}

	@SubscribeEvent
	public static void craftColorizer(PlayerEvent.ItemCraftedEvent event) {
		String playerName = event.getEntity().getName().getString();
		if(ContributorSpellCircleHandler.isContributor(playerName.toLowerCase(Locale.ROOT))
				&& event.getCrafting().getItem() instanceof ICADColorizer colorizer) {
			colorizer.setContributorName(event.getCrafting(), playerName);
		}
	}
}
