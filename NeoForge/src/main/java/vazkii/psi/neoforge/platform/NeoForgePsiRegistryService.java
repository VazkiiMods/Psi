/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.platform;

import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import vazkii.psi.common.registry.PsiRegistryService;
import vazkii.psi.common.registry.RegistryEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class NeoForgePsiRegistryService implements PsiRegistryService {

	private final Map<RegistryNamespace, DeferredRegister<?>> registers = new HashMap<>();

	@Override
	public <T> Registry<T> create(ResourceKey<Registry<T>> key, boolean synced) {
		Registry<T> registry = new RegistryBuilder<>(key).sync(synced).create();
		NeoForgePsiPlatform.modBus().addListener((NewRegistryEvent event) -> event.register(registry));
		return registry;
	}

	@Override
	public <T, I extends T> RegistryEntry<I> register(
			Registry<T> registry, ResourceLocation id, Supplier<I> factory) {
		DeferredRegister<T> register = registerFor(registry, id.getNamespace());
		var holder = register.register(id.getPath(), factory);
		return new RegistryEntry<>(id, holder, () -> holder);
	}

	@Override
	public <T> void registerSyncedDatapackRegistry(ResourceKey<Registry<T>> key, Codec<T> codec) {
		NeoForgePsiPlatform.modBus().addListener((DataPackRegistryEvent.NewRegistry event) -> event.dataPackRegistry(key, codec, codec));
	}

	@SuppressWarnings("unchecked")
	private <T> DeferredRegister<T> registerFor(Registry<T> registry, String namespace) {
		RegistryNamespace key = new RegistryNamespace(registry.key().location(), namespace);
		return (DeferredRegister<T>) registers.computeIfAbsent(key, ignored -> {
			DeferredRegister<T> register = DeferredRegister.create(registry, namespace);
			register.register(NeoForgePsiPlatform.modBus());
			return register;
		});

	}

	private record RegistryNamespace(ResourceLocation registry, String namespace) {
	}

}
