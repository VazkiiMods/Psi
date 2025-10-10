/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;

@EventBusSubscriber(modid = LibMisc.MOD_ID)
public class PsiCreativeTab {

	public static final ResourceKey<CreativeModeTab> PSI_CREATIVE_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Psi.location("creative_tab"));

	@SubscribeEvent
	public static void register(RegisterEvent evt) {
		evt.register(Registries.CREATIVE_MODE_TAB, creativeModeTabRegisterHelper -> {
			CreativeModeTab psiCreativeTab = CreativeModeTab.builder().title(Component.translatable("itemGroup.psi"))
					.icon(() -> new ItemStack(ModItems.cadAssemblyIron.get()))
					.displayItems((parameters, output) -> {
						output.accept(ModItems.psidust.get());
						output.accept(ModItems.psimetal.get());
						output.accept(ModItems.psigem.get());
						output.accept(ModItems.ebonyPsimetal.get());
						output.accept(ModItems.ivoryPsimetal.get());
						output.accept(ModItems.ebonySubstance.get());
						output.accept(ModItems.ivorySubstance.get());

						output.accept(ModItems.cadAssemblyIron.get());
						output.accept(ModItems.cadAssemblyGold.get());
						output.accept(ModItems.cadAssemblyPsimetal.get());
						output.accept(ModItems.cadAssemblyIvory.get());
						output.accept(ModItems.cadAssemblyEbony.get());
						output.accept(ModItems.cadAssemblyCreative.get());

						output.accept(ModItems.cadCoreBasic.get());
						output.accept(ModItems.cadCoreOverclocked.get());
						output.accept(ModItems.cadCoreConductive.get());
						output.accept(ModItems.cadCoreHyperClocked.get());
						output.accept(ModItems.cadCoreRadiative.get());

						output.accept(ModItems.cadSocketBasic.get());
						output.accept(ModItems.cadSocketSignaling.get());
						output.accept(ModItems.cadSocketLarge.get());
						output.accept(ModItems.cadSocketTransmissive.get());
						output.accept(ModItems.cadSocketHuge.get());

						output.accept(ModItems.cadBatteryBasic.get());
						output.accept(ModItems.cadBatteryExtended.get());
						output.accept(ModItems.cadBatteryUltradense.get());

						output.accept(ModItems.cadColorizerWhite.get());
						output.accept(ModItems.cadColorizerOrange.get());
						output.accept(ModItems.cadColorizerMagenta.get());
						output.accept(ModItems.cadColorizerLightBlue.get());
						output.accept(ModItems.cadColorizerYellow.get());
						output.accept(ModItems.cadColorizerLime.get());
						output.accept(ModItems.cadColorizerPink.get());
						output.accept(ModItems.cadColorizerGray.get());
						output.accept(ModItems.cadColorizerLightGray.get());
						output.accept(ModItems.cadColorizerCyan.get());
						output.accept(ModItems.cadColorizerPurple.get());
						output.accept(ModItems.cadColorizerBlue.get());
						output.accept(ModItems.cadColorizerBrown.get());
						output.accept(ModItems.cadColorizerGreen.get());
						output.accept(ModItems.cadColorizerRed.get());
						output.accept(ModItems.cadColorizerBlack.get());
						output.accept(ModItems.cadColorizerRainbow.get());
						output.accept(ModItems.cadColorizerPsi.get());
						output.accept(ModItems.cadColorizerEmpty.get());

						output.accept(ModItems.flashRing.get());

						output.accept(ModItems.spellBullet.get());
						output.accept(ModItems.projectileSpellBullet.get());
						output.accept(ModItems.loopSpellBullet.get());
						output.accept(ModItems.circleSpellBullet.get());
						output.accept(ModItems.grenadeSpellBullet.get());
						output.accept(ModItems.chargeSpellBullet.get());
						output.accept(ModItems.mineSpellBullet.get());

						output.accept(ModItems.spellDrive.get());
						output.accept(ModItems.detonator.get());
						output.accept(ModItems.exosuitController.get());

						output.accept(ModItems.exosuitSensorLight.get());
						output.accept(ModItems.exosuitSensorHeat.get());
						output.accept(ModItems.exosuitSensorStress.get());
						output.accept(ModItems.exosuitSensorWater.get());
						output.accept(ModItems.exosuitSensorTrigger.get());

						output.acceptAll(ItemCAD.getCreativeTabItems());

						output.accept(ModItems.vectorRuler.get());
						output.accept(ModItems.psimetalShovel.get());
						output.accept(ModItems.psimetalPickaxe.get());
						output.accept(ModItems.psimetalAxe.get());
						output.accept(ModItems.psimetalSword.get());
						output.accept(ModItems.psimetalExosuitHelmet.get());
						output.accept(ModItems.psimetalExosuitChestplate.get());
						output.accept(ModItems.psimetalExosuitLeggings.get());
						output.accept(ModItems.psimetalExosuitBoots.get());

						output.accept(ModBlocks.cadAssembler.get());
						output.accept(ModBlocks.programmer.get());
						output.accept(ModBlocks.psidustBlock.get());
						output.accept(ModBlocks.psimetalBlock.get());
						output.accept(ModBlocks.psigemBlock.get());
						output.accept(ModBlocks.psimetalPlateBlack.get());
						output.accept(ModBlocks.psimetalPlateBlackLight.get());
						output.accept(ModBlocks.psimetalPlateWhite.get());
						output.accept(ModBlocks.psimetalPlateWhiteLight.get());
						output.accept(ModBlocks.psimetalEbony.get());
						output.accept(ModBlocks.psimetalIvory.get());

						output.accept(ModBlocks.psimetalIvory.get());
					})
					.hideTitle()
					.backgroundTexture(ResourceLocation.withDefaultNamespace("textures/gui/container/creative_inventory/tab_" + LibResources.GUI_CREATIVE))
					.withSearchBar()
					.build();
			creativeModeTabRegisterHelper.register(PSI_CREATIVE_TAB, psiCreativeTab);
		});
	}

}
