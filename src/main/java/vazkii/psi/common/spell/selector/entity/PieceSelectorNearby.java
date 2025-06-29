/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.selector.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.List;
import java.util.function.Predicate;

public abstract class PieceSelectorNearby extends PieceSelector {

    SpellParam<Vector3> position;
    SpellParam<Number> radius;

    public PieceSelectorNearby(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, true, false));
        addParam(radius = new ParamNumber(SpellParam.GENERIC_NAME_RADIUS, SpellParam.GREEN, true, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);

        Double radiusVal = this.getParamEvaluationeOrDefault(radius, 2 * SpellContext.MAX_DISTANCE).doubleValue();
        if (radiusVal <= 0) {
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);
        }
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 positionVal = this.getParamValueOrDefault(context, position, Vector3.fromVec3d(context.focalPoint.position()));
        double radiusVal = this.getParamValueOrDefault(context, radius, 2 * SpellContext.MAX_DISTANCE).doubleValue();

        Vector3 positionCenter = Vector3.fromVec3d(context.focalPoint.position());

        if (!context.isInRadius(positionVal)) {
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        }

        AABB axis = new AABB(positionVal.x - radiusVal, positionVal.y - radiusVal, positionVal.z - radiusVal, positionVal.x + radiusVal, positionVal.y + radiusVal, positionVal.z + radiusVal);
        AABB eris = new AABB(positionCenter.x - SpellContext.MAX_DISTANCE, positionCenter.y - SpellContext.MAX_DISTANCE, positionCenter.z - SpellContext.MAX_DISTANCE, positionCenter.x + SpellContext.MAX_DISTANCE, positionCenter.y + SpellContext.MAX_DISTANCE, positionCenter.z + SpellContext.MAX_DISTANCE);
        AABB area = axis.intersect(eris);

        Predicate<Entity> pred = getTargetPredicate(context);

        List<Entity> list = context.caster.getCommandSenderWorld().getEntitiesOfClass(Entity.class, area, (Entity e) -> e != null && pred.test(e) && e != context.caster && e != context.focalPoint && context.isInRadius(e));

        return EntityListWrapper.make(list);
    }

    public abstract Predicate<Entity> getTargetPredicate(SpellContext context);

    @Override
    public Class<?> getEvaluationType() {
        return EntityListWrapper.class;
    }

}
