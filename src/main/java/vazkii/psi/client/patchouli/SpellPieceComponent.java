package vazkii.psi.client.patchouli;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpellPieceComponent implements ICustomComponent {
	private transient int x, y;
	private transient SpellPiece piece;

	private String name;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX;
		this.y = componentY;
		this.piece = PsiAPI.getSpellPieceRegistry().getValue(new ResourceLocation(name))
				.map(clazz -> SpellPiece.create(clazz, new Spell()))
				.orElseThrow(() -> new IllegalArgumentException("Invalid spell piece name: " + name));
	}

	@Override
	public void render(IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuffer());
		MatrixStack ms = new MatrixStack();
		ms.translate(x, y, 0);
		piece.draw(ms, buffer, 0xF000F0);
		buffer.draw();

		if (context.isAreaHovered(mouseX, mouseY, x - 1, y - 1, 16, 16)) {
			List<ITextComponent> tooltip = new ArrayList<>();
			piece.getTooltip(tooltip);
			context.setHoverTooltip(tooltip.stream().map(ITextComponent::getFormattedText).collect(Collectors.toList()));
		}
	}

	@Override
	public void onVariablesAvailable(Function<String, String> function) {
		name = function.apply(name);
	}
}
