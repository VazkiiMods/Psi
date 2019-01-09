/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [21/12/2018, 18:33:16 (GMT)]
 */
package vazkii.psi.client.jei.tricks;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.client.jei.JEICompat;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class TrickCraftingCategory implements IRecipeCategory<TrickCraftingRecipeJEI> {
    public static final TrickCraftingCategory INSTANCE = new TrickCraftingCategory();
    private static final IDrawable background = JEICompat.helpers.getGuiHelper().createDrawable(
            new ResourceLocation(LibMisc.MOD_ID, "textures/gui/jei/trick.png"), 0, 0, 96, 41);
    private static final int INPUT_SLOT = 0;
    private static final int CAD_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    @Nonnull
    @Override
    public String getUid() {
        return LibMisc.MOD_ID + ".trick";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return I18n.format("jei." + LibMisc.MOD_ID + ".category.trick");
    }

    @Nonnull
    @Override
    public String getModName() {
        return LibMisc.MOD_ID;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull TrickCraftingRecipeJEI recipeWrapper, @Nonnull IIngredients ingredients) {
        recipeLayout.getItemStacks().init(INPUT_SLOT, true, 0, 5);
        recipeLayout.getItemStacks().init(CAD_SLOT, true, 21, 23);
        recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 73, 5);

        recipeLayout.getItemStacks().set(ingredients);
    }
}
