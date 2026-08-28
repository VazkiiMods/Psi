/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface PsiRegistryService {
	<T> Registry<T> create(ResourceKey<Registry<T>> key, boolean synced);

	<T, I extends T> RegistryEntry<I> register(Registry<T> registry, ResourceLocation id, Supplier<I> factory);

}
