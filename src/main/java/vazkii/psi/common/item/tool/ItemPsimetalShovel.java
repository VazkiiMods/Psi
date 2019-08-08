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
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.arl.item.ItemMod;
import vazkii.arl.item.ItemModShovel;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.common.core.PsiCreativeTab;
import vazkii.psi.common.item.base.IPsiItem;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemPsimetalShovel extends ItemModShovel implements IPsimetalTool, IPsiItem {

	public ItemPsimetalShovel(String name) {
		super(name, PsiAPI.PSIMETAL_TOOL_MATERIAL);
		setCreativeTab(PsiCreativeTab.INSTANCE);
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
			damage = stack.getItemDamage();
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
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		IPsimetalTool.regen(stack, entityIn, isSelected);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, @Nonnull ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        String componentName = ItemMod.local(ISocketable.getSocketedItemName(stack, "psimisc.none"));
        TooltipHelper.addToTooltip(tooltip, "psimisc.spellSelected", componentName);
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
