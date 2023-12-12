/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.patchouli;

import com.mojang.blaze3d.vertex.Tesselator;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;

import java.util.function.UnaryOperator;

public class SpellPieceComponent implements ICustomComponent {
	private transient int x, y;
	private transient SpellPiece piece;

	private IVariable name;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX;
		this.y = componentY;
		this.piece = PsiAPI.getSpellPieceRegistry().getOptional(new ResourceLocation(name.asString()))
				.map(clazz -> SpellPiece.create(clazz, new Spell()))
				.orElseThrow(() -> new IllegalArgumentException("Invalid spell piece name: " + name));
	}

	@Override
	public void render(GuiGraphics graphics, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		graphics.pose().pushPose();
		graphics.pose().translate(x, y, 0);
		piece.draw(graphics, buffer, 0xF000F0);
		buffer.endBatch();

		if(context.isAreaHovered(mouseX, mouseY, x - 1, y - 1, 16, 16)) {
			PatchouliUtils.setPieceTooltip(context, piece);
		}
		graphics.pose().popPose();
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> function) {
		name = function.apply(name);
	}
}
