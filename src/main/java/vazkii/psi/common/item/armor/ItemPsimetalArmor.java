/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.armor;

import com.mojang.serialization.Codec;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.exosuit.IPsiEventArmor;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.material.PsimetalArmorMaterial;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.client.model.ArmorModels;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.tool.IPsimetalTool;
import vazkii.psi.common.item.tool.ToolSocketable;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

@EventBusSubscriber(modid = LibMisc.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ItemPsimetalArmor extends ArmorItem implements IPsimetalTool, IPsiEventArmor {


    //private final LazyLoadedValue<HumanoidModel<?>> model;
    public final EquipmentSlot type;

    public ItemPsimetalArmor(ArmorItem.Type type, Properties props) {
        this(type, PsimetalArmorMaterial.PSIMETAL_ARMOR_MATERIAL, props);
    }

    public ItemPsimetalArmor(ArmorItem.Type type, Holder<ArmorMaterial> mat, Properties props) {
        super(mat, type, props);
        this.type = type.getSlot();
		/*this.model = DistExecutor.runForDist(() -> () -> new LazyLoadedValue<>(() -> this.provideArmorModelForSlot(type)),
				() -> () -> null);*/
    }

    @SubscribeEvent
    public static void adjustAttributes(ItemAttributeModifierEvent event) {
        if (!IPsimetalTool.isEnabled(event.getItemStack())) {
            event.removeAllModifiersFor(Attributes.ARMOR);
            event.removeAllModifiersFor(Attributes.ARMOR_TOUGHNESS);
        }
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (damage > stack.getMaxDamage()) {
            damage = stack.getDamageValue();
        }
        super.setDamage(stack, damage);
    }

    @Nonnull
    @Override
    public String getDescriptionId(ItemStack stack) {
        String name = super.getDescriptionId(stack);
        if (!IPsimetalTool.isEnabled(stack)) {
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
        PlayerData data = PlayerDataHandler.get(event.getEntity());
        ItemStack playerCad = PsiAPI.getPlayerCAD(event.getEntity());

        if (IPsimetalTool.isEnabled(stack) && !playerCad.isEmpty()) {
            int timesCast = stack.getOrDefault(ModItems.TAG_TIMES_CAST, 0);

            ItemStack bullet = ISocketable.socketable(stack).getSelectedBullet();
            ItemCAD.cast(event.getEntity().getCommandSenderWorld(), event.getEntity(), data, bullet, playerCad, getCastCooldown(stack), 0, getCastVolume(), (SpellContext context) -> {
                context.tool = stack;
                context.attackingEntity = event.attacker;
                context.damageTaken = event.damage;
                context.loopcastIndex = timesCast;
            }, (int) (data.calculateDamageDeduction((float) event.damage) * 0.75));

            stack.set(ModItems.TAG_TIMES_CAST, timesCast + 1);
        }
    }

    @Override
    public void onEvent(ItemStack stack, PsiArmorEvent event) {
        if (event.type.equals(getEvent(stack)) && event.getEntity() != null) {
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable TooltipContext context, List<Component> tooltip, TooltipFlag advanced) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            Component componentName = ISocketable.getSocketedItemName(stack, "psimisc.none");
            tooltip.add(Component.translatable("psimisc.spell_selected", componentName));
            tooltip.add(Component.translatable(getEvent(stack)));
        });
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return super.isRepairable(stack);
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return LibResources.MODEL_PSIMETAL_EXOSUIT;
    }

    public boolean hasCustomColor(@Nonnull ItemStack stack) {
        return true;
    }

    public int getColor(@Nonnull ItemStack stack) {
        return ICADColorizer.DEFAULT_SPELL_COLOR;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return ArmorModels.get(itemStack);
            }
        });
    }

    public static class ArmorSocketable extends ToolSocketable {
        public ArmorSocketable(ItemStack tool, int slots) {
            super(tool, slots);
        }

        @Override
        public void setSelectedSlot(int slot) {
            super.setSelectedSlot(slot);
            tool.set(ModItems.TAG_TIMES_CAST, 0);
        }

        @Override
        public void setBulletInSocket(int slot, ItemStack bullet) {
            super.setBulletInSocket(slot, bullet);
            tool.set(ModItems.TAG_TIMES_CAST, 0);
        }

    }

}
