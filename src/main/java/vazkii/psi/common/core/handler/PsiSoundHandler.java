/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [14/03/2016, 16:30:00 (GMT)]
 */
package vazkii.psi.common.core.handler;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class PsiSoundHandler {

    public static SoundEvent bulletCreate;
    public static SoundEvent cadCreate;
    public static SoundEvent cadShoot;
    public static SoundEvent compileError;
    public static SoundEvent levelUp;
    public static SoundEvent loopcast;


    public static SoundEvent register(String name) {
        ResourceLocation loc = new ResourceLocation(LibResources.PREFIX_MOD + name);

        return new SoundEvent(loc).setRegistryName(loc);
    }


    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> evt) {
        IForgeRegistry<SoundEvent> r = evt.getRegistry();
        bulletCreate = register("bullet_create");
        cadCreate = register("cad_create");
        cadShoot = register("cad_shoot");
        compileError = register("compile_error");
        levelUp = register("level_up");
        loopcast = register("loopcast");
        r.register(bulletCreate);
        r.register(cadCreate);
        r.register(cadShoot);
        r.register(compileError);
        r.register(levelUp);
        r.register(loopcast);
    }
}
