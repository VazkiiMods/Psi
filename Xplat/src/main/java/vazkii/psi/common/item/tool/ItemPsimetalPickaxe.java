/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.common.item.base.ModDataComponents;

import java.util.List;

public class ItemPsimetalPickaxe extends PickaxeItem implements IPsimetalTool {

	public ItemPsimetalPickaxe(Item.Properties properties) {
		super(PsiAPI.PSIMETAL_TOOL_MATERIAL, properties.attributes(AxeItem.createAttributes(PsiAPI.PSIMETAL_TOOL_MATERIAL, 1, -2.8F)).component(ModDataComponents.BULLETS.get(), ItemContainerContents.EMPTY));
	}

	@Override
	public boolean mineBlock(@NotNull ItemStack itemstack, @NotNull Level world, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull LivingEntity player) {
		super.mineBlock(itemstack, world, state, pos, player);
		if(!(player instanceof Player)) {
			return false;
		}
		castOnBlockBreak(itemstack, (Player) player);

		return true;
	}

	@NotNull
	@Override
	public String getDescriptionId(@NotNull ItemStack stack) {
		String name = super.getDescriptionId(stack);
		if(!IPsimetalTool.isEnabled(stack)) {
			name += ".broken";
		}
		return name;
	}

	@Override
	public float getDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state) {
		if(!IPsimetalTool.isEnabled(stack)) {
			return 1;
		}
		return super.getDestroySpeed(stack, state);
	}

	@Override
	public void inventoryTick(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
		IPsimetalTool.regen(stack, entityIn);
	}

	public boolean shouldCauseReequipAnimation(@NotNull ItemStack oldStack, @NotNull ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable TooltipContext context, List<Component> tooltip, @NotNull TooltipFlag advanced) {
		Component componentName = ISocketable.getSocketedItemName(stack, "psimisc.none");
		tooltip.add(Component.translatable("psimisc.spell_selected", componentName));
	}

}
