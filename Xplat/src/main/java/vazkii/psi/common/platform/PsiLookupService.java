/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.platform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface PsiLookupService {

	@Nullable
	<T> T findItem(ResourceLocation id, Class<T> apiType, ItemStack stack);

	@Nullable
	<T> T findEntity(ResourceLocation id, Class<T> apiType, Entity entity);

	<T> void registerItem(
			ResourceLocation id, Class<T> apiType, Function<ItemStack, T> factory,
			List<? extends Supplier<? extends Item>> items);

	<T> void registerEntity(
			ResourceLocation id, Class<T> apiType, Function<Entity, T> factory,
			List<? extends Supplier<? extends EntityType<?>>> entityTypes);

}
