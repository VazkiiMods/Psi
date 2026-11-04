/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.platform;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

import vazkii.psi.common.platform.PsiAttributeService;

import java.util.List;

public final class NeoForgePsiAttributeService implements PsiAttributeService {

	@Override
	public void addPlayerAttributes(List<Holder<Attribute>> attributes) {
		NeoForgePsiPlatform.modBus().addListener((EntityAttributeModificationEvent event) -> attributes.forEach(attribute -> event.add(EntityType.PLAYER, attribute)));
	}

}
