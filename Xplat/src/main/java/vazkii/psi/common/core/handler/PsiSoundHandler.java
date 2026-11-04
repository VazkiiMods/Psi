/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

import vazkii.psi.common.PsiMod;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;

public final class PsiSoundHandler {

	public static final RegistryEntry<SoundEvent> bulletCreate = sound("bullet_create");
	public static final RegistryEntry<SoundEvent> cadCreate = sound("cad_create");
	public static final RegistryEntry<SoundEvent> cadShoot = sound("cad_shoot");
	public static final RegistryEntry<SoundEvent> compileError = sound("compile_error");
	public static final RegistryEntry<SoundEvent> levelUp = sound("level_up");
	public static final RegistryEntry<SoundEvent> loopcast = sound("loopcast");
	public static final RegistryEntry<SoundEvent> book = sound("book");
	public static final RegistryEntry<SoundEvent> bookFlip = sound("book_flip");
	public static final RegistryEntry<SoundEvent> bookOpen = sound("book_open");

	private PsiSoundHandler() {}

	public static void register() {}

	private static RegistryEntry<SoundEvent> sound(String name) {
		var id = PsiMod.location(name);
		return PsiRegistries.register(BuiltInRegistries.SOUND_EVENT, id,
				() -> SoundEvent.createVariableRangeEvent(id));
	}

}
