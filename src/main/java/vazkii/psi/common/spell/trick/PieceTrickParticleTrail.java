/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import net.minecraftforge.network.PacketDistributor;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellHelpers;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.StatLabel;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageParticleTrail;

public class PieceTrickParticleTrail extends PieceTrick {
	SpellParam<Vector3> positionParam;
	SpellParam<Vector3> rayParam;
	SpellParam<Number> lengthParam;
	SpellParam<Number> timeParam;

	public PieceTrickParticleTrail(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel(SpellParam.GENERIC_NAME_DISTANCE, true).floor().mul(10));
	}

	@Override
	public void initParams() {
		addParam(positionParam = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(rayParam = new ParamVector(SpellParam.GENERIC_NAME_RAY, SpellParam.GREEN, false, false));
		addParam(lengthParam = new ParamNumber(SpellParam.GENERIC_NAME_DISTANCE, SpellParam.CYAN, false, true));
		addParam(timeParam = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.PURPLE, true, true));

	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
		super.addToMetadata(meta);

		double length = SpellHelpers.ensurePositiveAndNonzero(this, lengthParam);
		meta.addStat(EnumSpellStat.POTENCY, (int) length * 10);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 pos = SpellHelpers.getVector3(this, context, positionParam, true, false);
		Vector3 dir = SpellHelpers.getVector3(this, context, rayParam, true, false, false);
		double length = this.getParamValue(context, lengthParam).doubleValue();
		int time = Math.min(this.getParamValueOrDefault(context, timeParam, 20).intValue(), 1200);

		if(time <= 0) {
			throw new SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER);
		}

		time = time / 6;

		if(!context.isInRadius(pos.copy().add(dir.copy().normalize().multiply(length)))) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}

		MessageRegister.HANDLER.send(PacketDistributor.DIMENSION.with(() -> context.focalPoint.getCommandSenderWorld().dimension()), new MessageParticleTrail(pos.toVec3D(), dir.toVec3D(), length, time, PsiAPI.getPlayerCAD(context.caster)));
		return null;
	}
}
