package vazkii.psi.common.spell.trick.entity;

import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.entity.EntitySpellCircle;
import vazkii.psi.common.entity.ModEntities;

public class PieceTrickConjureCircle extends PieceTrick {

	private SpellParam<Number> time;
	private SpellParam<Vector3> position;
	private SpellParam<Number> scale;
	private SpellParam<Vector3> direction;

	public PieceTrickConjureCircle(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.RED, false, false));
		addParam(direction = new ParamVector(SpellParam.GENERIC_NAME_DIRECTION, SpellParam.CYAN, true, false));
		addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.BLUE, true, true));
		addParam(scale = new ParamNumber(SpellParam.GENERIC_NAME_RADIUS, SpellParam.GREEN, true, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
		super.addToMetadata(meta);

		double scl = this.getParamEvaluationeOrDefault(scale, 1).doubleValue();
		double tim = this.getParamEvaluationeOrDefault(time, 100).doubleValue();

		if(scl > 4 || scl <= 0)
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);
		if(tim <= 0)
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);

		meta.addStat(EnumSpellStat.POTENCY, (int) (scl * tim / 100));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 pos = SpellHelpers.getVector3(this, context, position, true, true, false);
		Vector3 dir = SpellHelpers.getDefaultedVector(this, context, direction, false, false, new Vector3(0, 1, 0));
		double scl = this.getParamValueOrDefault(context, scale, 1).doubleValue();
		double maxTimeAlive = this.getParamValueOrDefault(context, time, 100).doubleValue();

		ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
		ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);

		EntitySpellCircle circle = new EntitySpellCircle(ModEntities.spellCircle, context.caster.getCommandSenderWorld());
		circle.setInfo(context.caster, colorizer, ItemStack.EMPTY);
		circle.setPos(pos.x, pos.y, pos.z);
		circle.setLifetime((int) maxTimeAlive);
		circle.setDirection(dir.toVec3D().normalize());
		circle.setLookAngle(dir.toVec3D().normalize());
		circle.setScale((float) scl);
		circle.getCommandSenderWorld().addFreshEntity(circle);
		return null;
	}
}
