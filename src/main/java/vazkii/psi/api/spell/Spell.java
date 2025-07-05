/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Basic class for a spell. Not much to see here.
 */
public final class Spell {
	public static final String TAG_SPELL_NAME = "spellName";
	public static final String TAG_UUID_MOST = "uuidMost";
	public static final String TAG_UUID_LEAST = "uuidLeast";
	public static final String TAG_MODS_REQUIRED = "modsRequired";
	public static final String TAG_MOD_NAME = "modName";
	public static final String TAG_MOD_VERSION = "modVersion";
	private static final String TAG_VALID = "validSpell";
	public final SpellGrid grid = new SpellGrid(this);
	public String name = "";
	public UUID uuid;

	public Spell() {
		uuid = UUID.randomUUID();
	}

	public record Immutable(String name, UUID uuid, List<ModInformation> modsRequired, SpellGrid grid) {
		public static Immutable of(String spellName, UUID uuid, SpellGrid grid) {
			var list = new ArrayList<ModInformation>();
			var spell = new Spell();
			spell.name = spellName;
			spell.uuid = uuid;
			var immGrid = new SpellGrid(spell);
			immGrid.gridData = grid.gridData.clone();
			var imm = new Immutable(spellName, uuid, list, immGrid);
			list.addAll(imm.getModInformationForCodec());
			return imm;
		}

		private static Immutable fromCodecData(boolean valid, String spellName, List<ModInformation> modsRequired, long uuidMost, long uuidLeast, SpellGrid grid) {
			return new Immutable(spellName, new UUID(uuidMost, uuidLeast), modsRequired, grid);
		}

		public Set<String> getPieceNamespaces() {
			Set<String> temp = Collections.newSetFromMap(new HashMap<>());
			for(SpellPiece[] gridDatum : grid.gridData) {
				for(SpellPiece spellPiece : gridDatum) {
					if(spellPiece != null) {
						temp.add(spellPiece.registryKey.getNamespace());
					}
				}
			}
			return temp;
		}

		private List<ModInformation> getModInformationForCodec() {
			List<ModInformation> info = new ArrayList<>();
			for(var namespace : this.getPieceNamespaces()) {
				var optionalMod = ModList.get().getModContainerById(namespace);
				if(optionalMod.isEmpty()) {
					continue;
				}
				var mod = optionalMod.get();
				info.add(new ModInformation(mod.getModId(), mod.getModInfo().getVersion().toString()));
			}

			info.sort(Comparator.comparing(i -> i.name));
			return info;
		}

		public Spell mutable() {
			var spell = new Spell();
			spell.name = name;
			spell.uuid = this.uuid;
			spell.grid.gridData = this.grid.gridData.clone();
			return spell;
		}

		@Override
		public boolean equals(Object obj) {
			// We ignore UUID here as it is usually not meaningful for equality.
			return this == obj || obj instanceof Immutable imm && this.name.equals(imm.name) && this.grid.equals(imm.grid);
		}

		public static final MapCodec<Immutable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.BOOL.fieldOf(TAG_VALID).forGetter(s -> true),
				Codec.STRING.fieldOf(TAG_SPELL_NAME).forGetter(s -> s.name),
				Codec.list(ModInformation.CODEC.codec()).fieldOf(TAG_MODS_REQUIRED).forGetter(Immutable::getModInformationForCodec),
				Codec.LONG.fieldOf(TAG_UUID_MOST).forGetter(s -> s.uuid.getMostSignificantBits()),
				Codec.LONG.fieldOf(TAG_UUID_LEAST).forGetter(s -> s.uuid.getLeastSignificantBits()),
				Codec.lazyInitialized(SpellGrid.CODEC::codec).fieldOf("spellList").forGetter(s -> s.grid)
		).apply(instance, Immutable::fromCodecData));

		public static final StreamCodec<RegistryFriendlyByteBuf, Immutable> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.BOOL, s -> true,
				ByteBufCodecs.STRING_UTF8, s -> s.name,
				ModInformation.STREAM_CODEC.apply(ByteBufCodecs.list()), Immutable::getModInformationForCodec,
				ByteBufCodecs.VAR_LONG, s -> s.uuid.getMostSignificantBits(),
				ByteBufCodecs.VAR_LONG, s -> s.uuid.getLeastSignificantBits(),
				NeoForgeStreamCodecs.lazy(() -> SpellGrid.STREAM_CODEC), s -> s.grid,
				Immutable::fromCodecData
		);
	}

	@Nullable
	public static Spell createFromNBT(CompoundTag cmp) {
		if(cmp == null || !cmp.getBoolean(TAG_VALID)) {
			return null;
		}

		Spell spell = new Spell();
		spell.readFromNBT(cmp);
		return spell;
	}

	@OnlyIn(Dist.CLIENT)
	public void draw(PoseStack pPoseStack, MultiBufferSource buffers, int light) {
		grid.draw(pPoseStack, buffers, light);
	}

	public void readFromNBT(CompoundTag cmp) {
		name = cmp.getString(TAG_SPELL_NAME);

		if(cmp.contains(TAG_UUID_MOST)) {
			long uuidMost = cmp.getLong(TAG_UUID_MOST);
			long uuidLeast = cmp.getLong(TAG_UUID_LEAST);
			if(uuid.getMostSignificantBits() != uuidMost || uuid.getLeastSignificantBits() != uuidLeast) {
				uuid = new UUID(uuidMost, uuidLeast);
			}
		}

		grid.readFromNBT(cmp);
	}

	public Set<String> getPieceNamespaces() {
		Set<String> temp = Collections.newSetFromMap(new HashMap<>());
		for(SpellPiece[] gridDatum : grid.gridData) {
			for(SpellPiece spellPiece : gridDatum) {
				if(spellPiece != null) {
					temp.add(spellPiece.registryKey.getNamespace());
				}
			}
		}
		return temp;
	}

	public Immutable immutable() {
		return Immutable.of(this.name, this.uuid, this.grid);
	}

	public static final MapCodec<Spell> CODEC = Immutable.CODEC.xmap(Immutable::mutable, Spell::immutable);

	public static final StreamCodec<RegistryFriendlyByteBuf, Spell> STREAM_CODEC = Immutable.STREAM_CODEC.map(Immutable::mutable, Spell::immutable);

	record ModInformation(String name, String version) {
		public static final MapCodec<ModInformation> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.STRING.fieldOf(TAG_MOD_NAME).forGetter(ModInformation::name),
				Codec.STRING.fieldOf(TAG_MOD_VERSION).forGetter(ModInformation::version)
		).apply(instance, ModInformation::new));

		public static final StreamCodec<RegistryFriendlyByteBuf, ModInformation> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.STRING_UTF8, ModInformation::name,
				ByteBufCodecs.STRING_UTF8, ModInformation::version,
				ModInformation::new
		);
	}

	public void writeToNBT(CompoundTag cmp) {
		cmp.putBoolean(TAG_VALID, true);
		cmp.putString(TAG_SPELL_NAME, name);
		ListTag modList = new ListTag();
		for(String namespace : getPieceNamespaces()) {
			CompoundTag nbt = new CompoundTag();
			nbt.putString(TAG_MOD_NAME, namespace);
			if(ModList.get().getModContainerById(namespace).isPresent()) {
				nbt.putString(TAG_MOD_VERSION, ModList.get().getModContainerById(namespace).get().getModInfo().getVersion().toString());
			}
			modList.add(nbt);
		}
		cmp.put(TAG_MODS_REQUIRED, modList);
		cmp.putLong(TAG_UUID_MOST, uuid.getMostSignificantBits());
		cmp.putLong(TAG_UUID_LEAST, uuid.getLeastSignificantBits());

		grid.writeToNBT(cmp);
	}

	public Spell copy() {
		CompoundTag cmp = new CompoundTag();
		writeToNBT(cmp);
		return createFromNBT(cmp);
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof Spell oSpell && this.grid.equals(oSpell.grid);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
