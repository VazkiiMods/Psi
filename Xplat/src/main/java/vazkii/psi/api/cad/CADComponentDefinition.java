/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * A {@code psi:cad_component} datapack registry entry. The entry's id is the id of the
 * item it describes, so a file at {@code data/<ns>/psi/cad_component/<path>.json} turns
 * the item {@code <ns>:<path>} into a CAD component.
 *
 * <pre>
 * { "type": "assembly", "stats": { "efficiency": 70, "potency": 100 }, "model": "psi:item/cad_iron" }
 * { "type": "core",     "stats": { "complexity": 14, "projection": 1 } }
 * { "type": "dye",      "color": "#FF0000" }
 * </pre>
 *
 * Only stats whose {@link EnumCADStat#getSourceType() source type} matches {@code type}
 * are accepted. {@code model} is optional and assembly-only; {@code color} is required
 * and dye-only, given as a {@code #RRGGBB} string or a packed RGB int.
 */
public record CADComponentDefinition(EnumCADComponent type, Map<EnumCADStat, Integer> stats,
		Optional<ResourceLocation> model, Optional<Integer> color) {

	public static final Codec<Integer> COLOR_CODEC = Codec.withAlternative(
			TextColor.CODEC.xmap(TextColor::getValue, TextColor::fromRgb), Codec.INT)
			.xmap(FastColor.ARGB32::opaque, argb -> argb & 0xFFFFFF);

	public static final Codec<CADComponentDefinition> CODEC = EnumCADComponent.CODEC.dispatch("type",
			CADComponentDefinition::type, CADComponentDefinition::codecFor);

	public CADComponentDefinition {
		Map<EnumCADStat, Integer> ordered = new EnumMap<>(EnumCADStat.class);
		ordered.putAll(stats);
		stats = Collections.unmodifiableMap(ordered);
	}

	public static CADComponentDefinition assembly(Map<EnumCADStat, Integer> stats, Optional<ResourceLocation> model) {
		return new CADComponentDefinition(EnumCADComponent.ASSEMBLY, stats, model, Optional.empty());
	}

	public static CADComponentDefinition core(Map<EnumCADStat, Integer> stats) {
		return new CADComponentDefinition(EnumCADComponent.CORE, stats, Optional.empty(), Optional.empty());
	}

	public static CADComponentDefinition socket(Map<EnumCADStat, Integer> stats) {
		return new CADComponentDefinition(EnumCADComponent.SOCKET, stats, Optional.empty(), Optional.empty());
	}

	public static CADComponentDefinition battery(Map<EnumCADStat, Integer> stats) {
		return new CADComponentDefinition(EnumCADComponent.BATTERY, stats, Optional.empty(), Optional.empty());
	}

	public static CADComponentDefinition dye(int color) {
		return new CADComponentDefinition(EnumCADComponent.DYE, Map.of(), Optional.empty(), Optional.of(color));
	}

	public int stat(EnumCADStat stat) {
		return stats.getOrDefault(stat, 0);
	}

	private static MapCodec<CADComponentDefinition> codecFor(EnumCADComponent type) {
		return switch(type) {
		case ASSEMBLY -> RecordCodecBuilder.mapCodec(instance -> instance.group(
				statsCodec(type).forGetter(CADComponentDefinition::stats),
				ResourceLocation.CODEC.optionalFieldOf("model").forGetter(CADComponentDefinition::model)
		).apply(instance, CADComponentDefinition::assembly));
		case CORE -> statsCodec(type).xmap(CADComponentDefinition::core, CADComponentDefinition::stats);
		case SOCKET -> statsCodec(type).xmap(CADComponentDefinition::socket, CADComponentDefinition::stats);
		case BATTERY -> statsCodec(type).xmap(CADComponentDefinition::battery, CADComponentDefinition::stats);
		case DYE -> COLOR_CODEC.fieldOf("color").xmap(CADComponentDefinition::dye,
				definition -> definition.color().orElseThrow());
		};
	}

	private static MapCodec<Map<EnumCADStat, Integer>> statsCodec(EnumCADComponent type) {
		Codec<EnumCADStat> statKey = EnumCADStat.CODEC.validate(stat -> stat.getSourceType() == type
				? DataResult.success(stat)
				: DataResult.error(() -> "Stat " + stat.getSerializedName() + " belongs to "
						+ stat.getSourceType().getSerializedName() + " components, not " + type.getSerializedName()));
		return Codec.unboundedMap(statKey, Codec.INT).optionalFieldOf("stats", Map.of());
	}

}
