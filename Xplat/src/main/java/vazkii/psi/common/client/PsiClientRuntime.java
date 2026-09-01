/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.client;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.block.tile.TileProgrammer;

import java.util.Objects;

public final class PsiClientRuntime {

	private static final Hooks NOOP = new Hooks() {};
	private static Hooks hooks = NOOP;

	private PsiClientRuntime() {}

	public static void install(Hooks clientHooks) {
		if(hooks != NOOP) {
			throw new IllegalStateException("Psi client runtime hooks were installed more than once");
		}
		hooks = Objects.requireNonNull(clientHooks);
	}

	public static boolean hasAdvancement(Player player, ResourceLocation advancement) {
		return hooks.hasAdvancement(advancement, player);
	}

	public static boolean hasShiftDown() {
		return hooks.hasShiftDown();
	}

	public static boolean hasControlDown() {
		return hooks.hasControlDown();
	}

	public static float clientTicks() {
		return hooks.clientTicks();
	}

	public static int colorForCAD(ItemStack stack) {
		return stack.getItem() instanceof ICAD cad ? cad.getSpellColor(stack) : -1;
	}

	public static void sparkle(Level level, double x, double y, double z, float red, float green, float blue,
			float motionX, float motionY, float motionZ, float size, int lifetime) {
		hooks.sparkle(level, x, y, z, red, green, blue, motionX, motionY, motionZ, size, lifetime);
	}

	public static void wisp(Level level, double x, double y, double z, float red, float green, float blue,
			float size, float motionX, float motionY, float motionZ, float maxAgeMultiplier) {
		hooks.wisp(level, x, y, z, red, green, blue, size, motionX, motionY, motionZ, maxAgeMultiplier);
	}

	public static void showRemainingItems(ItemStack stack, int count) {
		hooks.showRemainingItems(stack, count);
	}

	public static Vec3 reconcilePredictedMotion(Vec3 authoritativeMotion) {
		return hooks.reconcilePredictedMotion(authoritativeMotion);
	}

	public static void recordPredictedMotion(Vec3 motion) {
		hooks.recordPredictedMotion(motion);
	}

	public static void recordPredictedBlink(Vec3 offset) {
		hooks.recordPredictedBlink(offset);
	}

	public static Vec3 reconcilePredictedBlink(Vec3 authoritativeOffset) {
		return hooks.reconcilePredictedBlink(authoritativeOffset);
	}

	public static void showSpamlessChat(Component message, int magic) {
		hooks.showSpamlessChat(message, magic);
	}

	public static void showSpellError(String message, int x, int y) {
		hooks.showSpellError(message, x, y);
	}

	public static void openProgrammer(TileProgrammer programmer) {
		hooks.openProgrammer(programmer);
	}

	public static void openFlashRing(ItemStack stack) {
		hooks.openFlashRing(stack);
	}

	public interface Hooks {
		default boolean hasShiftDown() {
			return false;
		}

		default boolean hasControlDown() {
			return false;
		}

		default float clientTicks() {
			return 0;
		}

		default boolean hasAdvancement(ResourceLocation advancement, Player player) {
			return false;
		}

		default void sparkle(Level level, double x, double y, double z, float red, float green, float blue,
				float motionX, float motionY, float motionZ, float size, int lifetime) {}

		default void wisp(Level level, double x, double y, double z, float red, float green, float blue,
				float size, float motionX, float motionY, float motionZ, float maxAgeMultiplier) {}

		default void showRemainingItems(ItemStack stack, int count) {}

		default void recordPredictedMotion(Vec3 motion) {}

		default Vec3 reconcilePredictedMotion(Vec3 authoritativeMotion) {
			return authoritativeMotion;
		}

		default void recordPredictedBlink(Vec3 offset) {}

		default Vec3 reconcilePredictedBlink(Vec3 authoritativeOffset) {
			return authoritativeOffset;
		}

		default void showSpamlessChat(Component message, int magic) {}

		default void showSpellError(String message, int x, int y) {}

		default void openProgrammer(TileProgrammer programmer) {}

		default void openFlashRing(ItemStack stack) {}

	}

}
