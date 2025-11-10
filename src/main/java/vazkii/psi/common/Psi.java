/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.material.PsimetalArmorMaterial;
import vazkii.psi.client.core.proxy.ClientProxy;
import vazkii.psi.client.fx.ModParticles;
import vazkii.psi.common.attribute.base.ModAttributes;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.ContributorSpellCircleHandler;
import vazkii.psi.common.core.handler.InternalMethodHandler;
import vazkii.psi.common.core.proxy.IProxy;
import vazkii.psi.common.core.proxy.ServerProxy;
import vazkii.psi.common.crafting.ModCraftingRecipes;
import vazkii.psi.common.item.base.ModDataComponents;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.component.DefaultStats;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.spell.base.ModSpellPieces;

@Mod(PsiAPI.MOD_ID)
public class Psi {

	public static final Logger logger = LogManager.getLogger(PsiAPI.MOD_ID);

	public static boolean magical;
	public static IProxy proxy;

	public Psi(IEventBus bus, Dist dist, ModContainer container) {
		ModAttributes.ATTRIBUTES.register(bus);
		ModDataComponents.DATA_COMPONENT_TYPES.register(bus);
		PsimetalArmorMaterial.ARMOR_MATERIALS.register(bus);
		ModCraftingRecipes.RECIPE_TYPES.register(bus);
		ModCraftingRecipes.RECIPE_SERIALIZERS.register(bus);
		ModCraftingRecipes.CONDITION_CODECS.register(bus);
		ModParticles.PARTICLE_TYPES.register(bus);
		ModBlocks.BLOCKS.register(bus);
		ModBlocks.BLOCK_TYPES.register(bus);
		ModBlocks.MENU.register(bus);
		ModItems.ITEMS.register(bus);
		ModSpellPieces.SPELL_PIECES.register(bus);
		ModSpellPieces.ADVANCEMENT_GROUPS.register(bus);
		bus.addListener(this::registerRegistries);
		bus.addListener(this::commonSetup);
		bus.addListener(MessageRegister::onRegisterPayloadHandler);
		container.registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
		container.registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);
		proxy = dist.isClient() ? new ClientProxy() : new ServerProxy();
		proxy.registerHandlers(bus);
	}

	public static ResourceLocation location(String path) {
		return ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, path);
	}

	private void registerRegistries(NewRegistryEvent event) {
		event.register(PsiAPI.SPELL_PIECE_REGISTRY);
		event.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		magical = ModList.get().isLoaded("magipsi");
		PsiAPI.internalHandler = new InternalMethodHandler();

		ContributorSpellCircleHandler.firstStart();
		DefaultStats.registerStats();
	}

}
