package vazkii.psi.api.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
@Cancelable
@Event.HasResult
public class RenderPsiHudEvent extends Event {

	@Nonnull
	private final PsiHudElementType type;

	public RenderPsiHudEvent(@Nonnull PsiHudElementType type) {
		this.type = type;
	}

	@Nonnull
	public PsiHudElementType getType() {
		return type;
	}
}
