/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.armor;

import com.google.common.collect.Multimap;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.DistExecutor;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.exosuit.IPsiEventArmor;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.client.model.ModelPsimetalExosuit;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.tool.IPsimetalTool;
import vazkii.psi.common.item.tool.ToolSocketable;
import vazkii.psi.common.lib.LibResources;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class ItemPsimetalArmor extends ArmorItem implements IPsimetalTool, IPsiEventArmor {

	public final EquipmentSlot type;
	private final LazyLoadedValue<HumanoidModel<?>> model;

	private static final String TAG_TIMES_CAST = "timesCast";

	public ItemPsimetalArmor(EquipmentSlot type, Properties props) {
		this(type, PsiAPI.PSIMETAL_ARMOR_MATERIAL, props);
	}

	public ItemPsimetalArmor(EquipmentSlot type, ArmorMaterial mat, Properties props) {
		super(mat, type, props);
		this.type = type;
		this.model = DistExecutor.runForDist(() -> () -> new LazyLoadedValue<>(() -> this.provideArmorModelForSlot(type)),
				() -> () -> null);
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
			modifiers.removeAll(Attributes.ARMOR);
			modifiers.removeAll(Attributes.ARMOR_TOUGHNESS);
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
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		IPsimetalTool.regen(stack, entityIn);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new ArmorSocketable(stack, 3);
	}

	public void cast(ItemStack stack, PsiArmorEvent event) {
		PlayerData data = PlayerDataHandler.get(event.getPlayer());
		ItemStack playerCad = PsiAPI.getPlayerCAD(event.getPlayer());

		if (isEnabled(stack) && !playerCad.isEmpty()) {
			int timesCast = stack.getOrCreateTag().getInt(TAG_TIMES_CAST);

			ItemStack bullet = ISocketable.socketable(stack).getSelectedBullet();
			ItemCAD.cast(event.getPlayer().getCommandSenderWorld(), event.getPlayer(), data, bullet, playerCad, getCastCooldown(stack), 0, getCastVolume(), (SpellContext context) -> {
				context.tool = stack;
				context.attackingEntity = event.attacker;
				context.damageTaken = event.damage;
				context.loopcastIndex = timesCast;
			}, (int) (data.calculateDamageDeduction((float) event.damage) * 0.75));

			stack.getOrCreateTag().putInt(TAG_TIMES_CAST, timesCast + 1);
		}
	}

	@Override
	public void onEvent(ItemStack stack, PsiArmorEvent event) {
		if (event.type.equals(getTrueEvent(stack)) && event.getPlayer() != null) {
			cast(stack, event);
		}
	}

	public String getEvent(ItemStack stack) {
		return PsiArmorEvent.NONE;
	}

	public String getTrueEvent(ItemStack stack) {
		return stack.getOrCreateTag().getString("PsiEvent").isEmpty() ? getEvent(stack) : stack.getOrCreateTag().getString("PsiEvent");
	}

	public int getCastCooldown(ItemStack stack) {
		return 5;
	}

	public float getCastVolume() {
		return 0.025F;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level playerIn, List<Component> tooltip, TooltipFlag advanced) {
		TooltipHelper.tooltipIfShift(tooltip, () -> {
			Component componentName = ISocketable.getSocketedItemName(stack, "psimisc.none");
			tooltip.add(new TranslatableComponent("psimisc.spell_selected", componentName));
			tooltip.add(new TranslatableComponent(getTrueEvent(stack)));
		});
	}

	@Override
	public boolean isRepairable(ItemStack stack) {
		return super.isRepairable(stack);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return LibResources.MODEL_PSIMETAL_EXOSUIT;
	}

	public boolean hasColor(@Nonnull ItemStack stack) {
		return true;
	}

	public int getColor(@Nonnull ItemStack stack) {
		return ICADColorizer.DEFAULT_SPELL_COLOR;
	}

	@Nullable
	@Override
	@OnlyIn(Dist.CLIENT)
	@SuppressWarnings("unchecked")
	public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
		return (A) model.get();
	}

	@OnlyIn(Dist.CLIENT)
	public HumanoidModel<?> provideArmorModelForSlot(EquipmentSlot slot) {
		return new ModelPsimetalExosuit(slot);
	}

	public static class ArmorSocketable extends ToolSocketable {
		public ArmorSocketable(ItemStack tool, int slots) {
			super(tool, slots);
		}

		@Override
		public void setSelectedSlot(int slot) {
			super.setSelectedSlot(slot);
			tool.getOrCreateTag().putInt(TAG_TIMES_CAST, 0);
		}

		@Override
		public void setBulletInSocket(int slot, ItemStack bullet) {
			super.setBulletInSocket(slot, bullet);
			tool.getOrCreateTag().putInt(TAG_TIMES_CAST, 0);
		}

	}

}
