/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network;

import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;
import java.util.function.Supplier;

public final class LazyStreamCodec {

	private LazyStreamCodec() {}

	public static <B, V> StreamCodec<B, V> of(Supplier<StreamCodec<B, V>> factory) {
		return new StreamCodec<>() {
			private StreamCodec<B, V> delegate;

			@Override
			public V decode(B buffer) {
				return delegate().decode(buffer);
			}

			@Override
			public void encode(B buffer, V value) {
				delegate().encode(buffer, value);
			}

			private StreamCodec<B, V> delegate() {
				if(delegate == null) {
					delegate = Objects.requireNonNull(factory.get());
				}
				return delegate;
			}
		};
	}

}
