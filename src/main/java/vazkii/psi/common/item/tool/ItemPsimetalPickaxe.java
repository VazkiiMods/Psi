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

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

public class ItemPsimetalPickaxe extends PickaxeItem implements IPsimetalTool {

	public ItemPsimetalPickaxe(Item.Properties properties) {
		super(PsiAPI.PSIMETAL_TOOL_MATERIAL, 1, -2.8F, properties.addToolType(ToolType.PICKAXE, PsiAPI.PSIMETAL_TOOL_MATERIAL.getLevel()));
	}

	@Override
	public boolean mineBlock(ItemStack itemstack, World world, BlockState state, BlockPos pos, LivingEntity player) {
		super.mineBlock(itemstack, world, state, pos, player);
		if (!(player instanceof PlayerEntity)) {
			return false;
		}
		castOnBlockBreak(itemstack, (PlayerEntity) player);

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
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
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
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		IPsimetalTool.regen(stack, entityIn);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, @Nonnull ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable World playerIn, List<ITextComponent> tooltip, ITooltipFlag advanced) {
		ITextComponent componentName = ISocketable.getSocketedItemName(stack, "psimisc.none");
		tooltip.add(new TranslationTextComponent("psimisc.spell_selected", componentName));
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		return IPsimetalTool.super.initCapabilities(stack, nbt);
	}
}
