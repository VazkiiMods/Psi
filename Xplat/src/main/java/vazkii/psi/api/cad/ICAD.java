/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Base interface for a CAD. You probably shouldn't implement this,
 * unless you absolutely know what you are doing.
 */
public interface ICAD {
	/**
	 * Sets the component stack inside the CAD's respective component slot. The slot is
	 * resolved through {@link CADComponentLookup}: with a registry entry it stores the
	 * entry's holder, otherwise the bare item. {@code registries} may be null only when
	 * the component is known to implement {@link ICADComponent}, since a null provider
	 * skips the registry and the slot keeps no stats.
	 */
	static void setComponent(@Nullable HolderLookup.Provider registries, ItemStack stack, ItemStack componentStack) {
		Optional<EnumCADComponent> componentType = CADComponentLookup.componentType(registries, componentStack);
		if(componentType.isEmpty()) {
			return;
		}

		@Nullable
		List<CADComponentSlot> storedSlots = PsiAPI.internalHandler.getCADComponents(stack);
		List<CADComponentSlot> slots = storedSlots == null
				? new ArrayList<>(Collections.nCopies(EnumCADComponent.values().length, CADComponentSlot.EMPTY))
				: new ArrayList<>(storedSlots);
		slots.set(componentType.get().ordinal(), CADComponentSlot.of(registries, componentStack));
		PsiAPI.internalHandler.setCADComponents(stack, slots);
	}

	/**
	 * Creates a copy of two CADs' component lists in order to disassociate them.
	 *
	 * @param from The CAD to copy components from
	 * @param to   The CAD to copy components to
	 */
	static void copyComponents(ItemStack from, ItemStack to) {
		if(!(from.getItem() instanceof ICAD && to.getItem() instanceof ICAD)) {
			return;
		}

		List<CADComponentSlot> fromComponents = PsiAPI.internalHandler.getCADComponents(from);
		PsiAPI.internalHandler.setCADComponents(to,
				new ArrayList<>(Objects.requireNonNullElseGet(fromComponents,
						() -> Collections.nCopies(EnumCADComponent.values().length, CADComponentSlot.EMPTY))));
	}

	/**
	 * Sets the component in this slot for the CAD.
	 */
	default void setCADComponent(@Nullable HolderLookup.Provider registries, ItemStack stack, ItemStack component) {
		setComponent(registries, stack, component);
	}

	/**
	 * Gets the component used for this CAD in the given slot.
	 */
	ItemStack getComponentInSlot(ItemStack stack, EnumCADComponent type);

	/**
	 * Gets the stored slot for the given component type, holding the component's
	 * registry definition when it has one.
	 */
	default CADComponentSlot getComponentSlot(ItemStack stack, EnumCADComponent type) {
		@Nullable
		List<CADComponentSlot> slots = PsiAPI.internalHandler.getCADComponents(stack);
		return slots == null ? CADComponentSlot.EMPTY : slots.get(type.ordinal());
	}

	/**
	 * Gets the value of a given CAD stat.
	 */
	int getStatValue(ItemStack stack, EnumCADStat stat);

	/**
	 * Gets how much Psi is stored in this CAD's battery.
	 */
	int getStoredPsi(ItemStack stack);

	/**
	 * Has the CAD regen psi equal to the amount passed in. Will never go above
	 * the value of the CAD's OVERFLOW stat.
	 */
	void regenPsi(ItemStack stack, int psi);

	/**
	 * Consumes psi from the CAD's battery equal to the amount passed in. Returns
	 * the remainder that couldn't be consumed. Used to prevent damage.
	 */
	int consumePsi(ItemStack stack, int psi);

	/**
	 * Gets how many vectors this CAD can store in memory.
	 */
	int getMemorySize(ItemStack stack);

	void setStoredVector(ItemStack stack, int memorySlot, Vector3 vec) throws SpellRuntimeException;

	Vector3 getStoredVector(ItemStack stack, int memorySlot) throws SpellRuntimeException;

	int getTime(ItemStack stack);

	void incrementTime(ItemStack stack);

	/**
	 * Gets the color of the spells projected by this CAD. Usually just goes back
	 * to ICADColorizer.getColor().
	 */
	int getSpellColor(ItemStack stack);

	/**
	 * Performs crafting around the player using this CAD.
	 *
	 * @param cad    Stack casting the spell
	 * @param entity Player casting the spell
	 * @param trick  The trick used to craft
	 * @return Whether crafting was successful
	 */
	boolean craft(ItemStack cad, Player entity, PieceCraftingTrick trick);

}
