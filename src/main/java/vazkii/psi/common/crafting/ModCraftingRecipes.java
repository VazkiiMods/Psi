/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [25/01/2016, 20:38:39 (GMT)]
 */
package vazkii.psi.common.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.crafting.recipe.AssemblyScavengeRecipe;
import vazkii.psi.common.crafting.recipe.BulletToDriveRecipe;
import vazkii.psi.common.crafting.recipe.ColorizerChangeRecipe;
import vazkii.psi.common.crafting.recipe.DriveDuplicateRecipe;
import vazkii.psi.common.crafting.recipe.SensorAttachRecipe;
import vazkii.psi.common.crafting.recipe.SensorRemoveRecipe;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibPieceNames;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = LibMisc.MOD_ID)
public class ModCraftingRecipes {

	@SubscribeEvent
	public static void registerSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		event.getRegistry().registerAll(
			name(AssemblyScavengeRecipe.SERIALIZER, "scavenge"),
			name(BulletToDriveRecipe.SERIALIZER, "bullet_to_drive"),
			name(ColorizerChangeRecipe.SERIALIZER, "colorizer_change"),
			name(DriveDuplicateRecipe.SERIALIZER, "drive_duplicate"),
			name(SensorAttachRecipe.SERIALIZER, "sensor_attach"),
			name(SensorRemoveRecipe.SERIALIZER, "sensor_remove")
		);
	}

	private static <T extends IForgeRegistryEntry<? extends T>> T name(T entry, String name) {
		return entry.setRegistryName(new ResourceLocation(LibMisc.MOD_ID, name));
	}

	public static void init() {
		PsiAPI.registerTrickRecipe("", Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE), new ItemStack(ModItems.psidust), new ItemStack(ModItems.cadAssemblyIron));

		PsiAPI.registerTrickRecipe(LibPieceNames.TRICK_INFUSION, Ingredient.fromTag(Tags.Items.INGOTS_GOLD),
				new ItemStack(ModItems.psimetal),
				new ItemStack(ModItems.cadAssemblyIron));
		PsiAPI.registerTrickRecipe(LibPieceNames.TRICK_GREATER_INFUSION, Ingredient.fromTag(Tags.Items.GEMS_DIAMOND),
				new ItemStack(ModItems.psimetal),
				new ItemStack(ModItems.cadAssemblyPsimetal));
		PsiAPI.registerTrickRecipe(LibPieceNames.TRICK_EBONY_IVORY, Ingredient.fromTag(Tags.Items.ORES_COAL),
				new ItemStack(ModItems.ebonySubstance),
				new ItemStack(ModItems.cadAssemblyPsimetal));
		PsiAPI.registerTrickRecipe(LibPieceNames.TRICK_EBONY_IVORY, Ingredient.fromTag(Tags.Items.GEMS_QUARTZ),
				new ItemStack(ModItems.ivoryPsimetal),
				new ItemStack(ModItems.cadAssemblyPsimetal));

	}
}
