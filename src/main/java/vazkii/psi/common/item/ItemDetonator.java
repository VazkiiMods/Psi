/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [19/02/2016, 18:08:41 (GMT)]
 */
package vazkii.psi.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import vazkii.arl.item.ItemMod;
import vazkii.psi.common.entity.EntitySpellCharge;
import vazkii.psi.common.item.base.IPsiItem;
import vazkii.psi.common.lib.LibItemNames;

public class ItemDetonator extends ItemMod implements IPsiItem {

	public ItemDetonator() {
		super(LibItemNames.DETONATOR);
		setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		List<EntitySpellCharge> charges = worldIn.getEntitiesWithinAABB(EntitySpellCharge.class, playerIn.getEntityBoundingBox().expand(32, 32, 32));
		if(!charges.isEmpty())
			for(EntitySpellCharge c : charges)
				c.doExplosion();

		if(!worldIn.isRemote)
			worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1F, 1F);
		else playerIn.swingArm(hand);

		return new ActionResult<ItemStack>(charges.isEmpty() ? EnumActionResult.PASS : EnumActionResult.SUCCESS, itemStackIn);
	}

}
