/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.internal;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;

import java.util.LinkedHashSet;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class MathHelper {

	public static float pointDistancePlane(double x1, double y1, double x2, double y2) {
		return (float) Math.hypot(x1 - x2, y1 - y2);
	}

	public static double pointDistanceSpace(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2));
	}

	public static LinkedHashSet<BlockPos> getBlocksAlongRay(Vector3d origin, Vector3d end) {
		return getBlocksAlongRay(origin, end, Integer.MAX_VALUE);
	}

	/**
	 * [VanillaCopy] of {@link net.minecraft.world.IWorldReader#func_217300_a(RayTraceContext, BiFunction, Function)}
	 * but without the extra processing and endpoint bumping
	 */
	public static LinkedHashSet<BlockPos> getBlocksAlongRay(Vector3d origin, Vector3d end, int maxBlocks) {
		LinkedHashSet<BlockPos> positions = new LinkedHashSet<>();
		if (origin.equals(end)) {
			positions.add(new BlockPos(origin.x, origin.y, origin.z));
		} else {
			double endX = end.x;
			double endY = end.y;
			double endZ = end.z;
			double originX = origin.x;
			double originY = origin.y;
			double originZ = origin.z;
			int blockX = net.minecraft.util.math.MathHelper.floor(originX);
			int blockY = net.minecraft.util.math.MathHelper.floor(originY);
			int blockZ = net.minecraft.util.math.MathHelper.floor(originZ);
			BlockPos.Mutable blockPos = new BlockPos.Mutable(blockX, blockY, blockZ);
			positions.add(blockPos.toImmutable());
			double lengthX = endX - originX;
			double lengthY = endY - originY;
			double lengthZ = endZ - originZ;
			int signumX = net.minecraft.util.math.MathHelper.signum(lengthX);
			int signumY = net.minecraft.util.math.MathHelper.signum(lengthY);
			int signumZ = net.minecraft.util.math.MathHelper.signum(lengthZ);
			double stepSizeX = signumX == 0 ? Double.MAX_VALUE : (double) signumX / lengthX;
			double stepSizeY = signumY == 0 ? Double.MAX_VALUE : (double) signumY / lengthY;
			double stepSizeZ = signumZ == 0 ? Double.MAX_VALUE : (double) signumZ / lengthZ;
			double totalStepsX = stepSizeX * (signumX > 0 ? 1.0D - net.minecraft.util.math.MathHelper.frac(originX) : net.minecraft.util.math.MathHelper.frac(originX));
			double totalStepsY = stepSizeY * (signumY > 0 ? 1.0D - net.minecraft.util.math.MathHelper.frac(originY) : net.minecraft.util.math.MathHelper.frac(originY));
			double totalStepsZ = stepSizeZ * (signumZ > 0 ? 1.0D - net.minecraft.util.math.MathHelper.frac(originZ) : net.minecraft.util.math.MathHelper.frac(originZ));

			while ((totalStepsX <= 1.0D || totalStepsY <= 1.0D || totalStepsZ <= 1.0D) && positions.size() != maxBlocks) {
				if (totalStepsX < totalStepsY) {
					if (totalStepsX < totalStepsZ) {
						blockX += signumX;
						totalStepsX += stepSizeX;
					} else {
						blockZ += signumZ;
						totalStepsZ += stepSizeZ;
					}
				} else if (totalStepsY < totalStepsZ) {
					blockY += signumY;
					totalStepsY += stepSizeY;
				} else {
					blockZ += signumZ;
					totalStepsZ += stepSizeZ;
				}
				blockPos.setPos(blockX, blockY, blockZ);
				positions.add(blockPos.toImmutable());
			}
		}
		return positions;
	}

}
