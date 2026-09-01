/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.CADComponentLookup;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibItemNames;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class ClientModelHandler {
	private static final List<ResourceLocation> ADDITIONAL_CAD_MODELS = List.of(
			model(LibItemNames.CAD_IRON),
			model(LibItemNames.CAD_GOLD),
			model(LibItemNames.CAD_PSIMETAL),
			model(LibItemNames.CAD_EBONY_PSIMETAL),
			model(LibItemNames.CAD_IVORY_PSIMETAL),
			model(LibItemNames.CAD_CREATIVE));
	private static Function<ResourceLocation, BakedModel> cadModelLookup;

	private ClientModelHandler() {}

	public static List<ResourceLocation> additionalCadModels() {
		return ADDITIONAL_CAD_MODELS;
	}

	public static void installCadModelLookup(Function<ResourceLocation, BakedModel> lookup) {
		if(cadModelLookup != null) {
			throw new IllegalStateException("CAD model lookup was installed more than once");
		}
		cadModelLookup = Objects.requireNonNull(lookup);
	}

	public static BakedModel resolveCadModel(net.minecraft.world.item.ItemStack stack) {
		ICAD cad = (ICAD) stack.getItem();
		var assemblyStack = cad.getComponentInSlot(stack, EnumCADComponent.ASSEMBLY);
		if(assemblyStack.isEmpty()) {
			return Minecraft.getInstance().getModelManager().getMissingModel();
		}
		if(cadModelLookup == null) {
			throw new IllegalStateException("CAD model lookup has not been installed");
		}
		return cadModelLookup.apply(CADComponentLookup.cadModel(cad.getComponentSlot(stack, EnumCADComponent.ASSEMBLY), assemblyStack, stack));
	}

	public static void registerItemProperties(ItemPropertyRegistrar registrar) {
		ResourceLocation activeProperty = PsiAPI.location("active");
		ClampedItemPropertyFunction hasSpell = (stack, level, entity, seed) -> ISpellAcceptor.hasSpell(stack) ? 1.0F : 0.0F;
		registrar.register(ModItems.spellBullet.get(), activeProperty, hasSpell);
		registrar.register(ModItems.chargeSpellBullet.get(), activeProperty, hasSpell);
		registrar.register(ModItems.projectileSpellBullet.get(), activeProperty, hasSpell);
		registrar.register(ModItems.loopSpellBullet.get(), activeProperty, hasSpell);
		registrar.register(ModItems.circleSpellBullet.get(), activeProperty, hasSpell);
		registrar.register(ModItems.mineSpellBullet.get(), activeProperty, hasSpell);
		registrar.register(ModItems.flashRing.get(), activeProperty, hasSpell);
	}

	@FunctionalInterface
	public interface ItemPropertyRegistrar {
		void register(Item item, ResourceLocation id, ClampedItemPropertyFunction property);
	}

	private static ResourceLocation model(String name) {
		return PsiAPI.location("item/" + name);
	}
}
