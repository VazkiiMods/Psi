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
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.util.StringUtil;

import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellGrid;
import vazkii.psi.api.spell.SpellPiece;

import java.util.function.UnaryOperator;

import static vazkii.psi.client.gui.GuiProgrammer.texture;

public class SpellGridComponent implements ICustomComponent {
	private transient SpellGrid grid;
	private transient String spellName;

	public IVariable spell;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		try {
			String spellstr = spell.asString("");
			if(StringUtil.isNullOrEmpty(spellstr)) {
				throw new IllegalArgumentException("Spell string is missing!");
			}
			CompoundTag cmp = TagParser.parseTag(spellstr);
			Spell fromNBT = Spell.createFromNBT(cmp);
			if(fromNBT == null) {
				throw new IllegalArgumentException("Invalid spell string: " + spell);
			}
			grid = fromNBT.grid;
			spellName = fromNBT.name;
		} catch (CommandSyntaxException e) {
			throw new IllegalArgumentException("Invalid spell string: " + spell, e);
		}
	}

	@Override
	public void render(GuiGraphics graphics, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		float scale = 0.65f;

		graphics.pose().pushPose();
		graphics.pose().scale(scale, scale, 0f);

		// Draw the Programmer BG
		graphics.setColor(1F, 1F, 1F, 1F);
		graphics.blit(texture, 0, 0, 0, 0, 174, 184);

		// Draw the name label and spell name
		graphics.drawString(context.getGui().getMinecraft().font, I18n.get("psimisc.name"), 7, 171, 0xFFFFFF, true);
		graphics.drawString(context.getGui().getMinecraft().font, spellName, 44, 170, 0xFFFFFF, true);

		// Pad the spell pieces and draw them
		graphics.pose().translate(7f, 7f, 0f);
		MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		grid.draw(graphics.pose(), buffer, 0xF000F0);
		buffer.endBatch();

		float scaledSize = 18 * scale;
		int scaledHoverSize = (int) (16 * scale);

		SpellPiece[][] gridData = grid.gridData;
		for(int i = 0; i < gridData.length; i++) {
			SpellPiece[] data = gridData[i];
			for(int j = 0; j < data.length; j++) {
				SpellPiece piece = data[j];
				if(piece != null && context.isAreaHovered(mouseX, mouseY, (int) (4 + i * scaledSize), (int) (4 + j * scaledSize), scaledHoverSize, scaledHoverSize)) {
					PatchouliUtils.setPieceTooltip(context, piece);
				}
			}
		}
		graphics.pose().popPose();
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		spell = lookup.apply(spell);
	}
}
