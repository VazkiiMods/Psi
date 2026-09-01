/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.CADStatEvent;
import vazkii.psi.api.event.PsiEvents;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.common.core.handler.InternalMethodHandler;
import vazkii.psi.common.core.handler.PsiArmorHandler;
import vazkii.psi.common.item.component.DefaultStats;

import java.util.concurrent.atomic.AtomicBoolean;

public final class PsiCommonRuntime {
	private static final AtomicBoolean PREPARED = new AtomicBoolean();
	private static final AtomicBoolean INITIALIZED = new AtomicBoolean();

	private PsiCommonRuntime() {}

	public static void prepare() {
		if(PREPARED.compareAndSet(false, true)) {
			PsiAPI.internalHandler = new InternalMethodHandler();
		}
	}

	public static void initialize() {
		if(!INITIALIZED.compareAndSet(false, true)) {
			throw new IllegalStateException("Psi common runtime was initialized more than once");
		}
		prepare();
		PsiEvents.register(CADStatEvent.class, DefaultStats::modifyCreativeAssemblyStats);
		PsiEvents.register(PsiArmorEvent.class, PsiArmorHandler::onEvent);
	}
}
