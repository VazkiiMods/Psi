/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.fabric.platform;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;

import vazkii.psi.common.platform.PsiAttributeService;

import java.util.List;

public final class FabricPsiAttributeService implements PsiAttributeService {

	@Override
	public void addPlayerAttributes(List<Holder<Attribute>> attributes) {
		AttributeSupplier.Builder builder = Player.createAttributes();
		attributes.forEach(builder::add);
		FabricDefaultAttributeRegistry.register(EntityType.PLAYER, builder);
	}

}
