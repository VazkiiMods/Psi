/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell.piece;

import net.minecraft.world.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.common.item.ItemCAD;

/**
 * Pieces extending this class can be used for crafting.
 */
public abstract class PieceCraftingTrick extends PieceTrick {
    public PieceCraftingTrick(Spell spell) {
        super(spell);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        if (cad.getItem() instanceof ItemCAD) {
            ((ItemCAD) cad.getItem()).craft(cad, context.caster, this);
        }
        return null;
    }

    /**
     * Whether the passed trick can craft the recipe containing this trick instance.
     * Recipes requiring no trick will always work.
     *
     * @param trick Trick from casted spell
     */
    public abstract boolean canCraft(PieceCraftingTrick trick);

}
