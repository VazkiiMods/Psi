/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.fabric.platform;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import vazkii.psi.common.platform.PsiEntityTypeService;

@SuppressWarnings("deprecation")
public final class FabricPsiEntityTypeService implements PsiEntityTypeService {

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
		FabricEntityTypeBuilder<T> builder = FabricEntityTypeBuilder.create(category, factory)
				.dimensions(EntityDimensions.scalable(width, height))
				.trackRangeBlocks(trackingRange)
				.trackedUpdateRate(updateInterval)
				.forceTrackedVelocityUpdates(velocityUpdates);
		if(fireImmune) {
			builder.fireImmune();
		}
		return builder.build();
	}

}
