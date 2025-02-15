package vazkii.psi.api.interval;

import net.minecraft.network.chat.Component;

import java.util.List;

public class IntervalVoid implements Interval<Void> {
	
	public static final IntervalVoid instance = new IntervalVoid();
	
	private IntervalVoid() {}
	
	@Override
	public Interval<Void> combine(Interval<Void> with) {
		return this;
	}
	
	@Override
	public List<Component> getTooltip() {
		return List.of();
	}
	
}
