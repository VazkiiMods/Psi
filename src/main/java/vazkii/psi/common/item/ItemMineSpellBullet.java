/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.entity.EntitySpellMine;
import vazkii.psi.common.entity.EntitySpellProjectile;

import java.util.ArrayList;

public class ItemMineSpellBullet extends ItemSpellBullet {

	public ItemMineSpellBullet(Properties properties) {
		super(properties);
	}

	@Override
	public ArrayList<Entity> castSpell(ItemStack stack, SpellContext context) {
		ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
		ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
		EntitySpellProjectile projectile = new EntitySpellMine(context.caster.getCommandSenderWorld(), context.caster);
		projectile.setInfo(context.caster, colorizer, stack);
		projectile.context = context;
		projectile.getCommandSenderWorld().addFreshEntity(projectile);
		ArrayList<Entity> spellEntities = new ArrayList<>();
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

}
