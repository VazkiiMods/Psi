/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lwjgl.opengl.GL11;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Vector3 {
	public static final Vector3 zero = new Vector3();
	public static Vector3 one = new Vector3(1, 1, 1);
	public static Vector3 center = new Vector3(0.5, 0.5, 0.5);

	public static Vector3 up = new Vector3(0, 1, 0);
	public static Vector3 down = new Vector3(0, -1, 0);
	public static Vector3 forward = new Vector3(0, 0, 1);
	public static Vector3 back = new Vector3(0, 0, -1);
	public static Vector3 right = new Vector3(1, 0, 0);
	public static Vector3 left = new Vector3(-1, 0, 0);

	public double x;
	public double y;
	public double z;

	public Vector3() {}

	public Vector3(double d, double d1, double d2) {
		x = d;
		y = d1;
		z = d2;
	}

	public Vector3(Vector3 vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
	}

	public Vector3(Vec3 vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
	}

	public Vector3 copy() {
		return new Vector3(this);
	}

	public static Vector3 fromEntity(Entity e) {
		return new Vector3(e.getX(), e.getY(), e.getZ());
	}

	public static Vector3 fromEntityCenter(Entity e) {
		return new Vector3(e.getX(), e.getY() - e.getMyRidingOffset() + e.getBbHeight() / 2, e.getZ());
	}

	public static Vector3 fromTileEntity(BlockEntity e) {
		return fromBlockPos(e.getBlockPos());
	}

	public static Vector3 fromTileEntityCenter(BlockEntity e) {
		return fromTileEntity(e).add(0.5, 0.5, 0.5);
	}

	public static Vector3 fromBlockPos(BlockPos pos) {
		return new Vector3(pos.getX(), pos.getY(), pos.getZ());
	}

	public static Vector3 fromVec3d(Vec3 vec3d) {
		return new Vector3(vec3d.x(), vec3d.y(), vec3d.z());
	}

	public static Vector3 fromDirection(Direction direction) {
		return new Vector3(direction.getStepX(), direction.getStepY(), direction.getStepZ());
	}

	public Vector3 set(double d, double d1, double d2) {
		x = d;
		y = d1;
		z = d2;
		return this;
	}

	public Vector3 set(Vector3 vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
		return this;
	}

	public double dotProduct(Vector3 vec) {
		double d = vec.x * x + vec.y * y + vec.z * z;

		if (d > 1 && d < 1.00001) {
			d = 1;
		} else if (d < -1 && d > -1.00001) {
			d = -1;
		}
		return d;
	}

	public double dotProduct(double d, double d1, double d2) {
		return d * x + d1 * y + d2 * z;
	}

	public Vector3 crossProduct(Vector3 vec) {
		double d = y * vec.z - z * vec.y;
		double d1 = z * vec.x - x * vec.z;
		double d2 = x * vec.y - y * vec.x;
		x = d;
		y = d1;
		z = d2;
		return this;
	}

	public Vector3 add(double d, double d1, double d2) {
		x += d;
		y += d1;
		z += d2;
		return this;
	}

	public Vector3 add(Vector3 vec) {
		x += vec.x;
		y += vec.y;
		z += vec.z;
		return this;
	}

	public Vector3 add(double d) {
		return add(d, d, d);
	}

	public Vector3 sub(Vector3 vec) {
		return subtract(vec);
	}

	public Vector3 subtract(Vector3 vec) {
		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
		return this;
	}

	public Vector3 negate(Vector3 vec) {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}

	public Vector3 multiply(double d) {
		x *= d;
		y *= d;
		z *= d;
		return this;
	}

	public Vector3 multiply(Vector3 f) {
		x *= f.x;
		y *= f.y;
		z *= f.z;
		return this;
	}

	public Vector3 multiply(double fx, double fy, double fz) {
		x *= fx;
		y *= fy;
		z *= fz;
		return this;
	}

	public double mag() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public double magSquared() {
		return x * x + y * y + z * z;
	}

	public Vector3 normalize() {
		double d = mag();
		if (d != 0) {
			multiply(1 / d);
		}

		return this;
	}

	@Override
	public String toString() {
		MathContext cont = new MathContext(5, RoundingMode.HALF_UP);
		return "Vector[" + new BigDecimal(x, cont) + ", " + new BigDecimal(y, cont) + ", " + new BigDecimal(z, cont) + "]";
	}

	public Vector3 perpendicular() {
		if (z == 0) {
			return zCrossProduct();
		}
		return xCrossProduct();
	}

	public Vector3 xCrossProduct() {
		double d = z;
		double d1 = -y;
		x = 0;
		y = d;
		z = d1;
		return this;
	}

	public Vector3 zCrossProduct() {
		double d = y;

		double d1 = -x;
		x = d;
		y = d1;
		z = 0;
		return this;
	}

	public Vector3 yCrossProduct() {
		double d = -z;
		double d1 = x;
		x = d;
		y = 0;
		z = d1;
		return this;
	}

	public Vec3 toVec3D() {
		return new Vec3(x, y, z);
	}

	public BlockPos toBlockPos() {
		return new BlockPos(toVec3D());
	}

	public double angle(Vector3 vec) {
		return Math.acos(copy().normalize().dotProduct(vec.copy().normalize()));
	}

	public boolean isInside(AABB aabb) {
		return x >= aabb.minX && y >= aabb.maxY && z >= aabb.minZ && x < aabb.maxX && y < aabb.maxY && z < aabb.maxZ;
	}

	public boolean isZero() {
		return x == 0 && y == 0 && z == 0;
	}

	public boolean isAxial() {
		return x == 0 ? y == 0 || z == 0 : y == 0 && z == 0;
	}

	@OnlyIn(Dist.CLIENT)
	public void glVertex() {
		GL11.glVertex3d(x, y, z);
	}

	public Vector3 negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}

	public double scalarProject(Vector3 b) {
		double l = b.mag();
		return l == 0 ? 0 : dotProduct(b) / l;
	}

	public Vector3 project(Vector3 b) {
		double l = b.magSquared();
		if (l == 0) {
			set(0, 0, 0);
			return this;
		}

		double m = dotProduct(b) / l;
		set(b).multiply(m);
		return this;
	}

	public Vector3 rotate(double angle, Vector3 axis) {
		Quat.aroundAxis(axis.copy().normalize(), angle).rotate(this);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Vector3)) {
			return false;
		}

		Vector3 v = (Vector3) o;
		return x == v.x && y == v.y && z == v.z;
	}

	@Override
	public int hashCode() {
		return 31 * (31 * (31 + Double.hashCode(x)) + Double.hashCode(y)) + Double.hashCode(z);
	}
}
