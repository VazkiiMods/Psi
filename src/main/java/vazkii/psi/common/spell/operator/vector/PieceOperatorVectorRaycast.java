/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [18/01/2016, 22:02:05 (GMT)]
 */
package vazkii.psi.common.spell.operator.vector;



import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorRaycast extends PieceOperator {

	SpellParam origin;
	SpellParam ray;
	SpellParam max;

	public PieceOperatorVectorRaycast(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(origin = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(ray = new ParamVector(SpellParam.GENERIC_NAME_RAY, SpellParam.GREEN, false, false));
		addParam(max = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.PURPLE, true, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 originVal = this.getParamValue(context, origin);
		Vector3 rayVal = this.getParamValue(context, ray);

		if(originVal == null || rayVal == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

		double maxLen = SpellContext.MAX_DISTANCE;
		Double numberVal = this.<Double>getParamValue(context, max);
		if(numberVal != null)
			maxLen = numberVal;
		maxLen = Math.min(SpellContext.MAX_DISTANCE, maxLen);

		BlockRayTraceResult pos = raycast(context.caster, originVal, rayVal, maxLen);
		if(pos == null) // todo 1.14 should check for miss?
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

		return new Vector3(pos.getPos().getX(), pos.getPos().getY(), pos.getPos().getZ());
	}

	public static BlockRayTraceResult raycast(Entity e, double len) {
		Vector3 vec = Vector3.fromEntity(e);
		vec.add(0, e.getEyeHeight(), 0);
		
		Vec3d look = e.getLookVec();

		return raycast(e, vec, new Vector3(look), len);
	}

	private static BlockRayTraceResult raycast(Entity entity, Vector3 origin, Vector3 ray, double len) {
		Vector3 end = origin.copy().add(ray.copy().normalize().multiply(len));
		return entity.world.rayTraceBlocks(new RayTraceContext(origin.toVec3D(), end.toVec3D(), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity));
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

}
