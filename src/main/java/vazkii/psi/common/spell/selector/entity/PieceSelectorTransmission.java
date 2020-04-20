package vazkii.psi.common.spell.selector.entity;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.helpers.SpellHelpers;

public class PieceSelectorTransmission extends PieceSelector {

	SpellParam<Number> channel;

	public PieceSelectorTransmission(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(channel = new ParamNumber(SpellParam.GENERIC_NAME_CHANNEL, SpellParam.RED, true, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);

		SpellHelpers.ensurePositiveOrZero(this, channel, 0);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		int chanel = this.getParamValueOrDefault(context, channel, 0).intValue();

		String key = "psi_broadcast_channel:" + chanel;

		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(context.caster);

		if(data.getCustomData().contains(key)) {
			return data.getCustomData().getDouble(key);
		}

		throw new SpellRuntimeException(SpellRuntimeException.NO_MESSAGE);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}
}
