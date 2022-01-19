/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.material;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

import vazkii.psi.api.PsiAPI;

public class PsimetalToolMaterial implements Tier {
	private static final LazyLoadedValue<Ingredient> REPAIR_MATERIAL = new LazyLoadedValue<>(
			() -> Ingredient.of(Registry.ITEM.get(new ResourceLocation(PsiAPI.MOD_ID, "psimetal"))));

	@Override
	public int getUses() {
		return 900;
	}

	@Override
	public float getSpeed() {
		return 7.8F;
	}

	@Override
	public float getAttackDamageBonus() {
		return 2F;
	}

	@Override
	public int getLevel() {
		return 3;
	}

	@Override
	public int getEnchantmentValue() {
		return 12;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return REPAIR_MATERIAL.get();
	}
}
