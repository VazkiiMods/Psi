/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.platform;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public final class PsiServices {
	private static final Map<Class<?>, Object> SERVICES = new ConcurrentHashMap<>();

	private PsiServices() {}

	public static <T> T load(Class<T> serviceType) {
		return serviceType.cast(SERVICES.computeIfAbsent(serviceType, PsiServices::loadSingle));
	}

	private static Object loadSingle(Class<?> serviceType) {
		List<? extends ServiceLoader.Provider<?>> providers = ServiceLoader.load(serviceType).stream().toList();
		if(providers.size() != 1) {
			throw new IllegalStateException("Expected exactly one " + serviceType.getName()
					+ " provider, found " + providers.size());
		}
		return providers.getFirst().get();
	}

}
