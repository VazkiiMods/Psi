/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.piece;

import net.minecraft.world.entity.Entity;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.Arrays;
import java.util.stream.Collectors;

import dev.latvian.mods.kubejs.error.KubeRuntimeException;

public enum ScriptValueType {

	NUMBER("number", Number.class, SpellParam.RED),
	VECTOR("vector", Vector3.class, SpellParam.GREEN),
	ENTITY("entity", Entity.class, SpellParam.YELLOW),
	ENTITY_LIST("entity_list", EntityListWrapper.class, SpellParam.YELLOW),
	ANY("any", SpellParam.Any.class, SpellParam.BLUE);

	public final String scriptName;
	public final Class<?> evaluationType;
	private final int color;

	ScriptValueType(String scriptName, Class<?> evaluationType, int color) {
		this.scriptName = scriptName;
		this.evaluationType = evaluationType;
		this.color = color;
	}

	public static ScriptValueType byScriptName(String name) {
		for(ScriptValueType type : values()) {
			if(type.scriptName.equals(name)) {
				return type;
			}
		}
		String valid = Arrays.stream(values()).map(type -> type.scriptName).collect(Collectors.joining(", "));
		throw new KubeRuntimeException("Unknown spell value type '" + name + "', expected one of: " + valid);
	}

	public SpellParam<?> newParam(String key, boolean canDisable) {
		return switch(this) {
		case NUMBER -> new ParamNumber(key, color, canDisable, false);
		case VECTOR -> new ParamVector(key, color, canDisable, false);
		case ENTITY -> new ParamEntity(key, color, canDisable, false);
		case ENTITY_LIST -> new ParamEntityListWrapper(key, color, canDisable, false);
		case ANY -> new ParamAny(key, color, canDisable);
		};
	}

	public boolean accepts(Object value) {
		return this == ANY || value == null || evaluationType.isInstance(value);
	}

}
