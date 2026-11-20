/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell.param;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.SpellPiece;

/**
 * A parameter that accepts either a {@link Number} or a {@link Vector3}.
 */
public class ParamNumberOrVector extends ParamSpecific<Object> {

	public ParamNumberOrVector(String name, int color, boolean canDisable, boolean constant) {
		super(name, color, canDisable, constant);
	}

	@Override
	protected Class<Object> getRequiredType() {
		return Object.class;
	}

	@Override
	public boolean canAccept(SpellPiece piece) {
		Class<?> type = piece.getEvaluationType();
		return type != null && (Number.class.isAssignableFrom(type) || Vector3.class.isAssignableFrom(type))
				&& (!requiresConstant() || piece.getPieceType() == EnumPieceType.CONSTANT);
	}

	@Override
	public Component getRequiredTypeString() {
		MutableComponent s = Component.translatable("psi.datatype.number_or_vector");
		if(requiresConstant()) {
			s.append(" ").append(Component.translatable("psimisc.constant"));
		}

		return s;
	}

}
