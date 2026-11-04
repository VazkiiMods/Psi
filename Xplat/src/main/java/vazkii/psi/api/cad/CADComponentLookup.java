/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.PsiAPI;

import java.util.Optional;

/**
 * Resolves what an item stack is as a CAD component. The {@code psi:cad_component}
 * datapack registry is consulted first; items without an entry fall back to the
 * {@link ICADComponent}, {@link ICADAssembly} and {@link ICADColorizer} interfaces.
 * Passing {@code null} registries skips the registry and only uses the interfaces.
 */
public final class CADComponentLookup {

	public static final ResourceKey<Registry<CADComponentDefinition>> REGISTRY = ResourceKey.createRegistryKey(PsiAPI.location("cad_component"));
	public static final ResourceLocation DEFAULT_CAD_MODEL = PsiAPI.location("item/cad_iron");

	private CADComponentLookup() {}

	public static Optional<Holder.Reference<CADComponentDefinition>> holder(@Nullable HolderLookup.Provider registries, ItemStack stack) {
		if(registries == null || stack.isEmpty()) {
			return Optional.empty();
		}
		ResourceKey<CADComponentDefinition> key = ResourceKey.create(REGISTRY, BuiltInRegistries.ITEM.getKey(stack.getItem()));
		return registries.lookup(REGISTRY).flatMap(lookup -> lookup.get(key));
	}

	public static Optional<CADComponentDefinition> definition(@Nullable HolderLookup.Provider registries, ItemStack stack) {
		return holder(registries, stack).map(Holder::value);
	}

	public static Optional<EnumCADComponent> componentType(@Nullable HolderLookup.Provider registries, ItemStack stack) {
		Optional<CADComponentDefinition> definition = definition(registries, stack);
		if(definition.isPresent()) {
			return definition.map(CADComponentDefinition::type);
		}
		if(stack.getItem() instanceof ICADComponent component) {
			return Optional.of(component.getComponentType(stack));
		}
		return Optional.empty();
	}

	public static boolean isComponent(@Nullable HolderLookup.Provider registries, ItemStack stack, EnumCADComponent type) {
		return componentType(registries, stack).filter(type::equals).isPresent();
	}

	public static int statValue(@Nullable HolderLookup.Provider registries, ItemStack stack, EnumCADStat stat) {
		return statValue(definition(registries, stack), stack, stat);
	}

	/**
	 * Stat of a component already resolved into a CAD slot; {@code stack} is the slot's
	 * item, used for the {@link ICADComponent} fallback.
	 */
	public static int statValue(CADComponentSlot slot, ItemStack stack, EnumCADStat stat) {
		return statValue(slot.definition(), stack, stat);
	}

	private static int statValue(Optional<CADComponentDefinition> definition, ItemStack stack, EnumCADStat stat) {
		if(definition.isPresent()) {
			return definition.get().stat(stat);
		}
		if(stack.getItem() instanceof ICADComponent component) {
			return component.getCADStatValue(stack, stat);
		}
		return 0;
	}

	public static int color(@Nullable HolderLookup.Provider registries, ItemStack stack) {
		return color(definition(registries, stack), stack);
	}

	public static int color(CADComponentSlot slot, ItemStack stack) {
		return color(slot.definition(), stack);
	}

	private static int color(Optional<CADComponentDefinition> definition, ItemStack stack) {
		Optional<Integer> color = definition.flatMap(CADComponentDefinition::color);
		if(color.isPresent()) {
			return color.get();
		}
		if(stack.getItem() instanceof ICADColorizer colorizer) {
			return colorizer.getColor(stack);
		}
		return ICADColorizer.DEFAULT_SPELL_COLOR;
	}

	public static ResourceLocation cadModel(@Nullable HolderLookup.Provider registries, ItemStack assembly, ItemStack cad) {
		return cadModel(definition(registries, assembly), assembly, cad);
	}

	public static ResourceLocation cadModel(CADComponentSlot slot, ItemStack assembly, ItemStack cad) {
		return cadModel(slot.definition(), assembly, cad);
	}

	private static ResourceLocation cadModel(Optional<CADComponentDefinition> definition, ItemStack assembly, ItemStack cad) {
		Optional<ResourceLocation> model = definition.flatMap(CADComponentDefinition::model);
		if(model.isPresent()) {
			return model.get();
		}
		if(assembly.getItem() instanceof ICADAssembly cadAssembly) {
			return cadAssembly.getCADModel(assembly, cad);
		}
		return DEFAULT_CAD_MODEL;
	}

}
