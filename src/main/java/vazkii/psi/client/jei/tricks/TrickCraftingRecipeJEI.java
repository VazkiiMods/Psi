/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [21/12/2018, 21:11:10 (GMT)]
 */
package vazkii.psi.client.jei.tricks;

import com.google.common.collect.Lists;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.recipe.TrickRecipe;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.jei.JEICompat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TrickCraftingRecipeJEI implements IRecipeWrapper {
    private static final IDrawable programmerHover = JEICompat.helpers.getGuiHelper().createDrawable(
            new ResourceLocation("psi", "textures/gui/programmer.png"), 16, 184, 16, 16);

    private static final int trickX = 43;
    private static final int trickY = 24;


    @Nullable
    private final IDrawable icon;
    private final SpellPiece piece;
    private final TrickRecipe recipe;

    public TrickCraftingRecipeJEI(TrickRecipe recipe) {
        this.recipe = recipe;
        ResourceLocation location = PsiAPI.simpleSpellTextures.get(recipe.getPiece());
        Class<? extends SpellPiece> pieceType = PsiAPI.spellPieceRegistry.getObject(recipe.getPiece());

        icon = location != null ? JEICompat.helpers.getGuiHelper().createDrawable(location, 0, 0, 256, 256) : null;
        piece = pieceType != null ? SpellPiece.create(pieceType, new Spell()) : null;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class,
                Lists.newArrayList(
                        Lists.newArrayList(recipe.getInput().getMatchingStacks()),
                        Lists.newArrayList(recipe.getCAD())));
        ingredients.setOutput(ItemStack.class, recipe.getOutput());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (icon != null) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.0625f, 0.0625f, 0.0625f);
            icon.draw(minecraft, trickX * 16, trickY * 16);
            GlStateManager.color(1f, 1f, 1f);
            GlStateManager.popMatrix();

            if (onTrick(mouseX, mouseY))
                programmerHover.draw(minecraft, trickX, trickY);
        }

    }

    @Nonnull
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        List<String> tooltip = Lists.newArrayList();
        if (onTrick(mouseX, mouseY) && piece != null) {
            piece.getTooltip(tooltip);
            return tooltip;
        }
        return tooltip;
    }

    public boolean onTrick(int mouseX, int mouseY) {
        return (mouseX >= trickX && mouseX <= trickX + 16 && mouseY >= trickY && mouseY <= trickY + 16);
    }

}
