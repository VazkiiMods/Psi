package vazkii.psi.api.interval;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.DoubleUnaryOperator;

public class IntervalNumber implements Interval<Number> {
	
	public static final IntervalNumber zero = fromValue(0);
	public static final IntervalNumber one = fromValue(1);
	public static final IntervalNumber zeroToOne = new IntervalNumber(0, 1);
	public static final IntervalNumber unbounded = new IntervalNumber(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	
	public final double min, max;
	
	private IntervalNumber(double min, double max) {
		this.min = min;
		this.max = max;
	}
	
	@Override
	public Interval<Number> combine(Interval<Number> with) {
		if (!(with instanceof IntervalNumber n)) return unbounded;
		return new IntervalNumber(Math.min(min, n.min), Math.max(max, n.max));
	}
	
	private static String format(double value) {
		if (Double.isInfinite(value)) return (value > 0 ? "+" : "-") + "\u221E";
		String s = String.valueOf(value);
		return s.endsWith(".0") ? s.substring(0, s.length() - 2) : s;
	}
	
	@Override
	public List<Component> getTooltip() {
		if (min == max) return List.of(Component.literal("{").withStyle(ChatFormatting.DARK_GRAY)
				.append(Component.literal(format(min)).withStyle(Double.isFinite(min) ? ChatFormatting.WHITE : ChatFormatting.RED))
				.append("}"));
		return List.of(Component.literal(Double.isFinite(min) ? "[" : "(").withStyle(Double.isFinite(min) && Double.isFinite(max) ? ChatFormatting.DARK_AQUA : ChatFormatting.GOLD)
				.append(Component.literal(format(min)).withStyle(Double.isFinite(min) ? ChatFormatting.AQUA : ChatFormatting.YELLOW))
				.append(", ")
				.append(Component.literal(format(max)).withStyle(Double.isFinite(max) ? ChatFormatting.AQUA : ChatFormatting.YELLOW))
				.append(Double.isFinite(max) ? "]" : ")"));
	}
	
	public static IntervalNumber fromValue(double value) {
		if (!Double.isFinite(value)) return unbounded;
		return new IntervalNumber(value, value);
	}
	
	public static IntervalNumber fromRange(double min, double max) {
		if (!(min <= max)) return unbounded; // Also checks min, max != NaN implicitly
		return new IntervalNumber(min, max);
	}
	
	public IntervalNumber negate() {
		return new IntervalNumber(-max, -min);
	}
	public IntervalNumber invert() {
		if (min < 0 && max > 0) return unbounded;
		return new IntervalNumber(1 / max, 1 / min); // Floating point division takes care of the [0, x] and [x, 0] cases
	}
	public IntervalNumber abs() {
		if (min >= 0) return this;
		if (max < 0) return negate();
		return new IntervalNumber(0, Math.max(-min, max));
	}
	public IntervalNumber sign() {
		if (min > 0) return one;
		if (max < 0) return fromValue(-1);
		return new IntervalNumber(min < 0 ? -1 : 0, max > 0 ? 1 : 0);
	}
	
	public IntervalNumber add(IntervalNumber n) {
		return new IntervalNumber(min + n.min, max + n.max);
	}
	public IntervalNumber subtract(IntervalNumber n) {
		return new IntervalNumber(min - n.max, max - n.min);
	}
	public IntervalNumber multiply(IntervalNumber n) {
		return new IntervalNumber(
				Math.min(Math.min(min * n.min, min * n.max), Math.min(max * n.min, max * n.max)),
				Math.max(Math.max(min * n.min, min * n.max), Math.max(max * n.min, max * n.max))
		);
	}
	public IntervalNumber divide(IntervalNumber n) {
		return multiply(n.invert());
	}
	
	public IntervalNumber preservingMonotonicMap(DoubleUnaryOperator fn) {
		return fromRange(fn.applyAsDouble(min), fn.applyAsDouble(max));
	}
	public IntervalNumber reversingMonotonicMap(DoubleUnaryOperator fn) {
		return fromRange(fn.applyAsDouble(max), fn.applyAsDouble(min));
	}
	
	public IntervalNumber floor() {
		return preservingMonotonicMap(Math::floor);
	}
	public IntervalNumber ceil() {
		return preservingMonotonicMap(Math::ceil);
	}
	public IntervalNumber round() {
		return preservingMonotonicMap(Math::round);
	}
	
	public IntervalNumber clamp(IntervalNumber n) {
		return new IntervalNumber(Math.min(Math.max(min, n.min), n.max), Math.min(Math.max(max, n.min), n.max));
	}
	public IntervalNumber min(IntervalNumber n) {
		return new IntervalNumber(Math.min(min, n.min), Math.min(max, n.max));
	}
	public IntervalNumber max(IntervalNumber n) {
		return new IntervalNumber(Math.max(min, n.min), Math.max(max, n.max));
	}
	
}
