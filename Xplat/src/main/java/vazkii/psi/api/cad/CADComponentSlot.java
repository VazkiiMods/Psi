/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * One component slot of a CAD. Items with a {@code psi:cad_component} registry entry are
 * stored as the entry's holder, bound when the stack is decoded with registry-aware ops;
 * an empty slot or a Java-only {@link ICADComponent} is stored as the bare item.
 * Serialized as the item id either way, since an entry's id is its item's id.
 */
public record CADComponentSlot(Either<Holder<CADComponentDefinition>, Item> value) {

	public static final CADComponentSlot EMPTY = new CADComponentSlot(Either.right(Items.AIR));

	public static final Codec<CADComponentSlot> CODEC = new Codec<>() {
		@Override
		public <T> DataResult<Pair<CADComponentSlot, T>> decode(DynamicOps<T> ops, T input) {
			return ResourceLocation.CODEC.decode(ops, input)
					.map(pair -> pair.mapFirst(id -> resolve(ops, id)));
		}

		@Override
		public <T> DataResult<T> encode(CADComponentSlot slot, DynamicOps<T> ops, T prefix) {
			return ResourceLocation.CODEC.encode(slot.id(), ops, prefix);
		}
	};

	public static final StreamCodec<RegistryFriendlyByteBuf, CADComponentSlot> STREAM_CODEC = ByteBufCodecs.either(
			ByteBufCodecs.holderRegistry(CADComponentLookup.REGISTRY), ByteBufCodecs.registry(Registries.ITEM))
			.map(CADComponentSlot::new, CADComponentSlot::value);

	/**
	 * Resolves {@code stack} the way {@link CADComponentLookup} does; a {@code null}
	 * provider or an item without a registry entry yields the bare item.
	 */
	public static CADComponentSlot of(@Nullable HolderLookup.Provider registries, ItemStack stack) {
		Optional<Holder.Reference<CADComponentDefinition>> holder = CADComponentLookup.holder(registries, stack);
		if(holder.isPresent()) {
			return new CADComponentSlot(Either.left(holder.get()));
		}
		return new CADComponentSlot(Either.right(stack.getItem()));
	}

	public Item item() {
		return value.map(holder -> BuiltInRegistries.ITEM.get(holder.unwrapKey().orElseThrow().location()), item -> item);
	}

	public Optional<CADComponentDefinition> definition() {
		return value.left().map(Holder::value);
	}

	public boolean isEmpty() {
		return item() == Items.AIR;
	}

	private ResourceLocation id() {
		return value.map(holder -> holder.unwrapKey().orElseThrow().location(), BuiltInRegistries.ITEM::getKey);
	}

	private static <T> CADComponentSlot resolve(DynamicOps<T> ops, ResourceLocation id) {
		if(ops instanceof RegistryOps<?> registryOps) {
			Optional<Holder.Reference<CADComponentDefinition>> holder = registryOps.getter(CADComponentLookup.REGISTRY)
					.flatMap(getter -> getter.get(ResourceKey.create(CADComponentLookup.REGISTRY, id)));
			if(holder.isPresent()) {
				return new CADComponentSlot(Either.left(holder.get()));
			}
		}
		return new CADComponentSlot(Either.right(BuiltInRegistries.ITEM.get(id)));
	}

}
