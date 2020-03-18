/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [20/02/2016, 22:20:00 (GMT)]
 */
package vazkii.psi.common.item.armor;

import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.arl.util.RegistryHelper;
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
import vazkii.psi.common.lib.LibResources;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class ItemPsimetalArmor extends ArmorItem implements IPsimetalTool, IPsiEventArmor, IItemColorProvider {

    @OnlyIn(Dist.CLIENT)
    public static Function<EquipmentSlotType, BipedModel> modelSupplier;

    public final EquipmentSlotType type;

    private static final String TAG_TIMES_CAST = "timesCast";

    public ItemPsimetalArmor(String name, EquipmentSlotType type, Properties props) {
        this(name, type, PsiAPI.PSIMETAL_ARMOR_MATERIAL, props);
    }


    public ItemPsimetalArmor(String name, EquipmentSlotType type, IArmorMaterial mat, Properties props) {
        super(mat, type, props);
        this.type = type;
        RegistryHelper.register(this, name);
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
		if (!isEnabled(stack)) {
			modifiers.removeAll(SharedMonsterAttributes.ARMOR.getName());
			modifiers.removeAll(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName());
		}

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
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        IPsimetalTool.regen(stack, entityIn, isSelected);
    }


	public void cast(ItemStack stack, PsiArmorEvent event) {
        PlayerData data = PlayerDataHandler.get(event.getPlayer());
        ItemStack playerCad = PsiAPI.getPlayerCAD(event.getPlayer());

        if (isEnabled(stack) && !playerCad.isEmpty()) {
            int timesCast = ItemNBTHelper.getInt(stack, TAG_TIMES_CAST, 0);

            ItemStack bullet = getBulletInSocket(stack, getSelectedSlot(stack));
            ItemCAD.cast(event.getPlayer().getEntityWorld(), event.getPlayer(), data, bullet, playerCad, getCastCooldown(stack), 0, getCastVolume(), (SpellContext context) -> {
                context.tool = stack;
                context.attackingEntity = event.attacker;
                context.damageTaken = event.damage;
                context.loopcastIndex = timesCast;
            });

            ItemNBTHelper.setInt(stack, TAG_TIMES_CAST, timesCast + 1);
        }
	}


	@Override
	public void onEvent(ItemStack stack, PsiArmorEvent event) {
		if(event.type.equals(getTrueEvent(stack)))
			cast(stack, event);
	}
	
	@Override
	public void setSelectedSlot(ItemStack stack, int slot) {
		IPsimetalTool.super.setSelectedSlot(stack, slot);
		ItemNBTHelper.setInt(stack, TAG_TIMES_CAST, 0);
	}
	
	@Override
	public void setBulletInSocket(ItemStack stack, int slot, ItemStack bullet) {
		IPsimetalTool.super.setBulletInSocket(stack, slot, bullet);
		ItemNBTHelper.setInt(stack, TAG_TIMES_CAST, 0);
	}

	public String getEvent(ItemStack stack) {
		return PsiArmorEvent.NONE;
	}

	public String getTrueEvent(ItemStack stack) {
		return ItemNBTHelper.getString(stack, "PsiEvent", getEvent(stack));
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
    public boolean getIsRepairable(ItemStack thisStack, @Nonnull ItemStack material) {
        return IPsimetalTool.isRepairableBy(material) || super.getIsRepairable(thisStack, material);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        boolean overlay = type != null && type.equals("overlay");
        return overlay ? LibResources.MODEL_PSIMETAL_EXOSUIT_SENSOR : LibResources.MODEL_PSIMETAL_EXOSUIT;
	}

	public boolean hasColor(@Nonnull ItemStack stack) {
		return true;
	}


    public int getColor(@Nonnull ItemStack stack) {
		return ICADColorizer.DEFAULT_SPELL_COLOR;
	}


	@Override
	@OnlyIn(Dist.CLIENT)
	public IItemColor getItemColor() {
		return (stack, tintIndex) -> tintIndex == 1 ? getColor(stack) : 0xFFFFFF;
	}


	@Nullable
	@Override
	@OnlyIn(Dist.CLIENT)
	public BipedModel getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, BipedModel _default) {
		return provideArmorModelForSlot(slot);
	}

	@OnlyIn(Dist.CLIENT)
	public BipedModel provideArmorModelForSlot(EquipmentSlotType slot) {
		return new ModelPsimetalExosuit(slot);
	}

	@Override
	public boolean requiresSneakForSpellSet(ItemStack stack) {
		return false;
	}
	
}
