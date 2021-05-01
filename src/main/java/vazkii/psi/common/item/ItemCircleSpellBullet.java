/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.entity.EntitySpellCircle;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorRaycast;

import java.util.HashSet;
import java.util.Set;

public class ItemCircleSpellBullet extends ItemSpellBullet {

	public ItemCircleSpellBullet(Properties properties) {
		super(properties);
	}

	@Override
	public Set<Entity> castSpell(ItemStack stack, SpellContext context) {
		ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
		ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
		RayTraceResult pos = PieceOperatorVectorRaycast.raycast(context.caster, 32);
		HashSet<Entity> spellEntities = new HashSet<>();
		if (pos != null) {
			EntitySpellCircle circle = new EntitySpellCircle(EntitySpellCircle.TYPE, context.caster.getEntityWorld());
			circle.setInfo(context.caster, colorizer, stack);
			circle.setPosition(pos.getHitVec().x, pos.getHitVec().y, pos.getHitVec().z);
			circle.getEntityWorld().addEntity(circle);
			spellEntities.add(circle);
		}
		return spellEntities;
	}

	@Override
	public double getCostModifier(ItemStack stack) {
		return EntitySpellCircle.CAST_TIMES * 0.75;
	}

	@Override
	public String getBulletType() {
		return "circle";
	}

	@Override
	public boolean isCADOnlyContainer(ItemStack stack) {
		return true;
	}
}
