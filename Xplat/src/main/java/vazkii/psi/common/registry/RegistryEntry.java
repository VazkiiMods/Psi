/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.registry;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.function.Supplier;

public final class RegistryEntry<T> implements Supplier<T> {

	private final ResourceLocation id;
	private final Supplier<? extends T> value;
	private final Supplier<? extends Holder<?>> holder;

	public RegistryEntry(ResourceLocation id, Supplier<? extends T> value, Supplier<? extends Holder<?>> holder) {
		this.id = Objects.requireNonNull(id);
		this.value = Objects.requireNonNull(value);
		this.holder = Objects.requireNonNull(holder);
	}

	public ResourceLocation id() {
		return id;
	}

	@Override
	public T get() {
		return value.get();
	}

	@SuppressWarnings("unchecked")
	public Holder<T> holder() {
		return (Holder<T>) holder.get();
	}

}
