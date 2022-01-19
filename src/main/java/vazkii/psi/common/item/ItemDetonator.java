/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

import vazkii.psi.api.spell.detonator.IDetonationHandler;

import javax.annotation.Nonnull;

public class ItemDetonator extends Item {

	public ItemDetonator(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @Nonnull InteractionHand hand) {
		ItemStack itemStackIn = playerIn.getItemInHand(hand);

		if (!worldIn.isClientSide) {
			IDetonationHandler.performDetonation(worldIn, playerIn);
			worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.UI_BUTTON_CLICK, SoundSource.PLAYERS, 1F, 1F);
		}

		else {
			playerIn.swing(hand);
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStackIn);
	}

}
