/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@EventBusSubscriber(modid = LibMisc.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ItemPsimetalPickaxe extends PickaxeItem implements IPsimetalTool {

    public ItemPsimetalPickaxe(Item.Properties properties) {
        super(PsiAPI.PSIMETAL_TOOL_MATERIAL, properties.attributes(AxeItem.createAttributes(PsiAPI.PSIMETAL_TOOL_MATERIAL, 1, -2.8F)));
    }

    @SubscribeEvent
    public static void adjustAttributes(ItemAttributeModifierEvent event) {
        if (!IPsimetalTool.isEnabled(event.getItemStack())) {
            event.removeAllModifiersFor(Attributes.ATTACK_DAMAGE);
        }
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
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (!IPsimetalTool.isEnabled(stack)) {
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
    public void appendHoverText(ItemStack stack, @Nullable TooltipContext context, List<Component> tooltip, TooltipFlag advanced) {
        Component componentName = ISocketable.getSocketedItemName(stack, "psimisc.none");
        tooltip.add(Component.translatable("psimisc.spell_selected", componentName));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return IPsimetalTool.super.initCapabilities(stack, nbt);
    }
}
