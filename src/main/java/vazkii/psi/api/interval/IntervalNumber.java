package vazkii.psi.api.interval;

import java.util.function.DoubleUnaryOperator;

public class IntervalNumber implements Interval<Number> {
	
	public static final IntervalNumber invalid = new IntervalNumber(Double.NaN, Double.NaN);
	public static final IntervalNumber zero = fromValue(0);
	public static final IntervalNumber one = fromValue(1);
	public static final IntervalNumber zeroToOne = new IntervalNumber(0, 1);
	
	public final double min, max;
	
	private IntervalNumber(double min, double max) {
		this.min = min;
		this.max = max;
	}
	
	@Override
	public Interval<Number> combine(Interval<Number> with) {
		if (!(with instanceof IntervalNumber n)) return null;
		return new IntervalNumber(Math.min(min, n.min), Math.max(max, n.max));
	}
	
	@Override
	public boolean isValid() {
		return min <= max && Double.isFinite(min) && Double.isFinite(max);
	}
	
	public static IntervalNumber fromValue(double value) {
		if (!Double.isFinite(value)) return invalid;
		return new IntervalNumber(value, value);
	}
	
	public static IntervalNumber fromRange(double min, double max) {
		if (!Double.isFinite(min) || !Double.isFinite(max) || min > max) return invalid;
		return new IntervalNumber(min, max);
	}
	
	public IntervalNumber negate() {
		return new IntervalNumber(-max, -min);
	}
	public IntervalNumber invert() {
		if (min <= 0 && max >= 0) return invalid;
		return new IntervalNumber(1 / max, 1 / min);
	}
	public IntervalNumber abs() {
		if (min >= 0) return this;
		if (max < 0) return invert();
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
		return new IntervalNumber(Math.max(min, n.min), Math.min(max, n.max));
	}
	public IntervalNumber min(IntervalNumber n) {
		return new IntervalNumber(Math.min(min, n.min), Math.min(max, n.max));
	}
	public IntervalNumber max(IntervalNumber n) {
		return new IntervalNumber(Math.max(min, n.min), Math.max(max, n.max));
	}
	
}
