/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.registry;

import vazkii.psi.api.material.PsimetalArmorMaterial;
import vazkii.psi.client.fx.ModParticles;
import vazkii.psi.common.attribute.base.ModAttributes;
import vazkii.psi.common.block.base.ModBasicBlocks;
import vazkii.psi.common.block.base.ModCADAssemblerBlock;
import vazkii.psi.common.block.base.ModConjuredBlock;
import vazkii.psi.common.block.base.ModProgrammerBlock;
import vazkii.psi.common.core.PsiCreativeTab;
import vazkii.psi.common.core.capability.PsiEntityCapabilities;
import vazkii.psi.common.core.handler.ContributorSpellCircleHandler;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.crafting.ModCraftingRecipes;
import vazkii.psi.common.entity.ModEntities;
import vazkii.psi.common.item.base.ModBasicItems;
import vazkii.psi.common.item.base.ModCADComponents;
import vazkii.psi.common.item.base.ModCADItem;
import vazkii.psi.common.item.base.ModDataComponents;
import vazkii.psi.common.item.base.ModFlashRingItem;
import vazkii.psi.common.item.base.ModPsimetalItems;
import vazkii.psi.common.item.base.ModSpellItems;
import vazkii.psi.common.item.base.ModUtilityItems;
import vazkii.psi.common.network.PsiPackets;
import vazkii.psi.common.platform.PsiPlayerDataStorage;
import vazkii.psi.common.spell.base.ModSpellPieces;

public final class PsiContent {

	private PsiContent() {}

	public static void register() {
		PsiSoundHandler.register();
		ModDataComponents.register();
		ModAttributes.register();
		PsimetalArmorMaterial.register();
		ModBasicBlocks.register();
		ModCADAssemblerBlock.register();
		ModConjuredBlock.register();
		ModProgrammerBlock.register();
		ModBasicItems.register();
		ModCADComponents.register();
		ModCADItem.register();
		ModFlashRingItem.register();
		ModPsimetalItems.register();
		ModUtilityItems.register();
		ModSpellItems.register();
		PsiCreativeTab.register();
		ModCraftingRecipes.register();
		ModEntities.register();
		ModParticles.register();
		PsiEntityCapabilities.register();
		PsiPlayerDataStorage.initialize();
		PsiPackets.register();
		ModSpellPieces.register();
		ContributorSpellCircleHandler.firstStart();
	}

}
