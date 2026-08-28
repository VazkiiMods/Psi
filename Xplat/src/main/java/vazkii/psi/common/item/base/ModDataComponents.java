package vazkii.psi.common.item.base;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.item.data.CADDataValue;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;

import java.util.List;
import java.util.function.UnaryOperator;

public final class ModDataComponents {
	public static final RegistryEntry<DataComponentType<BlockPos>> DST_POS = register("dst_x",
			builder -> builder.persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC));
	public static final RegistryEntry<DataComponentType<BlockPos>> SRC_POS = register("src_z",
			builder -> builder.persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC));
	public static final RegistryEntry<DataComponentType<Spell>> SPELL = register("spell",
			builder -> builder.persistent(Spell.CODEC.codec()).networkSynchronized(Spell.STREAM_CODEC));
	public static final RegistryEntry<DataComponentType<Item>> SENSOR = register("sensor",
			builder -> builder.persistent(BuiltInRegistries.ITEM.byNameCodec().orElse(Items.AIR))
					.networkSynchronized(ByteBufCodecs.registry(Registries.ITEM)).cacheEncoding());
	public static final RegistryEntry<DataComponentType<Integer>> TIMES_CAST = register("times_cast",
			builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
	public static final RegistryEntry<DataComponentType<Integer>> SELECTED_CONTROL_SLOT = register("selected_control_slot",
			builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
	public static final RegistryEntry<DataComponentType<String>> CONTRIBUTOR = register("psi_contributor_name",
			builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));
	public static final RegistryEntry<DataComponentType<CADDataValue>> CAD_DATA = register("cad_data",
			builder -> builder.persistent(CADDataValue.CODEC).networkSynchronized(CADDataValue.STREAM_CODEC).cacheEncoding());
	public static final RegistryEntry<DataComponentType<ItemContainerContents>> BULLETS = register("bullets",
			builder -> builder.persistent(ItemContainerContents.CODEC)
					.networkSynchronized(ItemContainerContents.STREAM_CODEC).cacheEncoding());
	public static final RegistryEntry<DataComponentType<Integer>> SELECTED_SLOT = register("selected_slot",
			builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
	public static final RegistryEntry<DataComponentType<Integer>> REGEN_TIME = register("regen_time",
			builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
	public static final RegistryEntry<DataComponentType<List<Item>>> COMPONENTS = register("components",
			builder -> builder.persistent(Codec.list(BuiltInRegistries.ITEM.byNameCodec().orElse(Items.AIR)))
					.networkSynchronized(ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list())).cacheEncoding());

	private ModDataComponents() {}

	public static void register() {}

	private static <T> RegistryEntry<DataComponentType<T>> register(
			String name, UnaryOperator<DataComponentType.Builder<T>> configure) {
		return PsiRegistries.register(BuiltInRegistries.DATA_COMPONENT_TYPE, PsiAPI.location(name),
				() -> configure.apply(DataComponentType.builder()).build());
	}
}
