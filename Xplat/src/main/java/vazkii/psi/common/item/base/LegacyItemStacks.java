/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.base;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/**
 * Reads item stacks saved by pre-1.20.5 Psi as {@code {id, Count, tag}}. Modded block entity and
 * item data is never run through DataFixerUpper, so {@link ItemStack#CODEC} alone would drop the
 * legacy count and NBT tag; the tag is re-injected as custom data so the item's own
 * {@code verifyComponentsAfterLoad} migration can pick it up.
 */
public final class LegacyItemStacks {

	private LegacyItemStacks() {}

	public static ItemStack parse(HolderLookup.Provider provider, CompoundTag compound) {
		ItemStack stack = ItemStack.parseOptional(provider, compound);
		if(stack.isEmpty()) {
			return stack;
		}

		if(compound.contains("Count", Tag.TAG_ANY_NUMERIC)) {
			stack.setCount(compound.getInt("Count"));
		}
		if(compound.contains("tag", Tag.TAG_COMPOUND)) {
			stack.applyComponents(DataComponentPatch.builder()
					.set(DataComponents.CUSTOM_DATA, CustomData.of(compound.getCompound("tag")))
					.build());
		}
		return stack;
	}
}
