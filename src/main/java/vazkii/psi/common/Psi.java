/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [08/01/2016, 21:19:53 (GMT)]
 */
package vazkii.psi.common;

import net.minecraftforge.fml.CrashReportExtender;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.client.core.proxy.ClientProxy;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.command.CommandPsiLearn;
import vazkii.psi.common.command.CommandPsiUnlearn;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.CrashReportHandler;
import vazkii.psi.common.core.handler.InternalMethodHandler;
import vazkii.psi.common.core.handler.capability.CapabilityHandler;
import vazkii.psi.common.core.proxy.IProxy;
import vazkii.psi.common.core.proxy.ServerProxy;
import vazkii.psi.common.crafting.ModCraftingRecipes;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.component.DefaultStats;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.spell.base.ModSpellPieces;

@Mod(LibMisc.MOD_ID)
public class Psi {

	public static final Logger logger = LogManager.getLogger(LibMisc.MOD_ID);

	public static Psi instance;
	public static boolean magical;
	public static IProxy proxy;

	public Psi() {
		instance = this;
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverStartingEvent);
		proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
		proxy.registerHandlers();
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		magical = ModList.get().isLoaded("magipsi");
		PsiAPI.internalHandler = new InternalMethodHandler();

		CrashReportExtender.registerCrashCallable(new CrashReportHandler());

		ConfigHandler.init(event.getSuggestedConfigurationFile());

		new ModItems();
		new ModBlocks();
		DefaultStats.registerStats();
		ModSpellPieces.init();
		ModCraftingRecipes.init();

		CapabilityHandler.register();
		MessageRegister.init();
	}

	public void serverStartingEvent(FMLServerStartingEvent event) {
		CommandPsiLearn.register(event.getCommandDispatcher());
		CommandPsiUnlearn.register(event.getCommandDispatcher());
	}

}
