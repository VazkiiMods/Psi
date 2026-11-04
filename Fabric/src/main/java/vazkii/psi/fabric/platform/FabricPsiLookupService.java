/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.fabric.platform;

import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import vazkii.psi.common.platform.PsiLookupService;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class FabricPsiLookupService implements PsiLookupService {

	@Override
	@Nullable
	public <T> T findItem(ResourceLocation id, Class<T> apiType, ItemStack stack) {
		return ItemApiLookup.get(id, apiType, Void.class).find(stack, null);
	}

	@Override
	@Nullable
	public <T> T findEntity(ResourceLocation id, Class<T> apiType, Entity entity) {
		return EntityApiLookup.get(id, apiType, Void.class).find(entity, null);
	}

	@Override
	public <T> void registerItem(
			ResourceLocation id, Class<T> apiType, Function<ItemStack, T> factory,
			List<? extends Supplier<? extends Item>> items) {
		Item[] resolvedItems = items.stream().map(Supplier::get).toArray(Item[]::new);
		ItemApiLookup.get(id, apiType, Void.class)
				.registerForItems((stack, context) -> factory.apply(stack), resolvedItems);
	}

	@Override
	public <T> void registerEntity(
			ResourceLocation id, Class<T> apiType, Function<Entity, T> factory,
			List<? extends Supplier<? extends EntityType<?>>> entityTypes) {
		EntityType<?>[] resolvedTypes = entityTypes.stream().map(Supplier::get).toArray(EntityType<?>[]::new);
		EntityApiLookup.get(id, apiType, Void.class)
				.registerForTypes((entity, context) -> factory.apply(entity), resolvedTypes);
	}

}
