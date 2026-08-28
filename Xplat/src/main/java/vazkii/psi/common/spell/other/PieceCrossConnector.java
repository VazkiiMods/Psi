/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.other;

import net.minecraft.network.chat.Component;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.SpellParam.ArrowType;
import vazkii.psi.api.spell.param.ParamAny;

public class PieceCrossConnector extends SpellPiece implements IGenericRedirector {

	private static final int LINE_ONE = 0xA0A0A0;
	private static final int LINE_TWO = 0xA040FF;
	public SpellParam<SpellParam.Any> in1;
	public SpellParam<SpellParam.Any> out1;
	public SpellParam<SpellParam.Any> in2;
	public SpellParam<SpellParam.Any> out2;

	public PieceCrossConnector(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.COMPLEXITY, new StatLabel(1));
	}

	@Override
	public void initParams() {
		addParam(in1 = new ParamAny(SpellParam.CONNECTOR_NAME_FROM1, LINE_ONE, false));
		addParam(out1 = new ParamAny(SpellParam.CONNECTOR_NAME_TO1, LINE_ONE, false, ArrowType.NONE));
		addParam(in2 = new ParamAny(SpellParam.CONNECTOR_NAME_FROM2, LINE_TWO, false));
		addParam(out2 = new ParamAny(SpellParam.CONNECTOR_NAME_TO2, LINE_TWO, false, ArrowType.NONE));
	}

	@Override
	public boolean isInputSide(SpellParam.Side side) {
		return paramSides.get(in1) == side || paramSides.get(in2) == side;
	}

	@Override
	public String getSortingName() {
		return "00000000000";
	}

	@Override
	public Component getEvaluationTypeString() {
		return Component.translatable("psi.datatype.any");
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		meta.addStat(EnumSpellStat.COMPLEXITY, 1);
	}

	@Override
	public EnumPieceType getPieceType() {
		return EnumPieceType.CONNECTOR;
	}

	@Override
	public SpellParam.Side remapSide(SpellParam.Side inputSide) {
		if(paramSides.get(out1).getOpposite() == inputSide) {
			return paramSides.get(in1);
		} else if(paramSides.get(out2).getOpposite() == inputSide) {
			return paramSides.get(in2);
		} else {
			return SpellParam.Side.OFF;
		}
	}

	// Since this class implements IGenericRedirector we don't need this
	@Override
	public Class<?> getEvaluationType() {
		return null;
	}

	@Override
	public Object evaluate() {
		return null;
	}

	@Override
	public Object execute(SpellContext context) {
		return null;
	}
}
