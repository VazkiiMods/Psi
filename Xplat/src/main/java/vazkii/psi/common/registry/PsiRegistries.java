/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.registry;

import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import vazkii.psi.common.platform.PsiServices;

import java.util.function.Supplier;

public final class PsiRegistries {

	private static final PsiRegistryService SERVICE = PsiServices.load(PsiRegistryService.class);

	private PsiRegistries() {}

	public static <T> Registry<T> create(ResourceKey<Registry<T>> key, boolean synced) {
		return SERVICE.create(key, synced);
	}

	public static <T, I extends T> RegistryEntry<I> register(
			Registry<T> registry, ResourceLocation id, Supplier<I> factory) {
		return SERVICE.register(registry, id, factory);
	}

	public static <T> void registerSyncedDatapackRegistry(ResourceKey<Registry<T>> key, Codec<T> codec) {
		SERVICE.registerSyncedDatapackRegistry(key, codec);
	}

}
