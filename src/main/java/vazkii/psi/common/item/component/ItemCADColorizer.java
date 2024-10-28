/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.common.item.base.ModItems;

public class ItemCADColorizer extends ItemCADComponent implements ICADColorizer {


    private final DyeColor color;

    public ItemCADColorizer(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
    }

    public ItemCADColorizer(Properties properties) {
        super(properties);
        color = DyeColor.BLACK;
    }

    private static String getProperDyeName(DyeColor color) {
        return color.getSerializedName();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getColor(ItemStack stack) {
        return FastColor.ARGB32.opaque(color.getTextColor());
    } //TODO check if text color is proper

    @Override
    public String getContributorName(ItemStack stack) {
        return stack.getOrDefault(ModItems.TAG_CONTRIBUTOR, "");
    }

    @Override
    public void setContributorName(ItemStack stack, String name) {
        stack.set(ModItems.TAG_CONTRIBUTOR, name);
    }
}
