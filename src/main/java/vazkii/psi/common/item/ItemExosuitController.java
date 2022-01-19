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
import net.minecraft.world.World;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.cad.ISocketableController;
import vazkii.psi.common.core.handler.PsiSoundHandler;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public class ItemExosuitController extends Item implements ISocketableController {

	private static final String TAG_SELECTED_CONTROL_SLOT = "selectedControlSlot";

	public ItemExosuitController(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, @Nonnull Hand hand) {
		ItemStack itemStackIn = playerIn.getItemInHand(hand);
		if (playerIn.isShiftKeyDown()) {
			if (!worldIn.isClientSide) {
				worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), PsiSoundHandler.compileError, SoundCategory.PLAYERS, 0.25F, 1F);
			} else {
				playerIn.swing(hand);
			}

			ItemStack[] stacks = getControlledStacks(playerIn, itemStackIn);

			for (ItemStack stack : stacks) {
				stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).ifPresent(c -> c.setSelectedSlot(3));
			}

			return new ActionResult<>(ActionResultType.SUCCESS, itemStackIn);
		}

		return new ActionResult<>(ActionResultType.PASS, itemStackIn);
	}

	@Override
	public ItemStack[] getControlledStacks(PlayerEntity player, ItemStack stack) {
		List<ItemStack> stacks = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			ItemStack armor = player.inventory.armor.get(3 - i);
			if (!armor.isEmpty() && ISocketable.isSocketable(armor)) {
				stacks.add(armor);
			}
		}

		return stacks.toArray(new ItemStack[0]);
	}

	@Override
	public int getDefaultControlSlot(ItemStack stack) {
		return stack.getOrCreateTag().getInt(TAG_SELECTED_CONTROL_SLOT);
	}

	@Override
	public void setSelectedSlot(PlayerEntity player, ItemStack stack, int controlSlot, int slot) {
		stack.getOrCreateTag().putInt(TAG_SELECTED_CONTROL_SLOT, controlSlot);

		ItemStack[] stacks = getControlledStacks(player, stack);
		if (controlSlot < stacks.length && !stacks[controlSlot].isEmpty()) {
			stacks[controlSlot].getCapability(PsiAPI.SOCKETABLE_CAPABILITY).ifPresent(cap -> cap.setSelectedSlot(slot));
		}
	}

}
