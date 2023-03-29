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

	public static SoundEvent bulletCreate;
	public static SoundEvent cadCreate;
	public static SoundEvent cadShoot;
	public static SoundEvent compileError;
	public static SoundEvent levelUp;
	public static SoundEvent loopcast;
	public static SoundEvent book;
	public static SoundEvent bookOpen;
	public static SoundEvent bookFlip;

	@SubscribeEvent
	public static void registerSounds(RegisterEvent evt) {
		evt.register(ForgeRegistries.Keys.SOUND_EVENTS, helper -> {
			bulletCreate = new SoundEvent(new ResourceLocation(LibMisc.MOD_ID, "bullet_create"));
			cadCreate = new SoundEvent(new ResourceLocation(LibMisc.MOD_ID, "cad_create"));
			cadShoot = new SoundEvent(new ResourceLocation(LibMisc.MOD_ID, "cad_shoot"));
			compileError = new SoundEvent(new ResourceLocation(LibMisc.MOD_ID, "compile_error"));
			levelUp = new SoundEvent(new ResourceLocation(LibMisc.MOD_ID, "level_up"));
			loopcast = new SoundEvent(new ResourceLocation(LibMisc.MOD_ID, "loopcast"));
			book = new SoundEvent(new ResourceLocation(LibMisc.MOD_ID, "book"));
			bookFlip = new SoundEvent(new ResourceLocation(LibMisc.MOD_ID, "book_flip"));
			bookOpen = new SoundEvent(new ResourceLocation(LibMisc.MOD_ID, "book_open"));

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
