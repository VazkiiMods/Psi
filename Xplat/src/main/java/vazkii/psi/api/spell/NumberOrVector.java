/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import vazkii.psi.api.internal.Vector3;

/**
 * A spell value that is either a number or a {@link Vector3}. Ops apply per axis on vectors;
 * combining a scalar with a vector broadcasts the scalar across all three axes.
 */
public sealed interface NumberOrVector {

	static NumberOrVector of(Object raw) {
		return switch(raw) {
		case Number n -> new Scalar(n.doubleValue());
		case Vector3 v -> new Vector(v);
		default -> throw new IllegalArgumentException("Not a number or vector: " + raw);
		};
	}

	/**
	 * The raw value to store in the spell context: a {@link Double} or a {@link Vector3}.
	 */
	Object unwrap();

	Vector3 toVector();

	NumberOrVector map(UnaryOp op) throws SpellRuntimeException;

	default NumberOrVector combine(BinaryOp op, NumberOrVector other) throws SpellRuntimeException {
		if(this instanceof Scalar(double a) && other instanceof Scalar(double b)) {
			return new Scalar(op.apply(a, b));
		}

		Vector3 a = toVector();
		Vector3 b = other.toVector();
		return new Vector(new Vector3(op.apply(a.x, b.x), op.apply(a.y, b.y), op.apply(a.z, b.z)));
	}

	record Scalar(double value) implements NumberOrVector {

		@Override
		public Object unwrap() {
			return value;
		}

		@Override
		public Vector3 toVector() {
			return new Vector3(value, value, value);
		}

		@Override
		public NumberOrVector map(UnaryOp op) throws SpellRuntimeException {
			return new Scalar(op.apply(value));
		}

	}

	record Vector(Vector3 value) implements NumberOrVector {

		@Override
		public Object unwrap() {
			return value;
		}

		@Override
		public Vector3 toVector() {
			return value;
		}

		@Override
		public NumberOrVector map(UnaryOp op) throws SpellRuntimeException {
			return new Vector(new Vector3(op.apply(value.x), op.apply(value.y), op.apply(value.z)));
		}

	}

	@FunctionalInterface
	interface UnaryOp {
		double apply(double a) throws SpellRuntimeException;
	}

	@FunctionalInterface
	interface BinaryOp {
		double apply(double a, double b) throws SpellRuntimeException;
	}

}
