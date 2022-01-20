/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.tool;

import com.google.common.collect.Multimap;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

public class ItemPsimetalPickaxe extends PickaxeItem implements IPsimetalTool {

	public ItemPsimetalPickaxe(Item.Properties properties) {
		super(PsiAPI.PSIMETAL_TOOL_MATERIAL, 1, -2.8F, properties);
	}

	@Override
	public boolean mineBlock(ItemStack itemstack, Level world, BlockState state, BlockPos pos, LivingEntity player) {
		super.mineBlock(itemstack, world, state, pos, player);
		if (!(player instanceof Player)) {
			return false;
		}
		castOnBlockBreak(itemstack, (Player) player);

		return true;
	}

	@Override
	public void setDamage(ItemStack stack, int damage) {
		if (damage > stack.getMaxDamage()) {
			damage = stack.getDamageValue();
		}
		super.setDamage(stack, damage);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
		if (!isEnabled(stack)) {
			modifiers.removeAll(Attributes.ATTACK_DAMAGE);
		}
		return modifiers;
	}

	@Nonnull
	@Override
	public String getDescriptionId(ItemStack stack) {
		String name = super.getDescriptionId(stack);
		if (!isEnabled(stack)) {
			name += ".broken";
		}
		return name;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (!isEnabled(stack)) {
			return 1;
		}
		return super.getDestroySpeed(stack, state);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		IPsimetalTool.regen(stack, entityIn);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, @Nonnull ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level playerIn, List<Component> tooltip, TooltipFlag advanced) {
		Component componentName = ISocketable.getSocketedItemName(stack, "psimisc.none");
		tooltip.add(new TranslatableComponent("psimisc.spell_selected", componentName));
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return IPsimetalTool.super.initCapabilities(stack, nbt);
	}
}
