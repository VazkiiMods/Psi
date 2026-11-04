/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Deque;

public final class ClientSpellPredictionHandler {

	private static final int MAX_PREDICTION_AGE = 100;
	private static final Deque<Prediction> predictedMotion = new ArrayDeque<>();
	private static final Deque<Prediction> predictedBlinks = new ArrayDeque<>();
	private static WeakReference<LocalPlayer> owner = new WeakReference<>(null);

	private ClientSpellPredictionHandler() {}

	public static void recordMotion(Vec3 motion) {
		record(predictedMotion, motion);
	}

	public static Vec3 reconcileMotion(Vec3 authoritativeMotion) {
		return reconcile(predictedMotion, authoritativeMotion);
	}

	public static void recordBlink(Vec3 offset) {
		record(predictedBlinks, offset);
	}

	public static Vec3 reconcileBlink(Vec3 authoritativeOffset) {
		return reconcile(predictedBlinks, authoritativeOffset);
	}

	private static void record(Deque<Prediction> predictions, Vec3 value) {
		if(playerChanged()) {
			clearPredictions();
		}
		int tick = currentTick();
		Prediction previous = predictions.peekLast();
		if(previous != null && previous.tick == tick) {
			predictions.removeLast();
			value = previous.value.add(value);
		}
		predictions.addLast(new Prediction(tick, value));
	}

	private static Vec3 reconcile(Deque<Prediction> predictions, Vec3 authoritativeValue) {
		if(playerChanged()) {
			clearPredictions();
		}
		int oldestTick = currentTick() - MAX_PREDICTION_AGE;
		while(!predictions.isEmpty() && predictions.peekFirst().tick < oldestTick) {
			predictions.removeFirst();
		}

		Prediction prediction = predictions.pollFirst();
		return prediction == null ? authoritativeValue : authoritativeValue.subtract(prediction.value);
	}

	private static boolean playerChanged() {
		return Minecraft.getInstance().player != owner.get();
	}

	private static void clearPredictions() {
		owner = new WeakReference<>(Minecraft.getInstance().player);
		predictedMotion.clear();
		predictedBlinks.clear();
	}

	private static int currentTick() {
		return Minecraft.getInstance().player == null ? 0 : Minecraft.getInstance().player.tickCount;
	}

	private record Prediction(int tick, Vec3 value) {
	}

}
