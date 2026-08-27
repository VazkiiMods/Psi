/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.core.handler;

import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayDeque;
import java.util.Deque;

@OnlyIn(Dist.CLIENT)
public final class ClientSpellPredictionHandler {

	private static final int MAX_PREDICTION_AGE = 100;
	private static final Deque<Prediction> predictedMotion = new ArrayDeque<>();
	private static final Deque<Prediction> predictedBlinks = new ArrayDeque<>();

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
		int tick = ClientTickHandler.ticksInGame;
		Prediction previous = predictions.peekLast();
		if(previous != null && previous.tick == tick) {
			predictions.removeLast();
			value = previous.value.add(value);
		}
		predictions.addLast(new Prediction(tick, value));
	}

	private static Vec3 reconcile(Deque<Prediction> predictions, Vec3 authoritativeValue) {
		int oldestTick = ClientTickHandler.ticksInGame - MAX_PREDICTION_AGE;
		while(!predictions.isEmpty() && predictions.peekFirst().tick < oldestTick) {
			predictions.removeFirst();
		}

		Prediction prediction = predictions.pollFirst();
		return prediction == null ? authoritativeValue : authoritativeValue.subtract(prediction.value);
	}

	private record Prediction(int tick, Vec3 value) {
	}

}
