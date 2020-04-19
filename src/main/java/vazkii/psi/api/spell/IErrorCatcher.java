/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import javax.annotation.Nonnull;

/**
 * Interface for a SpellPiece that can catch exceptions of pieces it's attached to.
 */
public interface IErrorCatcher {

	/**
	 * Handles the exception thrown by the piece. If false is returned, the exception will be rethrown.
	 * Please set the result value to a default if you catch an exception, to prevent NullPointerExceptions.
	 * <p>
	 * Unlike other spell-related methods, this method should not throw SpellRuntimeExceptions.
	 * It should either ensure it can handle the error, or return false.
	 *
	 * @param errorPiece The piece which is throwing the exception.
	 * @param context    The erroring context.
	 * @param exception  The exception.
	 * @return Whether to suppress the exception.
	 */
	boolean catchException(SpellPiece errorPiece, SpellContext context, SpellRuntimeException exception);

	/**
	 * Set the result value to a default if you catch an exception, to prevent NullPointerExceptions.
	 * This is only called if catchException returns true, and the piece has a return type.
	 * The returned value's type should match that of the piece it's handling.
	 * <p>
	 * Unlike other spell-related methods, this method should not throw SpellRuntimeExceptions.
	 * It should ensure it can handle the error in
	 * {@link #catchException(SpellPiece, SpellContext, SpellRuntimeException)}.
	 *
	 * @param errorPiece The piece which is throwing the exception.
	 * @param context    The erroring context.
	 * @param exception  The exception.
	 * @return Whether to suppress the exception.
	 */
	@Nonnull
	Object supplyReplacementValue(SpellPiece errorPiece, SpellContext context, SpellRuntimeException exception);

}
