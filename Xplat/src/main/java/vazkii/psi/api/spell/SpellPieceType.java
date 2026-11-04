/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import vazkii.psi.api.PsiAPI;

import java.lang.reflect.Constructor;
import java.util.function.Function;

/**
 * The registry value for a kind of {@link SpellPiece}: how to construct one.
 */
public final class SpellPieceType {

	private final Function<Spell, ? extends SpellPiece> factory;

	private SpellPieceType(Function<Spell, ? extends SpellPiece> factory) {
		this.factory = factory;
	}

	public static SpellPieceType of(Function<Spell, ? extends SpellPiece> factory) {
		return new SpellPieceType(factory);
	}

	/**
	 * Migration shim for pieces written against the old class-keyed registry: instances are
	 * constructed reflectively through the public {@code (Spell)} constructor.
	 */
	public static <T extends SpellPiece> SpellPieceType ofClass(Class<T> clazz) {
		Constructor<T> constructor;
		try {
			constructor = clazz.getConstructor(Spell.class);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(clazz.getName() + " has no public (Spell) constructor", e);
		}

		return new SpellPieceType(spell -> {
			try {
				return constructor.newInstance(spell);
			} catch (ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
		});
	}

	/**
	 * Creates a piece bound to the given spell. The piece's registry key is taken from this
	 * type's registration, so this must only be called once the type is registered.
	 */
	public SpellPiece create(Spell spell) {
		SpellPiece piece = factory.apply(spell);
		piece.registryKey = PsiAPI.SPELL_PIECE_REGISTRY.getKey(this);
		return piece;
	}

}
