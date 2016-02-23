/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [13/01/2016, 16:48:52 (GMT)]
 */
package vazkii.psi.common.item;

import java.util.List;

import com.google.common.collect.ImmutableSet;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.helper.ItemNBTHelper;
import vazkii.psi.common.entity.EntitySpellCharge;
import vazkii.psi.common.entity.EntitySpellCircle;
import vazkii.psi.common.entity.EntitySpellGrenade;
import vazkii.psi.common.entity.EntitySpellMine;
import vazkii.psi.common.entity.EntitySpellProjectile;
import vazkii.psi.common.item.base.ItemMod;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorRaycast;

public class ItemSpellBullet extends ItemMod implements ISpellContainer {

	private static final String TAG_SPELL = "spell";

	public static final String[] VARIANTS = {
			"spellBullet",
			"spellBulletActive",
			"spellBulletProjectile",
			"spellBulletProjectileActive",
			"spellBulletLoop",
			"spellBulletLoopActive",
			"spellBulletCircle",
			"spellBulletCircleActive",
			"spellBulletGrenade",
			"spellBulletGrenadeActive",
			"spellBulletCharge",
			"spellBulletChargeActive",
			"spellBulletMine",
			"spellBulletMineActive"
	};

	public ItemSpellBullet() {
		super(LibItemNames.SPELL_BULLET, VARIANTS);
		setMaxStackSize(1);
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return getSpell(stack) != null;
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
		return itemStack.copy();
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if(!containsSpell(stack))
			return super.getItemStackDisplayName(stack);

		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_SPELL, false);
		String name = cmp.getString(Spell.TAG_SPELL_NAME); // We don't need to load the whole spell just for the name
		if(name == null || name.isEmpty())
			return super.getItemStackDisplayName(stack);

		return name;
	}

	@Override
	public void setSpell(ItemStack stack, Spell spell) {
		ItemSpellDrive.setSpell(stack, spell);

		if(!containsSpell(stack))
			stack.setItemDamage(stack.getItemDamage() + 1);
	}

	@Override
	public Spell getSpell(ItemStack stack) {
		return ItemSpellDrive.getSpell(stack);
	}

	@Override
	public boolean containsSpell(ItemStack stack) {
		return stack.getItemDamage() % 2 == 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack) {
		return containsSpell(stack) ? EnumRarity.RARE : EnumRarity.COMMON;
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for(int i = 0; i < getVariants().length; i++)
			if(i % 2 == 0)
				subItems.add(new ItemStack(itemIn, 1, i));
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltipIfShift(tooltip, () -> {
			addToTooltip(tooltip, "psimisc.bulletType", local("psi.bulletType" + stack.getItemDamage() / 2));
			addToTooltip(tooltip, "psimisc.bulletCost", (int) (getCostModifier(stack) * 100));
		});
	}

	@Override
	public void castSpell(ItemStack stack, SpellContext context) {
		switch(stack.getItemDamage()) {
		case 1: // Basic
			context.cspell.safeExecute(context);
			break;

		case 3: // Projectile
			if(!context.caster.worldObj.isRemote) {
				EntitySpellProjectile proj = new EntitySpellProjectile(context.caster.worldObj, context.caster);
				ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
				ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
				proj.setInfo(context.caster, colorizer, stack);
				proj.context = context;
				proj.worldObj.spawnEntityInWorld(proj);
			}
			break;

		case 5: // Loopcast
			context.cspell.safeExecute(context);
			PlayerDataHandler.get(context.caster).loopcasting = true;
			break;

		case 7: // Spell Circle
			if(!context.caster.worldObj.isRemote) {
				try {
					MovingObjectPosition pos = PieceOperatorVectorRaycast.raycast(context.caster, 32);

					if(pos != null) {
						EntitySpellCircle circle = new EntitySpellCircle(context.caster.worldObj);
						ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
						ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
						circle.setInfo(context.caster, colorizer, stack);
						circle.setPosition(pos.hitVec.xCoord, pos.hitVec.yCoord, pos.hitVec.zCoord);
						circle.worldObj.spawnEntityInWorld(circle);
					}
				} catch(SpellRuntimeException e) { }
			}
			break;

		case 9: // Grenade
			if(!context.caster.worldObj.isRemote) {
				EntitySpellProjectile proj = new EntitySpellGrenade(context.caster.worldObj, context.caster);
				ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
				ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
				proj.setInfo(context.caster, colorizer, stack);
				proj.context = context;
				proj.worldObj.spawnEntityInWorld(proj);
			}
			break;

		case 11: // Charge
			if(!context.caster.worldObj.isRemote) {
				EntitySpellProjectile proj = new EntitySpellCharge(context.caster.worldObj, context.caster);
				ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
				ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
				proj.setInfo(context.caster, colorizer, stack);
				proj.context = context;
				proj.worldObj.spawnEntityInWorld(proj);
			}
			break;

		case 13: // Mine
			if(!context.caster.worldObj.isRemote) {
				EntitySpellProjectile proj = new EntitySpellMine(context.caster.worldObj, context.caster);
				ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
				ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
				proj.setInfo(context.caster, colorizer, stack);
				proj.context = context;
				proj.worldObj.spawnEntityInWorld(proj);
			}
			break;
		}
	}

	@Override
	public double getCostModifier(ItemStack stack) {
		switch(stack.getItemDamage()) {
		case 0: case 1: // Normal
			return 1.0;
		case 2: case 3: // Projectile
			return 1.02;
		case 4: case 5: // Loopcast
			return 1.0;
		case 6: case 7: // Spell Circle
			return EntitySpellCircle.CAST_TIMES * 0.75;
		case 8: case 9: // Grenade
			return 1.05;
		case 10: case 11: // Charge
			return 1.151;
		case 12: case 13: // Mine
			return 1.151;
		}
		return 0;
	}

	@Override
	public boolean isCADOnlyContainer(ItemStack stack) {
		return ImmutableSet.of(4, 5, 6, 7).contains(stack.getItemDamage());
	}

}
