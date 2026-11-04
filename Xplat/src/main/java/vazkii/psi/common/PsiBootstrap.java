/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common;

import vazkii.psi.common.platform.PsiPlatform;
import vazkii.psi.common.platform.PsiServices;
import vazkii.psi.common.registry.PsiContent;

import java.util.concurrent.atomic.AtomicBoolean;

public final class PsiBootstrap {

	private static final AtomicBoolean INITIALIZED = new AtomicBoolean();

	private PsiBootstrap() {}

	public static void initialize() {
		if(INITIALIZED.compareAndSet(false, true)) {
			PsiServices.load(PsiPlatform.class).initialize();
			PsiContent.register();
		}
	}

}
