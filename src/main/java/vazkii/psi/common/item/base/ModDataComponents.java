package vazkii.psi.common.item.base;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.core.handler.capability.CADData;

import java.util.List;

public class ModDataComponents {
	public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, PsiAPI.MOD_ID);
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> DST_POS = DATA_COMPONENT_TYPES.registerComponentType("dst_x", builder -> builder.persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> SRC_POS = DATA_COMPONENT_TYPES.registerComponentType("src_z", builder -> builder.persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> SPELL = DATA_COMPONENT_TYPES.registerComponentType("spell", builder -> builder.persistent(CompoundTag.CODEC).networkSynchronized(ByteBufCodecs.COMPOUND_TAG));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Item>> SENSOR = DATA_COMPONENT_TYPES.registerComponentType("sensor", builder -> builder.persistent(BuiltInRegistries.ITEM.byNameCodec().orElse(Items.AIR)).networkSynchronized(ByteBufCodecs.registry(Registries.ITEM)).cacheEncoding());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TIMES_CAST = DATA_COMPONENT_TYPES.registerComponentType("times_cast", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SELECTED_CONTROL_SLOT = DATA_COMPONENT_TYPES.registerComponentType("selected_control_slot", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> CONTRIBUTOR = DATA_COMPONENT_TYPES.registerComponentType("psi_contributor_name", builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<CADData.Data>> CAD_DATA = DATA_COMPONENT_TYPES.registerComponentType("cad_data", builder -> builder.persistent(CADData.Data.CODEC).networkSynchronized(CADData.Data.STREAM_CODEC));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> BULLETS = DATA_COMPONENT_TYPES.registerComponentType("bullets", builder -> builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC).cacheEncoding());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SELECTED_SLOT = DATA_COMPONENT_TYPES.registerComponentType("selected_slot", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> REGEN_TIME = DATA_COMPONENT_TYPES.registerComponentType("regen_time", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<Item>>> COMPONENTS = DATA_COMPONENT_TYPES.registerComponentType("components", builder -> builder.persistent(Codec.list(BuiltInRegistries.ITEM.byNameCodec().orElse(Items.AIR))).networkSynchronized(ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list())).cacheEncoding());
}
