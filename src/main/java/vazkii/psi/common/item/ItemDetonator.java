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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import vazkii.psi.api.spell.detonator.IDetonationHandler;

import javax.annotation.Nonnull;

public class ItemDetonator extends Item {

	public ItemDetonator(Item.Properties properties) {
		super(properties.maxStackSize(1));
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand hand) {
		ItemStack itemStackIn = playerIn.getHeldItem(hand);


		if (!worldIn.isRemote){
			IDetonationHandler.performDetonation(worldIn, playerIn);
			worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1F, 1F);
		}

        else playerIn.swingArm(hand);

		return new ActionResult<>(ActionResultType.SUCCESS, itemStackIn);
	}

}
