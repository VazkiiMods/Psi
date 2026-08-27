/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.internal;

import net.minecraft.core.Position;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import dev.ryanhcode.sable.companion.SableCompanion;

public final class SpatialHelper {

	public static double distanceSquared(Level level, Position a, Position b) {
		return distanceSquared(level, a.x(), a.y(), a.z(), b);
	}

	public static double distanceSquared(Level level, double x, double y, double z, Position b) {
		return SableCompanion.INSTANCE.distanceSquaredWithSubLevels(level, x, y, z, b.x(), b.y(), b.z());
	}

	public static double rectilinearDistance(Level level, double x, double y, double z, Position b) {
		return SableCompanion.INSTANCE.rectilinearDistanceWithSubLevels(
				level, x, y, z, b.x(), b.y(), b.z());
	}

	public static Vec3 project(Level level, Position position) {
		return SableCompanion.INSTANCE.projectOutOfSubLevel(level, position);
	}

	public static Vec3 project(Level level, double x, double y, double z) {
		return project(level, new Vec3(x, y, z));
	}

	private SpatialHelper() {}
}
