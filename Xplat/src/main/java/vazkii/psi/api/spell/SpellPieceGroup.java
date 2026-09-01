/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import java.util.List;

/**
 * A set of spell pieces unlocked together, loaded from
 * {@code data/<namespace>/psi/spell_piece_group/<name>.json}. The group's advancement is the
 * one registered under the same id. {@code main} is the piece the group is presented by;
 * {@code pieces} are the remaining members. {@code unlock} decides when
 * {@link PieceGroupAdvancementComplete} is posted for the group.
 */
public record SpellPieceGroup(ResourceLocation main, List<ResourceLocation> pieces, Unlock unlock) {

	public static final Codec<SpellPieceGroup> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("main").forGetter(SpellPieceGroup::main),
			ResourceLocation.CODEC.listOf().fieldOf("pieces").forGetter(SpellPieceGroup::pieces),
			Unlock.CODEC.optionalFieldOf("unlock", Unlock.ExecuteMain.INSTANCE).forGetter(SpellPieceGroup::unlock)
	).apply(instance, SpellPieceGroup::new));

	public SpellPieceGroup {
		pieces = List.copyOf(pieces);
	}

	public SpellPieceGroup(ResourceLocation main, List<ResourceLocation> pieces) {
		this(main, pieces, Unlock.ExecuteMain.INSTANCE);
	}

	public boolean contains(ResourceLocation piece) {
		return main.equals(piece) || pieces.contains(piece);
	}

	public sealed interface Unlock permits Unlock.ExecuteMain, Unlock.Advancement {

		Codec<Unlock> CODEC = Type.CODEC.dispatch("type", Unlock::type, Type::codec);

		Type type();

		/**
		 * The group unlocks the first time its {@code main} piece is executed.
		 */
		record ExecuteMain() implements Unlock {
			public static final ExecuteMain INSTANCE = new ExecuteMain();
			public static final MapCodec<ExecuteMain> CODEC = MapCodec.unit(INSTANCE);

			@Override
			public Type type() {
				return Type.EXECUTE_MAIN;
			}
		}

		/**
		 * The group unlocks when the player completes {@code advancement}.
		 */
		record Advancement(ResourceLocation advancement) implements Unlock {
			public static final MapCodec<Advancement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					ResourceLocation.CODEC.fieldOf("advancement").forGetter(Advancement::advancement)
			).apply(instance, Advancement::new));

			@Override
			public Type type() {
				return Type.ADVANCEMENT;
			}
		}

		enum Type implements StringRepresentable {
			EXECUTE_MAIN("psi:execute_main", ExecuteMain.CODEC),
			ADVANCEMENT("psi:advancement", Advancement.CODEC);

			public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

			private final String name;
			private final MapCodec<? extends Unlock> codec;

			Type(String name, MapCodec<? extends Unlock> codec) {
				this.name = name;
				this.codec = codec;
			}

			@Override
			public String getSerializedName() {
				return name;
			}

			public MapCodec<? extends Unlock> codec() {
				return codec;
			}
		}
	}

}
