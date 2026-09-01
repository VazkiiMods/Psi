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
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Per-piece overrides loaded from {@code data/<piece namespace>/psi/spell_piece_settings/<piece path>.json}.
 * A piece without an entry is enabled. Disabled pieces are hidden from the programmer, rejected on
 * import, and fail spell compilation.
 */
public record SpellPieceSettings(boolean enabled) {

	public static final Codec<SpellPieceSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.BOOL.fieldOf("enabled").forGetter(SpellPieceSettings::enabled)
	).apply(instance, SpellPieceSettings::new));

}
