/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.material.PsimetalArmorMaterial;
import vazkii.psi.client.core.proxy.ClientProxy;
import vazkii.psi.client.fx.ModParticles;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.ContributorSpellCircleHandler;
import vazkii.psi.common.core.handler.InternalMethodHandler;
import vazkii.psi.common.core.proxy.IProxy;
import vazkii.psi.common.core.proxy.ServerProxy;
import vazkii.psi.common.crafting.ModCraftingRecipes;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.component.DefaultStats;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.network.MessageRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Mod(LibMisc.MOD_ID)
public class Psi {

	public static final Logger logger = LogManager.getLogger(LibMisc.MOD_ID);

	public static Psi instance;
	public static boolean magical;
	public static IProxy proxy;
	public static List<SoundEvent> noteblockSoundEvents = new ArrayList<>();

	public Psi(IEventBus bus, Dist dist, ModContainer container) {
		instance = this;
		ModItems.DATA_COMPONENT_TYPES.register(bus);
		PsimetalArmorMaterial.ARMOR_MATERIALS.register(bus);
		ModCraftingRecipes.CONDITION_CODECS.register(bus);
		ModParticles.PARTICLE_TYPES.register(bus);
		bus.addListener(this::commonSetup);
		bus.addListener(MessageRegister::onRegisterPayloadHandler);
		bus.addListener(this::loadComplete);
		container.registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
		container.registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);
		proxy = dist.isClient() ? new ClientProxy() : new ServerProxy();
		proxy.registerHandlers(bus);
	}

	public static ResourceLocation location(String path) {
		return ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, path);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		magical = ModList.get().isLoaded("magipsi");
		PsiAPI.internalHandler = new InternalMethodHandler();

		//CrashReportExtender.registerCrashCallable(new CrashReportHandler());

		ContributorSpellCircleHandler.firstStart();
		DefaultStats.registerStats();
	}

	private void loadComplete(FMLLoadCompleteEvent event) {
		BuiltInRegistries.SOUND_EVENT.forEach(el -> {
			if(BuiltInRegistries.SOUND_EVENT.getKey(el).getPath().toLowerCase(Locale.ROOT).startsWith("block.note_block")) {
				noteblockSoundEvents.add(el);
			}
		});
	}

}
