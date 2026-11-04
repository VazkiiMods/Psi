/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.platform;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class PsiEntityTypes {

	private static final PsiEntityTypeService SERVICE = PsiServices.load(PsiEntityTypeService.class);

	private PsiEntityTypes() {}

	public static <T extends Entity> EntityType<T> create(
			String name,
			EntityType.EntityFactory<T> factory,
			MobCategory category,
			float width,
			float height,
			int trackingRange,
			int updateInterval,
			boolean velocityUpdates,
			boolean fireImmune) {
		return SERVICE.create(name, factory, category, width, height,
				trackingRange, updateInterval, velocityUpdates, fireImmune);
	}

}
