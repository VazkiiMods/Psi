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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import vazkii.arl.item.ItemMod;
import vazkii.psi.api.spell.detonator.IDetonationHandler;
import vazkii.psi.common.core.PsiCreativeTab;
import vazkii.psi.common.item.base.IPsiItem;
import vazkii.psi.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemDetonator extends ItemMod implements IPsiItem {

	public ItemDetonator() {
		super(LibItemNames.DETONATOR);
		setMaxStackSize(1);
		setCreativeTab(PsiCreativeTab.INSTANCE);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand hand) {
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		IDetonationHandler.performDetonation(worldIn, playerIn);

		if(!worldIn.isRemote)
			worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1F, 1F);
		else playerIn.swingArm(hand);

		return new ActionResult<>(ActionResultType.SUCCESS, itemStackIn);
	}

}
