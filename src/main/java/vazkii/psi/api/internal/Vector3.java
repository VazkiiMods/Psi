/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.internal;

import com.mojang.serialization.Codec;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import io.netty.buffer.ByteBuf;

public class Vector3 {
	public static final Vector3 zero = new Vector3();
	public static final Codec<Vector3> CODEC = Codec.DOUBLE
			.listOf()
			.comapFlatMap(
					to -> Util.fixedSize(to, 3).map(list -> new Vector3(list.getFirst(), list.get(1), list.get(2))),
					from -> List.of(from.x, from.y, from.z)
			);
	public static final StreamCodec<ByteBuf, Vector3> STREAM_CODEC = new StreamCodec<>() {
		public @NotNull Vector3 decode(ByteBuf buffer) {
			return new Vector3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		}

		public void encode(ByteBuf buffer, Vector3 vector3) {
			buffer.writeDouble(vector3.x);
			buffer.writeDouble(vector3.y);
			buffer.writeDouble(vector3.z);
		}
	};
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

	public static Vector3 fromEntity(Entity e) {
		return new Vector3(e.getX(), e.getY(), e.getZ());
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

	public Vector3 copy() {
		return new Vector3(this);
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

		if(d > 1 && d < 1.00001) {
			d = 1;
		} else if(d < -1 && d > -1.00001) {
			d = -1;
		}
		return d;
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

	public Vector3 sub(Vector3 vec) {
		return subtract(vec);
	}

	public Vector3 subtract(Vector3 vec) {
		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
		return this;
	}

	public Vector3 multiply(double d) {
		x *= d;
		y *= d;
		z *= d;
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
		if(d != 0) {
			multiply(1 / d);
		}

		return this;
	}

	@Override
	public String toString() {
		MathContext cont = new MathContext(5, RoundingMode.HALF_UP);
		return "Vector[" + new BigDecimal(x, cont) + ", " + new BigDecimal(y, cont) + ", " + new BigDecimal(z, cont) + "]";
	}

	public Vec3 toVec3D() {
		return new Vec3(x, y, z);
	}

	public Vec3i toVec3i() {
		return new Vec3i((int) x, (int) y, (int) z);
	}

	public BlockPos toBlockPos() {
		return new BlockPos(toVec3i());
	}

	public boolean isZero() {
		return x == 0 && y == 0 && z == 0;
	}

	public boolean isAxial() {
		return x == 0 ? y == 0 || z == 0 : y == 0 && z == 0;
	}

	public Vector3 negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}

	public Vector3 project(Vector3 b) {
		double l = b.magSquared();
		if(l == 0) {
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
		if(!(o instanceof Vector3 v)) {
			return false;
		}

		return x == v.x && y == v.y && z == v.z;
	}

	@Override
	public int hashCode() {
		return 31 * (31 * (31 + Double.hashCode(x)) + Double.hashCode(y)) + Double.hashCode(z);
	}
}
