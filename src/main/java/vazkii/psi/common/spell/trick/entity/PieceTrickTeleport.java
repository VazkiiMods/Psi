/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [18/01/2016, 22:32:11 (GMT)]
 */
package vazkii.psi.common.spell.trick.entity;

import net.minecraft.entity.Entity;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickTeleport extends PieceTrick {

	SpellParam target;
	SpellParam direction;

	public PieceTrickTeleport(Spell spell) {
		
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
		addParam(direction = new ParamVector("psi.spellparam.position", SpellParam.GREEN, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		meta.addStat(EnumSpellStat.POTENCY, (int) (300));
		meta.addStat(EnumSpellStat.COST, (int) (4000));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity targetVal = this.<Entity>getParamValue(context, target);
		Vector3 directionVal = this.<Vector3>getParamValue(context, direction);
		targetVal.setPosition(directionVal.x,directionVal.y ,directionVal.z);
		return null;
	}


}
