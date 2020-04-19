/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.number;

import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorSwizzle extends PieceOperator {
	SpellParam<Number> num;
	SpellParam<Number> mask;
	SpellParam<Number> base;

	public static int swizzle(int n, int mask, int base) {
		int sgn = Integer.compare(n * mask, 0);

		int[] nDecompose = decomposeNumber(Math.abs(n), base);
		int[] maskDecompose = decomposeNumber(Math.abs(mask), 10);

		int[] newNumber = new int[maskDecompose.length];
		for (int i = 0; i < newNumber.length; i++) {
			int idx = maskDecompose[i];
			if (idx <= nDecompose.length && idx > 0) {
				newNumber[i] = nDecompose[idx - 1];
			}
		}

		return sgn * composeNumber(newNumber, base);
	}

	private static int composeNumber(int[] decomposed, int base) {
		int n = 0;
		for (int i = decomposed.length - 1; i >= 0; i--) {
			n = n * base + decomposed[i];
		}

		return n;
	}

	private static int[] decomposeNumber(int n, int base) {
		int size = (int) (Math.log(n) / Math.log(base) + 1);
		int[] decomposed = new int[size];
		for (int i = 0; i < size; i++) {
			decomposed[i] = n % base;
			if ((n /= base) == 0) {
				break;
			}
		}
		return decomposed;
	}

	public PieceOperatorSwizzle(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.GREEN, false, false));
		addParam(mask = new ParamNumber(SpellParam.GENERIC_NAME_MASK, SpellParam.BLUE, false, false));
		addParam(base = new ParamNumber(SpellParam.GENERIC_NAME_BASE, SpellParam.RED, true, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) {
		meta.addStat(EnumSpellStat.COMPLEXITY, paramSides.get(base).isEnabled() ? 4 : 3);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		int num = this.getParamValue(context, this.num).intValue();
		int mask = this.getParamValue(context, this.mask).intValue();
		int base = this.getParamValueOrDefault(context, this.base, 10).intValue();

		if (base < Character.MIN_RADIX) {
			throw new SpellRuntimeException(SpellRuntimeException.INVALID_BASE);
		}

		return (double) swizzle(num, mask, base);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}
}
