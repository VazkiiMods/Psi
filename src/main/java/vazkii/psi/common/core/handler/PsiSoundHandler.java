/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.Psi;

@EventBusSubscriber(modid = PsiAPI.MOD_ID)
public final class PsiSoundHandler {

	public static final SoundEvent bulletCreate = SoundEvent.createVariableRangeEvent(Psi.location("bullet_create"));
	public static final SoundEvent cadCreate = SoundEvent.createVariableRangeEvent(Psi.location("cad_create"));
	public static final SoundEvent cadShoot = SoundEvent.createVariableRangeEvent(Psi.location("cad_shoot"));
	public static final SoundEvent compileError = SoundEvent.createVariableRangeEvent(Psi.location("compile_error"));
	public static final SoundEvent levelUp = SoundEvent.createVariableRangeEvent(Psi.location("level_up"));
	public static final SoundEvent loopcast = SoundEvent.createVariableRangeEvent(Psi.location("loopcast"));
	public static final SoundEvent book = SoundEvent.createVariableRangeEvent(Psi.location("book"));
	public static final SoundEvent bookFlip = SoundEvent.createVariableRangeEvent(Psi.location("book_flip"));
	public static final SoundEvent bookOpen = SoundEvent.createVariableRangeEvent(Psi.location("book_open"));

	@SubscribeEvent
	public static void registerSounds(RegisterEvent evt) {
		evt.register(Registries.SOUND_EVENT, helper -> {
			helper.register(bulletCreate.getLocation(), bulletCreate);
			helper.register(cadCreate.getLocation(), cadCreate);
			helper.register(cadShoot.getLocation(), cadShoot);
			helper.register(compileError.getLocation(), compileError);
			helper.register(levelUp.getLocation(), levelUp);
			helper.register(loopcast.getLocation(), loopcast);
			helper.register(book.getLocation(), book);
			helper.register(bookFlip.getLocation(), bookFlip);
			helper.register(bookOpen.getLocation(), bookOpen);
		});
	}
}
