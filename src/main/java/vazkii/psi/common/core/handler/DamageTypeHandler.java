/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = LibMisc.MOD_ID)
public class DamageTypeHandler {
	public static final DamageType psiDamageType = new DamageType("psi_overload", 0f);

	@SubscribeEvent
	public static void registerSerializers(RegisterEvent event) {
		event.register(Registries.DAMAGE_TYPE, helper -> {
			helper.register(LibResources.PSI_DAMAGE_TYPE, psiDamageType);
		});
	}
}
