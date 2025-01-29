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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PsiCreativeTab {

	public static final ResourceKey<CreativeModeTab> PSI_CREATIVE_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(LibMisc.MOD_ID, "creative_tab"));

	@SubscribeEvent
	public static void register(RegisterEvent evt) {
		evt.register(Registries.CREATIVE_MODE_TAB, creativeModeTabRegisterHelper -> {
			CreativeModeTab psiCreativeTab = CreativeModeTab.builder().title(Component.translatable("itemGroup.psi"))
					.icon(() -> new ItemStack(ModItems.cadAssemblyIron))
					.displayItems((parameters, output) -> {
						output.accept(ModItems.psidust);
						output.accept(ModItems.psimetal);
						output.accept(ModItems.psigem);
						output.accept(ModItems.ebonyPsimetal);
						output.accept(ModItems.ivoryPsimetal);
						output.accept(ModItems.ebonySubstance);
						output.accept(ModItems.ivorySubstance);

						output.accept(ModItems.cadAssemblyIron);
						output.accept(ModItems.cadAssemblyGold);
						output.accept(ModItems.cadAssemblyPsimetal);
						output.accept(ModItems.cadAssemblyIvory);
						output.accept(ModItems.cadAssemblyEbony);
						output.accept(ModItems.cadAssemblyCreative);

						output.accept(ModItems.cadCoreBasic);
						output.accept(ModItems.cadCoreOverclocked);
						output.accept(ModItems.cadCoreConductive);
						output.accept(ModItems.cadCoreHyperClocked);
						output.accept(ModItems.cadCoreRadiative);

						output.accept(ModItems.cadSocketBasic);
						output.accept(ModItems.cadSocketSignaling);
						output.accept(ModItems.cadSocketLarge);
						output.accept(ModItems.cadSocketTransmissive);
						output.accept(ModItems.cadSocketHuge);

						output.accept(ModItems.cadBatteryBasic);
						output.accept(ModItems.cadBatteryExtended);
						output.accept(ModItems.cadBatteryUltradense);

						output.accept(ModItems.cadColorizerWhite);
						output.accept(ModItems.cadColorizerOrange);
						output.accept(ModItems.cadColorizerMagenta);
						output.accept(ModItems.cadColorizerLightBlue);
						output.accept(ModItems.cadColorizerYellow);
						output.accept(ModItems.cadColorizerLime);
						output.accept(ModItems.cadColorizerPink);
						output.accept(ModItems.cadColorizerGray);
						output.accept(ModItems.cadColorizerLightGray);
						output.accept(ModItems.cadColorizerCyan);
						output.accept(ModItems.cadColorizerPurple);
						output.accept(ModItems.cadColorizerBlue);
						output.accept(ModItems.cadColorizerBrown);
						output.accept(ModItems.cadColorizerGreen);
						output.accept(ModItems.cadColorizerRed);
						output.accept(ModItems.cadColorizerBlack);
						output.accept(ModItems.cadColorizerRainbow);
						output.accept(ModItems.cadColorizerPsi);
						output.accept(ModItems.cadColorizerEmpty);

						output.accept(ModItems.spellBullet);
						output.accept(ModItems.projectileSpellBullet);
						output.accept(ModItems.loopSpellBullet);
						output.accept(ModItems.circleSpellBullet);
						output.accept(ModItems.grenadeSpellBullet);
						output.accept(ModItems.chargeSpellBullet);
						output.accept(ModItems.mineSpellBullet);

						output.accept(ModItems.spellDrive);
						output.accept(ModItems.detonator);
						output.accept(ModItems.exosuitController);

						output.accept(ModItems.exosuitSensorLight);
						output.accept(ModItems.exosuitSensorHeat);
						output.accept(ModItems.exosuitSensorStress);
						output.accept(ModItems.exosuitSensorWater);
						output.accept(ModItems.exosuitSensorTrigger);

						output.acceptAll(ItemCAD.getCreativeTabItems());

						output.accept(ModItems.vectorRuler);
						output.accept(ModItems.psimetalShovel);
						output.accept(ModItems.psimetalPickaxe);
						output.accept(ModItems.psimetalAxe);
						output.accept(ModItems.psimetalSword);
						output.accept(ModItems.psimetalExosuitHelmet);
						output.accept(ModItems.psimetalExosuitChestplate);
						output.accept(ModItems.psimetalExosuitLeggings);
						output.accept(ModItems.psimetalExosuitBoots);

						output.accept(ModBlocks.cadAssembler);
						output.accept(ModBlocks.programmer);
						output.accept(ModBlocks.psidustBlock);
						output.accept(ModBlocks.psimetalBlock);
						output.accept(ModBlocks.psigemBlock);
						output.accept(ModBlocks.psimetalPlateBlack);
						output.accept(ModBlocks.psimetalPlateBlackLight);
						output.accept(ModBlocks.psimetalPlateWhite);
						output.accept(ModBlocks.psimetalPlateWhiteLight);
						output.accept(ModBlocks.psimetalEbony);
						output.accept(ModBlocks.psimetalIvory);

						output.accept(ModBlocks.psimetalIvory);
					})
					.hideTitle()
					.backgroundSuffix(LibResources.GUI_CREATIVE)
					.withSearchBar()
					.build();
			creativeModeTabRegisterHelper.register(PSI_CREATIVE_TAB, psiCreativeTab);
		});
	}

}
