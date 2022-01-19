/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import vazkii.psi.api.internal.Vector3;

public class SpellHelpers {

	public static double ensurePositiveOrZero(SpellPiece piece, SpellParam<Number> param) throws SpellCompilationException {
		double val = piece.getNonNullParamEvaluation(param).doubleValue();
		if (val < 0) {
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, piece.x, piece.y);
		}
		return val;
	}

	public static double ensurePositiveAndNonzero(SpellPiece piece, SpellParam<Number> param) throws SpellCompilationException {
		double val = piece.getNonNullParamEvaluation(param).doubleValue();
		if (val <= 0) {
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, piece.x, piece.y);
		}

		return val;
	}

	public static double rangeLimitParam(SpellPiece piece, SpellContext context, SpellParam<Number> param, double max) throws SpellRuntimeException {
		Number numberVal = piece.getParamValue(context, param);
		if (numberVal == null) {
			return max;
		}
		return Math.min(max, Math.max(-max, numberVal.doubleValue()));
	}

	public static Direction getFacing(SpellPiece piece, SpellContext context, SpellParam<Vector3> param) throws SpellRuntimeException {
		Vector3 face = getVector3(piece, context, param, false, true);
		return Direction.getNearest((float) face.x, (float) face.y, (float) face.z);
	}

	public static Direction getFacing(SpellPiece piece, SpellContext context, SpellParam<Vector3> param, Direction def) throws SpellRuntimeException {
		Vector3 face = getVector3(piece, context, param, true, false, true);
		if (face == null) {
			return def;
		}
		return Direction.getNearest((float) face.x, (float) face.y, (float) face.z);
	}

	public static boolean isBlockPosInRadius(SpellContext context, BlockPos pos) {
		return context.isInRadius(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
	}

	public static double ensurePositiveOrZero(SpellPiece piece, SpellParam<Number> param, double def) throws SpellCompilationException {
		double val = piece.getParamEvaluationeOrDefault(param, def).doubleValue();
		if (val < 0) {
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, piece.x, piece.y);
		}
		return val;
	}

	public static double ensurePositiveAndNonzero(SpellPiece piece, SpellParam<Number> param, double def) throws SpellCompilationException {
		double val = piece.getParamEvaluationeOrDefault(param, def).doubleValue();
		if (val <= 0) {
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, piece.x, piece.y);
		}

		return val;
	}

	public static BlockPos getBlockPos(SpellPiece piece, SpellContext context, SpellParam<Vector3> param) throws SpellRuntimeException {
		Vector3 position = piece.getParamValue(context, param);
		if (position == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}
		if (!context.isInRadius(position)) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}
		return position.toBlockPos();
	}

	public static Vector3 getVector3(SpellPiece piece, SpellContext context, SpellParam<Vector3> param) throws SpellRuntimeException {
		return checkPos(piece, context, param, false, false);
	}

	public static Vector3 getVector3(SpellPiece piece, SpellContext context, SpellParam<Vector3> param, boolean check, boolean shouldBeAxial) throws SpellRuntimeException {
		return checkPos(piece, context, param, check, shouldBeAxial);
	}

	public static Vector3 getVector3(SpellPiece piece, SpellContext context, SpellParam<Vector3> param, boolean nonnull, boolean check, boolean shouldBeAxial) throws SpellRuntimeException {
		return checkPos(piece, context, param, nonnull, check, shouldBeAxial);
	}

	public static BlockPos getBlockPos(SpellPiece piece, SpellContext context, SpellParam<Vector3> param, boolean check, boolean shouldBeAxial) throws SpellRuntimeException {
		return checkPos(piece, context, param, check, shouldBeAxial).toBlockPos();
	}

	public static BlockPos getBlockPos(SpellPiece piece, SpellContext context, SpellParam<Vector3> param, boolean nonnull, boolean check, boolean shouldBeAxial) throws SpellRuntimeException {
		return checkPos(piece, context, param, nonnull, check, shouldBeAxial).toBlockPos();
	}

	public static Vector3 checkPos(SpellPiece piece, SpellContext context, SpellParam<Vector3> param, boolean check, boolean shouldBeAxial) throws SpellRuntimeException {
		return checkPos(piece, context, param, true, check, shouldBeAxial);
	}

	public static Vector3 checkPos(SpellPiece piece, SpellContext context, SpellParam<Vector3> param, boolean nonnull, boolean check, boolean shouldBeAxial) throws SpellRuntimeException {
		Vector3 position = piece.getParamValue(context, param);
		if (nonnull && (position == null || position.isZero())) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}
		if (check && !context.isInRadius(position)) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}
		if (shouldBeAxial && !position.isAxial()) {
			throw new SpellRuntimeException(SpellRuntimeException.NON_AXIAL_VECTOR);
		}
		return position;
	}

}
