/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.capability;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.common.block.base.ModCADAssemblerBlock;
import vazkii.psi.common.item.base.ModDataComponents;
import vazkii.psi.common.item.base.ModItems;

@EventBusSubscriber(modid = PsiAPI.MOD_ID)
public final class NeoForgeCapabilityHandler {
	private NeoForgeCapabilityHandler() {}

	@SubscribeEvent
	private static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(
				Capabilities.ItemHandler.BLOCK,
				ModCADAssemblerBlock.TYPE.get(),
				(assembler, side) -> new InvWrapper(assembler));

		event.registerItem(
				Capabilities.ItemHandler.ITEM,
				(itemStack, context) -> new ComponentItemHandler(itemStack, ModDataComponents.BULLETS.get(), ISocketable.MAX_ASSEMBLER_SLOTS),
				ModItems.cad.get());
		event.registerItem(
				Capabilities.ItemHandler.ITEM,
				(itemStack, context) -> new ComponentItemHandler(itemStack, ModDataComponents.BULLETS.get(), 3),
				ModItems.psimetalShovel.get(),
				ModItems.psimetalPickaxe.get(),
				ModItems.psimetalAxe.get(),
				ModItems.psimetalSword.get());
		event.registerItem(
				Capabilities.ItemHandler.ITEM,
				(itemStack, context) -> new ComponentItemHandler(itemStack, ModDataComponents.BULLETS.get(), 3),
				ModItems.psimetalExosuitHelmet.get(),
				ModItems.psimetalExosuitChestplate.get(),
				ModItems.psimetalExosuitLeggings.get(),
				ModItems.psimetalExosuitBoots.get());

	}

}
