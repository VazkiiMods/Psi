/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.CrashReportExtender;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.client.core.proxy.ClientProxy;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.CrashReportHandler;
import vazkii.psi.common.core.handler.InternalMethodHandler;
import vazkii.psi.common.core.handler.capability.CapabilityHandler;
import vazkii.psi.common.core.proxy.IProxy;
import vazkii.psi.common.core.proxy.ServerProxy;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.component.DefaultStats;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.spell.base.ModSpellPieces;

import java.util.ArrayList;
import java.util.List;

@Mod(LibMisc.MOD_ID)
public class Psi {

	public static final Logger logger = LogManager.getLogger(LibMisc.MOD_ID);

	public static Psi instance;
	public static boolean magical;
	public static IProxy proxy;
	public static List<SoundEvent> noteblockSoundEvents = new ArrayList<>();

	public Psi() {
		instance = this;
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
		proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
		proxy.registerHandlers();
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		magical = ModList.get().isLoaded("magipsi");
		PsiAPI.internalHandler = new InternalMethodHandler();

		CrashReportExtender.registerCrashCallable(new CrashReportHandler());

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);

		DefaultStats.registerStats();
		ModSpellPieces.init();
		MessageRegister.init();
	}

	private void loadComplete(FMLLoadCompleteEvent event) {
		ForgeRegistries.SOUND_EVENTS.forEach(el -> {
			if (el.getRegistryName().getPath().toLowerCase().startsWith("block.note_block")) {
				noteblockSoundEvents.add(el);
			}
		});
	}

	public static ResourceLocation location(String path) {
		return new ResourceLocation(LibMisc.MOD_ID, path);
	}

}
