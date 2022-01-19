/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import vazkii.psi.api.spell.detonator.IDetonationHandler;

import javax.annotation.Nonnull;

public class ItemDetonator extends Item {

	public ItemDetonator(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, @Nonnull Hand hand) {
		ItemStack itemStackIn = playerIn.getItemInHand(hand);

		if (!worldIn.isClientSide) {
			IDetonationHandler.performDetonation(worldIn, playerIn);
			worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1F, 1F);
		}

		else {
			playerIn.swing(hand);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, itemStackIn);
	}

}
