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

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.item.ItemMod;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.PsiCreativeTab;
import vazkii.psi.common.core.handler.LoopcastTrackingHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.entity.*;
import vazkii.psi.common.item.base.IPsiItem;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorRaycast;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemSpellBullet extends ItemMod implements ISpellContainer, IPsiItem {

	private static final String TAG_SPELL = "spell";

	public static final String[] VARIANTS = {
			"spell_bullet",
			"spell_bullet_active",
			"spell_bullet_projectile",
			"spell_bullet_projectile_active",
			"spell_bullet_loop",
			"spell_bullet_loop_active",
			"spell_bullet_circle",
			"spell_bullet_circle_active",
			"spell_bullet_grenade",
			"spell_bullet_grenade_active",
			"spell_bullet_charge",
			"spell_bullet_charge_active",
			"spell_bullet_mine",
			"spell_bullet_mine_active"
	};

	public ItemSpellBullet() {
		super(LibItemNames.SPELL_BULLET, VARIANTS);
		setMaxStackSize(1);
		setCreativeTab(PsiCreativeTab.INSTANCE);
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return getSpell(stack) != null;
	}

	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
		return itemStack.copy();
	}

	@Nonnull
	@Override
	public String getItemStackDisplayName(@Nonnull ItemStack stack) {
		if(!containsSpell(stack))
			return super.getItemStackDisplayName(stack);

		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_SPELL, false);
		String name = cmp.getString(Spell.TAG_SPELL_NAME); // We don't need to load the whole spell just for the name
		if(name.isEmpty())
			return super.getItemStackDisplayName(stack);

		return name;
	}

	@Override
	public void setSpell(EntityPlayer player, ItemStack stack, Spell spell) {
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

	@Nonnull
	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack) {
		return containsSpell(stack) ? EnumRarity.RARE : EnumRarity.COMMON;
	}

	@Override
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
		if(isInCreativeTab(tab))
			for(int i = 0; i < getVariants().length; i++)
				if(i % 2 == 0)
					subItems.add(new ItemStack(this, 1, i));
	}

    @SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            TooltipHelper.addToTooltip(tooltip, "psimisc.bulletType", local("psi.bulletType" + stack.getItemDamage() / 2));
            TooltipHelper.addToTooltip(tooltip, "psimisc.bulletCost", (int) (getCostModifier(stack) * 100));
		});
	}

	@Override
	public void castSpell(ItemStack stack, SpellContext context) {
		ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
		ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);

		EntitySpellProjectile projectile = null;

		switch (stack.getItemDamage()) {
			case 1: // Basic
				context.cspell.safeExecute(context);
				break;

			case 3: // Projectile
				projectile = new EntitySpellProjectile(context.caster.getEntityWorld(), context.caster);
				break;

			case 5: // Loopcast
				PlayerDataHandler.PlayerData data = PlayerDataHandler.get(context.caster);
				if (!data.loopcasting || context.castFrom != data.loopcastHand) {
					context.cspell.safeExecute(context);
					data.loopcasting = true;
					data.loopcastHand = context.castFrom;
					data.lastTickLoopcastStack = null;
					if (context.caster instanceof EntityPlayerMP)
						LoopcastTrackingHandler.syncForTrackers((EntityPlayerMP) context.caster);
				}

				break;

			case 7: // Spell Circle
				RayTraceResult pos = PieceOperatorVectorRaycast.raycast(context.caster, 32);

				if (pos != null) {
					EntitySpellCircle circle = new EntitySpellCircle(context.caster.getEntityWorld());
					circle.setInfo(context.caster, colorizer, stack);
					circle.setPosition(pos.hitVec.x, pos.hitVec.y, pos.hitVec.z);
					circle.getEntityWorld().spawnEntity(circle);
				}

				break;

			case 9: // Grenade
				projectile = new EntitySpellGrenade(context.caster.getEntityWorld(), context.caster);
				break;

			case 11: // Charge
				projectile = new EntitySpellCharge(context.caster.getEntityWorld(), context.caster);
				break;

			case 13: // Mine
				projectile = new EntitySpellMine(context.caster.getEntityWorld(), context.caster);
				break;
		}

		if (projectile != null) {
			projectile.setInfo(context.caster, colorizer, stack);
			projectile.context = context;
			projectile.getEntityWorld().spawnEntity(projectile);
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
	
	@Override
	public boolean requiresSneakForSpellSet(ItemStack stack) {
		return false;
	}

}
