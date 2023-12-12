/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import vazkii.psi.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class PsiSoundHandler {

	public static final SoundEvent bulletCreate = SoundEvent.createVariableRangeEvent(new ResourceLocation(LibMisc.MOD_ID, "bullet_create"));
	public static final SoundEvent cadCreate = SoundEvent.createVariableRangeEvent(new ResourceLocation(LibMisc.MOD_ID, "cad_create"));
	public static final SoundEvent cadShoot = SoundEvent.createVariableRangeEvent(new ResourceLocation(LibMisc.MOD_ID, "cad_shoot"));
	public static final SoundEvent compileError = SoundEvent.createVariableRangeEvent(new ResourceLocation(LibMisc.MOD_ID, "compile_error"));
	public static final SoundEvent levelUp = SoundEvent.createVariableRangeEvent(new ResourceLocation(LibMisc.MOD_ID, "level_up"));
	public static final SoundEvent loopcast = SoundEvent.createVariableRangeEvent(new ResourceLocation(LibMisc.MOD_ID, "loopcast"));
	public static final SoundEvent book = SoundEvent.createVariableRangeEvent(new ResourceLocation(LibMisc.MOD_ID, "book"));
	public static final SoundEvent bookFlip = SoundEvent.createVariableRangeEvent(new ResourceLocation(LibMisc.MOD_ID, "book_flip"));
	public static final SoundEvent bookOpen = SoundEvent.createVariableRangeEvent(new ResourceLocation(LibMisc.MOD_ID, "book_open"));

	@SubscribeEvent
	public static void registerSounds(RegisterEvent evt) {
		evt.register(ForgeRegistries.Keys.SOUND_EVENTS, helper -> {
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
