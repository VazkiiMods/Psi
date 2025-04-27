/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.base;

import com.mojang.serialization.Codec;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.client.model.ArmorModels;
import vazkii.psi.common.core.handler.capability.CADData;
import vazkii.psi.common.item.*;
import vazkii.psi.common.item.armor.ItemPsimetalExosuitBoots;
import vazkii.psi.common.item.armor.ItemPsimetalExosuitChestplate;
import vazkii.psi.common.item.armor.ItemPsimetalExosuitHelmet;
import vazkii.psi.common.item.armor.ItemPsimetalExosuitLeggings;
import vazkii.psi.common.item.component.*;
import vazkii.psi.common.item.tool.ItemPsimetalAxe;
import vazkii.psi.common.item.tool.ItemPsimetalPickaxe;
import vazkii.psi.common.item.tool.ItemPsimetalShovel;
import vazkii.psi.common.item.tool.ItemPsimetalSword;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.spell.base.ModSpellPieces;

import java.util.List;

@EventBusSubscriber(modid = LibMisc.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ModItems {
	public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(PsiAPI.MOD_ID);
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<Item>>> COMPONENTS = DATA_COMPONENT_TYPES.registerComponentType("components", builder -> builder.persistent(Codec.list(BuiltInRegistries.ITEM.byNameCodec().orElse(Items.AIR))).networkSynchronized(ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list())).cacheEncoding());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TAG_REGEN_TIME = DATA_COMPONENT_TYPES.registerComponentType("regen_time", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TAG_SELECTED_SLOT = DATA_COMPONENT_TYPES.registerComponentType("selected_slot", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> TAG_BULLETS = DATA_COMPONENT_TYPES.registerComponentType("bullets", builder -> builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC).cacheEncoding());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<CADData.Data>> CAD_DATA = DATA_COMPONENT_TYPES.registerComponentType("cad_data", builder -> builder.persistent(CADData.Data.CODEC).networkSynchronized(CADData.Data.STREAM_CODEC));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> TAG_CONTRIBUTOR = DATA_COMPONENT_TYPES.registerComponentType("psi_contributor_name", builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TAG_SELECTED_CONTROL_SLOT = DATA_COMPONENT_TYPES.registerComponentType("selected_control_slot", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TAG_TIMES_CAST = DATA_COMPONENT_TYPES.registerComponentType("times_cast", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Item>> TAG_SENSOR = DATA_COMPONENT_TYPES.registerComponentType("sensor", builder -> builder.persistent(BuiltInRegistries.ITEM.byNameCodec().orElse(Items.AIR)).networkSynchronized(ByteBufCodecs.registry(Registries.ITEM)).cacheEncoding());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> HAS_SPELL = DATA_COMPONENT_TYPES.registerComponentType("has_spell", builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> TAG_SPELL = DATA_COMPONENT_TYPES.registerComponentType("spell", builder -> builder.persistent(CompoundTag.CODEC).networkSynchronized(ByteBufCodecs.COMPOUND_TAG));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> TAG_SRC_POS = DATA_COMPONENT_TYPES.registerComponentType("src_z", builder -> builder.persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> TAG_DST_POS = DATA_COMPONENT_TYPES.registerComponentType("dst_x", builder -> builder.persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC));

	public static Item psidust;
	public static Item psimetal;
	public static Item psigem;
	public static Item ebonyPsimetal;
	public static Item ivoryPsimetal;
	public static Item ebonySubstance;
	public static Item ivorySubstance;

	public static Item cadAssemblyIron;
	public static Item cadAssemblyGold;
	public static Item cadAssemblyPsimetal;
	public static Item cadAssemblyIvory;
	public static Item cadAssemblyEbony;
	public static Item cadAssemblyCreative;

	public static Item cadCoreBasic;
	public static Item cadCoreOverclocked;
	public static Item cadCoreConductive;
	public static Item cadCoreHyperClocked;
	public static Item cadCoreRadiative;

	public static Item cadSocketBasic;
	public static Item cadSocketSignaling;
	public static Item cadSocketLarge;
	public static Item cadSocketTransmissive;
	public static Item cadSocketHuge;

	public static Item cadBatteryBasic;
	public static Item cadBatteryExtended;
	public static Item cadBatteryUltradense;

	public static Item cadColorizerWhite;
	public static Item cadColorizerOrange;
	public static Item cadColorizerMagenta;
	public static Item cadColorizerLightBlue;
	public static Item cadColorizerYellow;
	public static Item cadColorizerLime;
	public static Item cadColorizerPink;
	public static Item cadColorizerGray;
	public static Item cadColorizerLightGray;
	public static Item cadColorizerCyan;
	public static Item cadColorizerPurple;
	public static Item cadColorizerBlue;
	public static Item cadColorizerBrown;
	public static Item cadColorizerGreen;
	public static Item cadColorizerRed;
	public static Item cadColorizerBlack;
	public static Item cadColorizerRainbow;
	public static Item cadColorizerPsi;
	public static Item cadColorizerEmpty;

	public static Item spellBullet;
	public static Item projectileSpellBullet;
	public static Item loopSpellBullet;
	public static Item circleSpellBullet;
	public static Item grenadeSpellBullet;
	public static Item chargeSpellBullet;
	public static Item mineSpellBullet;

	public static Item spellDrive;
	public static Item detonator;
	public static Item exosuitController;

	public static Item exosuitSensorLight;
	public static Item exosuitSensorHeat;
	public static Item exosuitSensorStress;
	public static Item exosuitSensorWater;
	public static Item exosuitSensorTrigger;
	public static Item cad;

	public static Item vectorRuler;
	public static Item psimetalShovel;
	public static Item psimetalPickaxe;
	public static Item psimetalAxe;
	public static Item psimetalSword;
	public static Item psimetalExosuitHelmet;
	public static Item psimetalExosuitChestplate;
	public static Item psimetalExosuitLeggings;
	public static Item psimetalExosuitBoots;

	@SubscribeEvent
	public static void register(RegisterEvent evt) {
		evt.register(Registries.ITEM, helper -> {
			psidust = new Item(defaultBuilder());
			psimetal = new Item(defaultBuilder());
			psigem = new Item(defaultBuilder());
			ebonyPsimetal = new Item(defaultBuilder());
			ivoryPsimetal = new Item(defaultBuilder());
			ebonySubstance = new Item(defaultBuilder());
			ivorySubstance = new Item(defaultBuilder());

			cadAssemblyIron = new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_IRON);
			cadAssemblyGold = new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_GOLD);
			cadAssemblyPsimetal = new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_PSIMETAL);
			cadAssemblyIvory = new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_IVORY_PSIMETAL);
			cadAssemblyEbony = new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_EBONY_PSIMETAL);
			cadAssemblyCreative = new ItemCADAssembly(defaultBuilder(), LibItemNames.CAD_CREATIVE);

			cadCoreBasic = new ItemCADCore(defaultBuilder());
			cadCoreOverclocked = new ItemCADCore(defaultBuilder());
			cadCoreConductive = new ItemCADCore(defaultBuilder());
			cadCoreHyperClocked = new ItemCADCore(defaultBuilder());
			cadCoreRadiative = new ItemCADCore(defaultBuilder());

			cadSocketBasic = new ItemCADSocket(defaultBuilder());
			cadSocketSignaling = new ItemCADSocket(defaultBuilder());
			cadSocketLarge = new ItemCADSocket(defaultBuilder());
			cadSocketTransmissive = new ItemCADSocket(defaultBuilder());
			cadSocketHuge = new ItemCADSocket(defaultBuilder());

			cadBatteryBasic = new ItemCADBattery(defaultBuilder());
			cadBatteryExtended = new ItemCADBattery(defaultBuilder());
			cadBatteryUltradense = new ItemCADBattery(defaultBuilder());

			cadColorizerWhite = new ItemCADColorizer(defaultBuilder(), DyeColor.WHITE);
			cadColorizerOrange = new ItemCADColorizer(defaultBuilder(), DyeColor.ORANGE);
			cadColorizerMagenta = new ItemCADColorizer(defaultBuilder(), DyeColor.MAGENTA);
			cadColorizerLightBlue = new ItemCADColorizer(defaultBuilder(), DyeColor.LIGHT_BLUE);
			cadColorizerYellow = new ItemCADColorizer(defaultBuilder(), DyeColor.YELLOW);
			cadColorizerLime = new ItemCADColorizer(defaultBuilder(), DyeColor.LIME);
			cadColorizerPink = new ItemCADColorizer(defaultBuilder(), DyeColor.PINK);
			cadColorizerGray = new ItemCADColorizer(defaultBuilder(), DyeColor.GRAY);
			cadColorizerLightGray = new ItemCADColorizer(defaultBuilder(), DyeColor.LIGHT_GRAY);
			cadColorizerCyan = new ItemCADColorizer(defaultBuilder(), DyeColor.CYAN);
			cadColorizerPurple = new ItemCADColorizer(defaultBuilder(), DyeColor.PURPLE);
			cadColorizerBlue = new ItemCADColorizer(defaultBuilder(), DyeColor.BLUE);
			cadColorizerBrown = new ItemCADColorizer(defaultBuilder(), DyeColor.BROWN);
			cadColorizerGreen = new ItemCADColorizer(defaultBuilder(), DyeColor.GREEN);
			cadColorizerRed = new ItemCADColorizer(defaultBuilder(), DyeColor.RED);
			cadColorizerBlack = new ItemCADColorizer(defaultBuilder(), DyeColor.BLACK);
			cadColorizerRainbow = new ItemCADColorizerRainbow(defaultBuilder());
			cadColorizerPsi = new ItemCADColorizerPsi(defaultBuilder());
			cadColorizerEmpty = new ItemCADColorizerEmpty(defaultBuilder());

			spellBullet = new ItemSpellBullet(defaultBuilder());
			projectileSpellBullet = new ItemProjectileSpellBullet(defaultBuilder());
			loopSpellBullet = new ItemLoopcastSpellBullet(defaultBuilder());
			circleSpellBullet = new ItemCircleSpellBullet(defaultBuilder());
			grenadeSpellBullet = new ItemGrenadeSpellBullet(defaultBuilder());
			chargeSpellBullet = new ItemChargeSpellBullet(defaultBuilder());
			mineSpellBullet = new ItemMineSpellBullet(defaultBuilder());

			spellDrive = new ItemSpellDrive(defaultBuilder());
			detonator = new ItemDetonator(defaultBuilder());
			exosuitController = new ItemExosuitController(defaultBuilder());

			exosuitSensorLight = new ItemLightExosuitSensor(defaultBuilder());
			exosuitSensorHeat = new ItemHeatExosuitSensor(defaultBuilder());
			exosuitSensorStress = new ItemStressExosuitSensor(defaultBuilder());
			exosuitSensorWater = new ItemWaterExosuitSensor(defaultBuilder());
			exosuitSensorTrigger = new ItemTriggerExosuitSensor(defaultBuilder());
			cad = new ItemCAD(defaultBuilder());

			vectorRuler = new ItemVectorRuler(defaultBuilder());
			psimetalShovel = new ItemPsimetalShovel(defaultBuilder());
			psimetalPickaxe = new ItemPsimetalPickaxe(defaultBuilder());
			psimetalAxe = new ItemPsimetalAxe(defaultBuilder());
			psimetalSword = new ItemPsimetalSword(defaultBuilder());
			psimetalExosuitHelmet = new ItemPsimetalExosuitHelmet(ArmorItem.Type.HELMET, defaultBuilder().durability(ArmorItem.Type.HELMET.getDurability(18)));
			psimetalExosuitChestplate = new ItemPsimetalExosuitChestplate(ArmorItem.Type.CHESTPLATE, defaultBuilder().durability(ArmorItem.Type.CHESTPLATE.getDurability(18)));
			psimetalExosuitLeggings = new ItemPsimetalExosuitLeggings(ArmorItem.Type.LEGGINGS, defaultBuilder().durability(ArmorItem.Type.LEGGINGS.getDurability(18)));
			psimetalExosuitBoots = new ItemPsimetalExosuitBoots(ArmorItem.Type.BOOTS, defaultBuilder().durability(ArmorItem.Type.BOOTS.getDurability(18)));

			ModSpellPieces.init();

			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.PSIDUST), psidust);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.PSIMETAL), psimetal);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.PSIGEM), psigem);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.EBONY_PSIMETAL), ebonyPsimetal);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.IVORY_PSIMETAL), ivoryPsimetal);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.EBONY_SUBSTANCE), ebonySubstance);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.IVORY_SUBSTANCE), ivorySubstance);

			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_ASSEMBLY_CREATIVE), cadAssemblyCreative);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_ASSEMBLY_IRON), cadAssemblyIron);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_ASSEMBLY_GOLD), cadAssemblyGold);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_ASSEMBLY_PSIMETAL), cadAssemblyPsimetal);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_ASSEMBLY_IVORY_PSIMETAL), cadAssemblyIvory);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_ASSEMBLY_EBONY_PSIMETAL), cadAssemblyEbony);

			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_CORE_BASIC), cadCoreBasic);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_CORE_OVERCLOCKED), cadCoreOverclocked);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_CORE_CONDUCTIVE), cadCoreConductive);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_CORE_HYPERCLOCKED), cadCoreHyperClocked);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_CORE_RADIATIVE), cadCoreRadiative);

			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_SOCKET_BASIC), cadSocketBasic);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_SOCKET_SIGNALING), cadSocketSignaling);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_SOCKET_LARGE), cadSocketLarge);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_SOCKET_TRANSMISSIVE), cadSocketTransmissive);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_SOCKET_HUGE), cadSocketHuge);

			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_BATTERY_BASIC), cadBatteryBasic);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_BATTERY_EXTENDED), cadBatteryExtended);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_BATTERY_ULTRADENSE), cadBatteryUltradense);

			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_WHITE), cadColorizerWhite);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_ORANGE), cadColorizerOrange);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_MAGENTA), cadColorizerMagenta);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_LIGHT_BLUE), cadColorizerLightBlue);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_YELLOW), cadColorizerYellow);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_LIME), cadColorizerLime);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_PINK), cadColorizerPink);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_GRAY), cadColorizerGray);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_LIGHT_GRAY), cadColorizerLightGray);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_CYAN), cadColorizerCyan);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_PURPLE), cadColorizerPurple);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_BLUE), cadColorizerBlue);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_BROWN), cadColorizerBrown);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_GREEN), cadColorizerGreen);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_RED), cadColorizerRed);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_BLACK), cadColorizerBlack);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_RAINBOW), cadColorizerRainbow);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_PSI), cadColorizerPsi);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD_COLORIZER_EMPTY), cadColorizerEmpty);

			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.SPELL_BULLET), spellBullet);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.SPELL_BULLET_PROJECTILE), projectileSpellBullet);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.SPELL_BULLET_LOOP), loopSpellBullet);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.SPELL_BULLET_CIRCLE), circleSpellBullet);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.SPELL_BULLET_GRENADE), grenadeSpellBullet);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.SPELL_BULLET_CHARGE), chargeSpellBullet);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.SPELL_BULLET_MINE), mineSpellBullet);

			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.SPELL_DRIVE), spellDrive);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.DETONATOR), detonator);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.EXOSUIT_CONTROLLER), exosuitController);

			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.EXOSUIT_SENSOR_LIGHT), exosuitSensorLight);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.EXOSUIT_SENSOR_HEAT), exosuitSensorHeat);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.EXOSUIT_SENSOR_STRESS), exosuitSensorStress);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.EXOSUIT_SENSOR_WATER), exosuitSensorWater);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.EXOSUIT_SENSOR_TRIGGER), exosuitSensorTrigger);

			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.CAD), cad);

			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.VECTOR_RULER), vectorRuler);

			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.PSIMETAL_SHOVEL), psimetalShovel);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.PSIMETAL_PICKAXE), psimetalPickaxe);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.PSIMETAL_AXE), psimetalAxe);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.PSIMETAL_SWORD), psimetalSword);

			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.PSIMETAL_EXOSUIT_HELMET), psimetalExosuitHelmet);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.PSIMETAL_EXOSUIT_CHESTPLATE), psimetalExosuitChestplate);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.PSIMETAL_EXOSUIT_LEGGINGS), psimetalExosuitLeggings);
			helper.register(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, LibItemNames.PSIMETAL_EXOSUIT_BOOTS), psimetalExosuitBoots);
		});
	}

	@SubscribeEvent
	public static void initializeClient(RegisterClientExtensionsEvent event) {
		event.registerItem(new IClientItemExtensions() {
			@Override
			public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
				return ArmorModels.get(itemStack);
			}
		}, psimetalExosuitHelmet, psimetalExosuitChestplate, psimetalExosuitLeggings, psimetalExosuitBoots);
	}

	public static Item.Properties defaultBuilder() {
		return new Item.Properties();
	}

}
