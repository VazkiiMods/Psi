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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

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
			CreativeModeTab psiCreativeTab = CreativeModeTab.builder()
					.icon(() -> new ItemStack(ModItems.cadAssemblyIron))
					.displayItems((parameters, output) -> {
						output.acceptAll(ItemCAD.getCreativeTabItems());
					})
					.hideTitle()
					.withBackgroundLocation(new ResourceLocation(LibMisc.MOD_ID, LibResources.GUI_CREATIVE))
					.build();
			creativeModeTabRegisterHelper.register(PSI_CREATIVE_TAB, psiCreativeTab);
		});
	}

}
