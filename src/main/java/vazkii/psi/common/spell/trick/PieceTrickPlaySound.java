/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.helpers.SpellHelpers;

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

		if (dPit > 24) {
			throw new SpellCompilationException(SpellCompilationException.PITCH, x, y);
		}

		if (dVol > 1) {
			throw new SpellCompilationException(SpellCompilationException.VOLUME, x, y);
		}
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		BlockPos pos = SpellHelpers.getBlockPos(this, context, position, true, false);
		double instrumentVal = this.getParamValue(context, instrument).doubleValue();
		double volVal = this.getParamValueOrDefault(context, volume, 1).doubleValue();
		double pitchVal = this.getParamValueOrDefault(context, pitch, 0).doubleValue();

		int instrumentId = MathHelper.clamp((int) instrumentVal, 0, Psi.noteblockSoundEvents.size() - 1);

		float f = (float) Math.pow(2, (pitchVal - 12) / 12.0);
		context.caster.world.playSound(null, pos, Psi.noteblockSoundEvents.get(instrumentId), SoundCategory.RECORDS, (float) volVal, f);
		return null;
	}
}
