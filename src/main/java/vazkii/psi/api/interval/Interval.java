package vazkii.psi.api.interval;

import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public interface Interval<T>  {
	
	/**
	 * Returns a new interval that covers this interval and the one given as argument.
	 */
	Interval<T> combine(Interval<T> with);
	
	@OnlyIn(Dist.CLIENT)
	List<Component> getTooltip();
	
	/**
	 * Returns an unbounded interval. Should be used when the specific interval type is not known.
	 * TODO Replace with something that doesn't result in ClassCastExceptions
	 */
	@SuppressWarnings("unchecked")
	static <T> Unbounded<T> unbounded() {
		return (Unbounded<T>) Unbounded.instance;
	}
	
	final class Unbounded<T> implements Interval<T> {
		
		private static final Unbounded<?> instance = new Unbounded<>();
		
		private Unbounded() {}
		
		@Override
		public Interval<T> combine(Interval<T> with) {
			return this;
		}
		
		@Override
		public List<Component> getTooltip() {
			return List.of();
		}
		
	}
	
}
