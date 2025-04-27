package vazkii.psi.api.interval;

import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Interval<T>  {
	
	/**
	 * Returns a new interval that covers this interval and the one given as argument.
	 */
	Interval<T> combine(Interval<T> with);
	
	@OnlyIn(Dist.CLIENT)
	List<Component> getTooltip();
	
	Map<Class<?>, Interval<?>> unboundedValues = new HashMap<>();
	
}
