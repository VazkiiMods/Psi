/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.exosuit.IPsiEventArmor;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.material.PsimetalArmorMaterial;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerData;
import vazkii.psi.common.core.handler.PsiPlayerData;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModDataComponents;
import vazkii.psi.common.item.tool.IPsimetalTool;
import vazkii.psi.common.item.tool.ToolSocketable;
import vazkii.psi.common.lib.LibResources;

import java.util.List;

public class ItemPsimetalArmor extends ArmorItem implements IPsimetalTool, IPsiEventArmor {

	public ItemPsimetalArmor(ArmorItem.Type type, Properties props) {
		this(type, PsimetalArmorMaterial.PSIMETAL_ARMOR_MATERIAL.holder(), props);
	}

	public ItemPsimetalArmor(ArmorItem.Type type, Holder<ArmorMaterial> mat, Properties props) {
		super(mat, type, props.component(ModDataComponents.BULLETS.get(), ItemContainerContents.EMPTY));
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
	public void inventoryTick(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
		IPsimetalTool.regen(stack, entityIn);
	}

	public void cast(ItemStack stack, PsiArmorEvent event) {
		PlayerData data = PsiPlayerData.get(event.getEntity());
		ItemStack playerCad = PsiAPI.getPlayerCAD(event.getEntity());

		if(IPsimetalTool.isEnabled(stack) && !playerCad.isEmpty()) {
			int timesCast = stack.getOrDefault(ModDataComponents.TIMES_CAST.get(), 0);

			ItemStack bullet = ISocketable.socketable(stack).getSelectedBullet();
			ItemCAD.cast(event.getEntity().getCommandSenderWorld(), event.getEntity(), data, bullet, playerCad, getCastCooldown(stack), 0, getCastVolume(), (SpellContext context) -> {
				context.tool = stack;
				context.attackingEntity = event.attacker;
				context.damageTaken = event.damage;
				context.loopcastIndex = timesCast;
			}, (int) (data.calculateDamageDeduction((float) event.damage) * 0.75));

			stack.set(ModDataComponents.TIMES_CAST.get(), timesCast + 1);
		}
	}

	@Override
	public void onEvent(ItemStack stack, PsiArmorEvent event) {
		if(event.type.equals(getEvent(stack))) {
			cast(stack, event);
		}
	}

	public String getEvent(ItemStack stack) {
		return PsiArmorEvent.NONE;
	}

	public int getCastCooldown(ItemStack stack) {
		return 5;
	}

	public float getCastVolume() {
		return 0.025F;
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag advanced) {
		TooltipHelper.tooltipIfShift(tooltip, () -> {
			Component componentName = ISocketable.getSocketedItemName(stack, "psimisc.none");
			tooltip.add(Component.translatable("psimisc.spell_selected", componentName));
			tooltip.add(Component.translatable(getEvent(stack)));
		});
	}

	public ResourceLocation getArmorTexture(@NotNull ItemStack stack, @NotNull Entity entity, @NotNull EquipmentSlot slot, ArmorMaterial.@NotNull Layer layer, boolean innerModel) {
		return LibResources.MODEL_PSIMETAL_EXOSUIT;
	}

	public int getColor(@NotNull ItemStack stack) {
		return ICADColorizer.DEFAULT_SPELL_COLOR;
	}

	@Override
	public boolean keepsAttributeWhenBroken(Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute) {
		return !attribute.is(Attributes.ARMOR) && !attribute.is(Attributes.ARMOR_TOUGHNESS);
	}

	public static class ArmorSocketable extends ToolSocketable {
		public ArmorSocketable(ItemStack tool, int slots) {
			super(tool, slots);
		}

		@Override
		public void setSelectedSlot(int slot) {
			super.setSelectedSlot(slot);
			tool.set(ModDataComponents.TIMES_CAST.get(), 0);
		}

		@Override
		public void setBulletInSocket(int slot, ItemStack bullet) {
			super.setBulletInSocket(slot, bullet);
			tool.set(ModDataComponents.TIMES_CAST.get(), 0);
		}

	}

}
