/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.material;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class PsimetalToolMaterial implements IItemTier {
	@Override
	public int getMaxUses() {
		return 900;
	}

	@Override
	public float getEfficiency() {
		return 7.8F;
	}

	@Override
	public float getAttackDamage() {
		return 2F;
	}

	@Override
	public int getHarvestLevel() {
		return 3;
	}

	@Override
	public int getEnchantability() {
		return 12;
	}

	@Override
	public Ingredient getRepairMaterial() {
		return Ingredient.fromTag(ItemTags.getCollection().getTagByID(new ResourceLocation("forge", "ingots/psimetal")));
	}
}
