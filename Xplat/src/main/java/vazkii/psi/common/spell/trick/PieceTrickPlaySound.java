/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickPlaySound extends PieceTrick {

	SpellParam<Vector3> position;
	SpellParam<Number> pitch;
	SpellParam<Number> volume;
	SpellParam<Number> instrument;

	public PieceTrickPlaySound(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(instrument = new ParamNumber(SpellParam.GENERIC_NAME_INSTRUMENT, SpellParam.RED, false, false));
		addParam(pitch = new ParamNumber(SpellParam.GENERIC_NAME_PITCH, SpellParam.GREEN, true, false));
		addParam(volume = new ParamNumber(SpellParam.GENERIC_NAME_VOLUME, SpellParam.YELLOW, true, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		double dVol = SpellHelpers.ensurePositiveOrZero(this, volume, 1);
		double dPit = SpellHelpers.ensurePositiveOrZero(this, pitch, 0);

		if(dPit > 24) {
			throw new SpellCompilationException(SpellCompilationException.PITCH, x, y);
		}

		if(dVol > 1) {
			throw new SpellCompilationException(SpellCompilationException.VOLUME, x, y);
		}
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		BlockPos pos = SpellHelpers.getBlockPos(this, context, position, true, false);
		double instrumentVal = this.getParamValue(context, instrument).doubleValue();
		double volVal = this.getParamValueOrDefault(context, volume, 1).doubleValue();
		double pitchVal = this.getParamValueOrDefault(context, pitch, 0).doubleValue();

		int instrumentId = Mth.clamp((int) instrumentVal, 0, BuiltInRegistries.INSTRUMENT.size() - 1);

		float f = (float) Math.pow(2, (pitchVal - 12) / 12.0);
		context.focalPoint.level().playSound(null, pos, BuiltInRegistries.INSTRUMENT.stream().toList().get(instrumentId).soundEvent().value(), SoundSource.RECORDS, (float) volVal, f);
		return null;
	}
}
