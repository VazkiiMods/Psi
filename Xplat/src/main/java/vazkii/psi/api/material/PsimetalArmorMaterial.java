package vazkii.psi.api.material;

import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;

import java.util.EnumMap;
import java.util.List;

public class PsimetalArmorMaterial {
	public static final RegistryEntry<ArmorMaterial> PSIMETAL_ARMOR_MATERIAL = PsiRegistries.register(
			BuiltInRegistries.ARMOR_MATERIAL, PsiAPI.location("psimetal"), () -> new ArmorMaterial(
					Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
						map.put(ArmorItem.Type.BOOTS, 2);
						map.put(ArmorItem.Type.LEGGINGS, 5);
						map.put(ArmorItem.Type.CHESTPLATE, 6);
						map.put(ArmorItem.Type.HELMET, 2);
						map.put(ArmorItem.Type.BODY, 5);
					}),
					12,
					SoundEvents.ARMOR_EQUIP_IRON,
					() -> Ingredient.of(BuiltInRegistries.ITEM.get(PsiAPI.location("psimetal"))),
					List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace(""), "", true)),
					0.0F,
					0.0F));

	public static void register() {}
}
