/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.fabric.platform;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import vazkii.psi.common.registry.PsiRegistryService;
import vazkii.psi.common.registry.RegistryEntry;

import java.util.function.Supplier;

public final class FabricPsiRegistryService implements PsiRegistryService {
	@Override
	public <T> Registry<T> create(ResourceKey<Registry<T>> key, boolean synced) {
		FabricRegistryBuilder<T, ?> builder = FabricRegistryBuilder.createSimple(key);
		if(synced) {
			builder.attribute(RegistryAttribute.SYNCED);
		}
		return builder.buildAndRegister();
	}

	@Override
	public <T, I extends T> RegistryEntry<I> register(
			Registry<T> registry, ResourceLocation id, Supplier<I> factory) {
		I value = Registry.register(registry, id, factory.get());
		return new RegistryEntry<>(id, () -> value, () -> registry.wrapAsHolder(value));
	}

	@Override
	public <T> void registerSyncedDatapackRegistry(ResourceKey<Registry<T>> key, Codec<T> codec) {
		DynamicRegistries.registerSynced(key, codec);
	}

}
