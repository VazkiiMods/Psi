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

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;
import vazkii.psi.api.recipe.ITrickRecipe;
import vazkii.psi.common.crafting.recipe.AssemblyScavengeRecipe;
import vazkii.psi.common.crafting.recipe.BulletToDriveRecipe;
import vazkii.psi.common.crafting.recipe.ColorizerChangeRecipe;
import vazkii.psi.common.crafting.recipe.DimensionTrickRecipe;
import vazkii.psi.common.crafting.recipe.DriveDuplicateRecipe;
import vazkii.psi.common.crafting.recipe.SensorAttachRecipe;
import vazkii.psi.common.crafting.recipe.SensorRemoveRecipe;
import vazkii.psi.common.crafting.recipe.TrickRecipe;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.data.MagicalPsiCondition;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = LibMisc.MOD_ID)
public class ModCraftingRecipes {
	public static final IRecipeType<ITrickRecipe> TRICK_RECIPE_TYPE = new RecipeType<>();

	@SubscribeEvent
	public static void registerSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		event.getRegistry().registerAll(
			name(AssemblyScavengeRecipe.SERIALIZER, "scavenge"),
			name(BulletToDriveRecipe.SERIALIZER, "bullet_to_drive"),
			name(ColorizerChangeRecipe.SERIALIZER, "colorizer_change"),
			name(DriveDuplicateRecipe.SERIALIZER, "drive_duplicate"),
			name(SensorAttachRecipe.SERIALIZER, "sensor_attach"),
			name(SensorRemoveRecipe.SERIALIZER, "sensor_remove"),
				
			name(TrickRecipe.SERIALIZER, "trick_crafting"),
			name(DimensionTrickRecipe.SERIALIZER, "dimension_trick_crafting")
		);

		CraftingHelper.register(MagicalPsiCondition.Serializer.INSTANCE);
		Registry.register(Registry.RECIPE_TYPE, ITrickRecipe.TYPE_ID, TRICK_RECIPE_TYPE);
	}

	private static <T extends IForgeRegistryEntry<? extends T>> T name(T entry, String name) {
		return entry.setRegistryName(new ResourceLocation(LibMisc.MOD_ID, name));
	}
	
	private static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T> {
		@Override
		public String toString() {
			return Registry.RECIPE_TYPE.getKey(this).toString();
		}
	}
}
