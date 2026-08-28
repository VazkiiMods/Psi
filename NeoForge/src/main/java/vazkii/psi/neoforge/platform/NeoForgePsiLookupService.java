/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.platform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import org.jetbrains.annotations.Nullable;

import vazkii.psi.common.platform.PsiLookupService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class NeoForgePsiLookupService implements PsiLookupService {
	private final List<ItemRegistration<?>> itemRegistrations = new ArrayList<>();
	private final List<EntityRegistration<?>> entityRegistrations = new ArrayList<>();

	public NeoForgePsiLookupService() {
		NeoForgePsiPlatform.modBus().addListener(this::registerCapabilities);
	}

	@Override
	@Nullable
	public <T> T findItem(ResourceLocation id, Class<T> apiType, ItemStack stack) {
		return stack.getCapability(ItemCapability.createVoid(id, apiType));
	}

	@Override
	@Nullable
	public <T> T findEntity(ResourceLocation id, Class<T> apiType, Entity entity) {
		return entity.getCapability(EntityCapability.createVoid(id, apiType));
	}

	@Override
	public <T> void registerItem(
			ResourceLocation id, Class<T> apiType, Function<ItemStack, T> factory,
			List<? extends Supplier<? extends Item>> items) {
		itemRegistrations.add(new ItemRegistration<>(ItemCapability.createVoid(id, apiType), factory, items));
	}

	@Override
	public <T> void registerEntity(
			ResourceLocation id, Class<T> apiType, Function<Entity, T> factory,
			List<? extends Supplier<? extends EntityType<?>>> entityTypes) {
		entityRegistrations.add(new EntityRegistration<>(
				EntityCapability.createVoid(id, apiType), factory, entityTypes));
	}

	private void registerCapabilities(RegisterCapabilitiesEvent event) {
		itemRegistrations.forEach(registration -> registration.register(event));
		entityRegistrations.forEach(registration -> registration.register(event));
	}

	private record ItemRegistration<T>(
			ItemCapability<T, Void> capability, Function<ItemStack, T> factory,
			List<? extends Supplier<? extends Item>> items) {

		private void register(RegisterCapabilitiesEvent event) {
			Item[] resolvedItems = items.stream().map(Supplier::get).toArray(Item[]::new);
			event.registerItem(capability, (stack, context) -> factory.apply(stack), resolvedItems);
		}
	}

	private record EntityRegistration<T>(
			EntityCapability<T, Void> capability, Function<Entity, T> factory,
			List<? extends Supplier<? extends EntityType<?>>> entityTypes) {

		private void register(RegisterCapabilitiesEvent event) {
			entityTypes.stream().map(Supplier::get).forEach(type -> register(event, type));
		}

		private <E extends Entity> void register(RegisterCapabilitiesEvent event, EntityType<E> type) {
			event.registerEntity(capability, type, (entity, context) -> factory.apply(entity));
		}
	}

}
