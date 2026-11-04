/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.platform;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import vazkii.psi.common.platform.PsiEntityTypeService;

public final class NeoForgePsiEntityTypeService implements PsiEntityTypeService {

	@Override
	public <T extends Entity> EntityType<T> create(
			String name,
			EntityType.EntityFactory<T> factory,
			MobCategory category,
			float width,
			float height,
			int trackingRange,
			int updateInterval,
			boolean velocityUpdates,
			boolean fireImmune) {
		EntityType.Builder<T> builder = EntityType.Builder.of(factory, category)
				.setTrackingRange(trackingRange)
				.setUpdateInterval(updateInterval)
				.setShouldReceiveVelocityUpdates(velocityUpdates)
				.sized(width, height);
		if(fireImmune) {
			builder.fireImmune();
		}
		return builder.build(name);
	}

}
