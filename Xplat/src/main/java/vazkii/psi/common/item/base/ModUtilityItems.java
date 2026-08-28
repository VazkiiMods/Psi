/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.base;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.item.ItemDetonator;
import vazkii.psi.common.item.ItemExosuitController;
import vazkii.psi.common.item.ItemHeatExosuitSensor;
import vazkii.psi.common.item.ItemLightExosuitSensor;
import vazkii.psi.common.item.ItemStressExosuitSensor;
import vazkii.psi.common.item.ItemTriggerExosuitSensor;
import vazkii.psi.common.item.ItemVectorRuler;
import vazkii.psi.common.item.ItemWaterExosuitSensor;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;

import java.util.function.Supplier;

public final class ModUtilityItems {

	public static final RegistryEntry<ItemDetonator> detonator = item(LibItemNames.DETONATOR, () -> new ItemDetonator(properties()));
	public static final RegistryEntry<ItemExosuitController> exosuitController = item(LibItemNames.EXOSUIT_CONTROLLER, () -> new ItemExosuitController(properties()));
	public static final RegistryEntry<ItemLightExosuitSensor> exosuitSensorLight = item(LibItemNames.EXOSUIT_SENSOR_LIGHT, () -> new ItemLightExosuitSensor(properties()));
	public static final RegistryEntry<ItemHeatExosuitSensor> exosuitSensorHeat = item(LibItemNames.EXOSUIT_SENSOR_HEAT, () -> new ItemHeatExosuitSensor(properties()));
	public static final RegistryEntry<ItemStressExosuitSensor> exosuitSensorStress = item(LibItemNames.EXOSUIT_SENSOR_STRESS, () -> new ItemStressExosuitSensor(properties()));
	public static final RegistryEntry<ItemWaterExosuitSensor> exosuitSensorWater = item(LibItemNames.EXOSUIT_SENSOR_WATER, () -> new ItemWaterExosuitSensor(properties()));
	public static final RegistryEntry<ItemTriggerExosuitSensor> exosuitSensorTrigger = item(LibItemNames.EXOSUIT_SENSOR_TRIGGER, () -> new ItemTriggerExosuitSensor(properties()));
	public static final RegistryEntry<ItemVectorRuler> vectorRuler = item(LibItemNames.VECTOR_RULER, () -> new ItemVectorRuler(properties()));

	private ModUtilityItems() {}

	private static <T extends Item> RegistryEntry<T> item(String name, Supplier<T> factory) {
		return PsiRegistries.register(BuiltInRegistries.ITEM, PsiAPI.location(name), factory);
	}

	private static Item.Properties properties() {
		return new Item.Properties();
	}

	public static void register() {}
}
