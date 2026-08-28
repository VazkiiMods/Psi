/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import vazkii.psi.api.spell.SpellContext;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public final class DelayedSpellHandler {
	private static final Set<SpellContext> CONTEXTS = new LinkedHashSet<>();

	private DelayedSpellHandler() {}

	public static void delay(SpellContext context) {
		CONTEXTS.add(context);
	}

	public static void tick() {
		for(SpellContext context : new ArrayList<>(CONTEXTS)) {
			context.delay--;
			if(context.delay <= 0) {
				CONTEXTS.remove(context);
				context.delay = 0;
				context.cspell.safeExecute(context);
			}
		}
	}
}
