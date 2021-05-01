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

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.entity.EntitySpellMine;
import vazkii.psi.common.entity.EntitySpellProjectile;

import java.util.HashSet;
import java.util.Set;

public class ItemMineSpellBullet extends ItemSpellBullet {

	public ItemMineSpellBullet(Properties properties) {
		super(properties);
	}

	@Override
	public Set<Entity> castSpell(ItemStack stack, SpellContext context) {
		ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
		ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
		EntitySpellProjectile projectile = new EntitySpellMine(context.caster.getEntityWorld(), context.caster);
		projectile.setInfo(context.caster, colorizer, stack);
		projectile.context = context;
		projectile.getEntityWorld().addEntity(projectile);
		HashSet<Entity> spellEntities = new HashSet<>();
		spellEntities.add(projectile);
		return spellEntities;
	}

	@Override
	public double getCostModifier(ItemStack stack) {
		return 1.151;
	}

	@Override
	public String getBulletType() {
		return "mine";
	}

	@Override
	public boolean isCADOnlyContainer(ItemStack stack) {
		return false;
	}
}
