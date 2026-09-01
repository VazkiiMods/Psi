/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.piece;

import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.SpellRuntimeException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dev.latvian.mods.kubejs.script.ScriptType;

public final class ScriptSpellPiece extends SpellPiece {

	public record Definition(EnumPieceType pieceType, ScriptValueType returnType, List<ScriptPieceParam> params,
			ScriptPieceStats stats, boolean foldable, ScriptPieceBody body) {
	}

	private final Definition definition;
	private final Map<String, SpellParam<?>> scriptParams = new LinkedHashMap<>();

	public ScriptSpellPiece(Spell spell, Definition definition) {
		super(spell);
		this.definition = definition;
		for(ScriptPieceParam param : definition.params()) {
			SpellParam<?> spellParam = param.create();
			addParam(spellParam);
			scriptParams.put(param.name(), spellParam);
		}
		definition.stats().label(this);
	}

	@Override
	public EnumPieceType getPieceType() {
		return definition.pieceType();
	}

	@Override
	public Class<?> getEvaluationType() {
		return definition.pieceType().isTrick() ? Void.class : definition.returnType().evaluationType;
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		definition.stats().addTo(meta);
	}

	@Override
	public Object evaluate() throws SpellCompilationException {
		if(!definition.foldable()) {
			return null;
		}

		Map<String, Object> params = new LinkedHashMap<>();
		for(Map.Entry<String, SpellParam<?>> entry : scriptParams.entrySet()) {
			params.put(entry.getKey(), getParamEvaluation(entry.getValue()));
		}

		try {
			return run(ScriptType.STARTUP, null, params);
		} catch (SpellRuntimeException e) {
			throw new SpellCompilationException(e.getMessage(), x, y, e.arguments);
		}
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Map<String, Object> params = new LinkedHashMap<>();
		for(Map.Entry<String, SpellParam<?>> entry : scriptParams.entrySet()) {
			params.put(entry.getKey(), getParamValue(context, entry.getValue()));
		}

		ScriptType side = context.caster.level().isClientSide() ? ScriptType.CLIENT : ScriptType.SERVER;
		Object result = run(side, context, params);
		return definition.pieceType().isTrick() ? null : result;
	}

	private Object run(ScriptType side, SpellContext context, Map<String, Object> params) throws SpellRuntimeException {
		Object result;
		try {
			result = ScriptPieceErrors.unwrapResult(definition.body().run(context, params));
		} catch (Throwable e) {
			throw ScriptPieceErrors.toSpellError(getRegistryKey(), side, e);
		}

		if(definition.pieceType().isTrick() || definition.returnType().accepts(result)) {
			return result;
		}
		throw new SpellRuntimeException(ScriptPieceErrors.RETURN_TYPE, getRegistryKey().toString(),
				result.getClass().getSimpleName(), definition.returnType().scriptName);
	}

}
