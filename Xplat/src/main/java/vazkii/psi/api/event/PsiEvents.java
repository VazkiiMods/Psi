/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class PsiEvents {

	private static final Map<Class<? extends PsiEvent>, CopyOnWriteArrayList<Consumer<PsiEvent>>> LISTENERS =
			new ConcurrentHashMap<>();

	private PsiEvents() {}

	public static <T extends PsiEvent> Runnable register(Class<T> eventType, Consumer<? super T> listener) {
		CopyOnWriteArrayList<Consumer<PsiEvent>> listeners =
				LISTENERS.computeIfAbsent(eventType, ignored -> new CopyOnWriteArrayList<>());
		Consumer<PsiEvent> checkedListener = event -> listener.accept(eventType.cast(event));
		listeners.add(checkedListener);
		return () -> listeners.remove(checkedListener);
	}

	public static <T extends PsiEvent> T post(T event) {
		for(Consumer<PsiEvent> listener : LISTENERS.getOrDefault(event.getClass(), new CopyOnWriteArrayList<>())) {
			listener.accept(event);
		}
		return event;
	}

}
