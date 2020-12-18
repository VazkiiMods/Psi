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

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.LazyValue;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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

public class ItemPsimetalArmor extends ArmorItem implements IPsimetalTool, IPsiEventArmor {

	public final EquipmentSlotType type;
	private final LazyValue<BipedModel<?>> model;

	private static final String TAG_TIMES_CAST = "timesCast";

	public ItemPsimetalArmor(EquipmentSlotType type, Properties props) {
		this(type, PsiAPI.PSIMETAL_ARMOR_MATERIAL, props);
	}

	public ItemPsimetalArmor(EquipmentSlotType type, IArmorMaterial mat, Properties props) {
		super(mat, type, props);
		this.type = type;
		this.model = DistExecutor.runForDist(() -> () -> new LazyValue<>(() -> this.provideArmorModelForSlot(type)),
				() -> () -> null);
	}

	@Override
	public void setDamage(ItemStack stack, int damage) {
		if (damage > stack.getMaxDamage()) {
			damage = stack.getDamage();
		}
		super.setDamage(stack, damage);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
		if (!isEnabled(stack)) {
			modifiers.removeAll(Attributes.ARMOR);
			modifiers.removeAll(Attributes.ARMOR_TOUGHNESS);
		}

		return modifiers;
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack) {
		String name = super.getTranslationKey(stack);
		if (!isEnabled(stack)) {
			name += ".broken";
		}
		return name;
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		IPsimetalTool.regen(stack, entityIn);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		return new ArmorSocketable(stack, 3);
	}

	public void cast(ItemStack stack, PsiArmorEvent event) {
		PlayerData data = PlayerDataHandler.get(event.getPlayer());
		ItemStack playerCad = PsiAPI.getPlayerCAD(event.getPlayer());

		if (isEnabled(stack) && !playerCad.isEmpty()) {
			int timesCast = stack.getOrCreateTag().getInt(TAG_TIMES_CAST);

			ItemStack bullet = ISocketable.socketable(stack).getSelectedBullet();
			ItemCAD.cast(event.getPlayer().getEntityWorld(), event.getPlayer(), data, bullet, playerCad, getCastCooldown(stack), 0, getCastVolume(), (SpellContext context) -> {
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
		if (event.type.equals(getTrueEvent(stack))) {
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
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<ITextComponent> tooltip, ITooltipFlag advanced) {
		TooltipHelper.tooltipIfShift(tooltip, () -> {
			ITextComponent componentName = ISocketable.getSocketedItemName(stack, "psimisc.none");
			tooltip.add(new TranslationTextComponent("psimisc.spell_selected", componentName));
			tooltip.add(new TranslationTextComponent(getTrueEvent(stack)));
		});
	}

	@Override
	public boolean isRepairable(ItemStack stack) {
		return super.isRepairable(stack);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
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
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
		return (A) model.getValue();
	}

	@OnlyIn(Dist.CLIENT)
	public BipedModel<?> provideArmorModelForSlot(EquipmentSlotType slot) {
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
