/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [05/02/2016, 00:40:08 (GMT)]
 */
package vazkii.psi.api.cad;

import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.PsiAPI;

import java.util.List;

public interface ICADAssembly {

    default ItemStack createCADStack(ItemStack stack, List<ItemStack> allComponents) {
        return PsiAPI.internalHandler.createDefaultCAD(allComponents);
    }

    @OnlyIn(Dist.CLIENT)
    ModelResourceLocation getCADModel(ItemStack stack, ItemStack cad);

    @OnlyIn(Dist.CLIENT)
    ResourceLocation getCadTexture(ItemStack stack, ItemStack cad);

}
