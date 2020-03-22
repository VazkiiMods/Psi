/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [06/02/2016, 20:09:22 (GMT)]
 */
package vazkii.psi.common.item.tool;

import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemPsimetalShovel extends ShovelItem implements IPsimetalTool {

	public ItemPsimetalShovel(Item.Properties properties) {
		super(PsiAPI.PSIMETAL_TOOL_MATERIAL, 1.5F, -3.0F, properties.addToolType(ToolType.SHOVEL, PsiAPI.PSIMETAL_TOOL_MATERIAL.getHarvestLevel()));
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
		super.onBlockStartBreak(itemstack, pos, player);

		castOnBlockBreak(itemstack, player);

		return false;
	}

	@Override
	public void setDamage(ItemStack stack, int damage) {
		if (damage > stack.getMaxDamage())
			damage = stack.getDamage();
		super.setDamage(stack, damage);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
		if (!isEnabled(stack))
			modifiers.removeAll(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
		return modifiers;
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack) {
		String name = super.getTranslationKey(stack);
		if (!isEnabled(stack))
			name += ".broken";
		return name;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (!isEnabled(stack))
			return 1;
		return super.getDestroySpeed(stack, state);
	}


	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		IPsimetalTool.regen(stack, entityIn, isSelected);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, @Nonnull ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<ITextComponent> tooltip, ITooltipFlag advanced) {
		ITextComponent componentName = ISocketable.getSocketedItemName(stack, "psimisc.none");
		tooltip.add(new TranslationTextComponent("psimisc.spell_selected", componentName));
	}

	@Override
	public boolean getIsRepairable(ItemStack thisStack, @Nonnull ItemStack material) {
		return IPsimetalTool.isRepairableBy(material) || super.getIsRepairable(thisStack, material);
	}

	@Override
	public boolean requiresSneakForSpellSet(ItemStack stack) {
		return false;
	}

}
