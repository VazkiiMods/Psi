/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [21/12/2018, 18:43:02 (GMT)]
 */
package vazkii.psi.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import vazkii.psi.api.spell.piece.PieceTrick;

public class TrickRecipe {
	private final PieceTrick piece;
	private final Ingredient input;
	private final ItemStack output;
	private final ItemStack cad;

	public TrickRecipe(PieceTrick trick, Ingredient input, ItemStack output, ItemStack CAD) {
		this.piece = trick;
		this.input = input;
		this.output = output;
		this.cad = CAD;
	}

	public PieceTrick getPiece() {
		return piece;
	}

	public Ingredient getInput() {
		return input;
	}

	public ItemStack getOutput() {
		return output;
	}

	public ItemStack getCAD() {
		return cad;
	}
}
