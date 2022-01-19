/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.patchouli;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.Tesselator;
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

public class SpellGridComponent implements ICustomComponent {
	private transient int x, y;
	private transient boolean isDownscaled;
	private transient SpellGrid grid;

	public IVariable spell;
	public IVariable halfsize;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		try {
			String spellstr = spell.asString("");
			if (StringUtil.isNullOrEmpty(spellstr)) {
				throw new IllegalArgumentException("Spell string is missing!");
			}
			CompoundTag cmp = TagParser.parseTag(spellstr);
			Spell fromNBT = Spell.createFromNBT(cmp);
			if (fromNBT == null) {
				throw new IllegalArgumentException("Invalid spell string: " + spell);
			}
			grid = fromNBT.grid;
		} catch (CommandSyntaxException e) {
			throw new IllegalArgumentException("Invalid spell string: " + spell, e);
		}
		isDownscaled = halfsize.asBoolean(false);
	}

	@Override
	public void render(PoseStack ms, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		float scale = isDownscaled ? 0.5f : 1.0f;

		MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		ms.pushPose();
		ms.translate(x, y, 0);
		ms.scale(scale, scale, scale);
		grid.draw(ms, buffer, 0xF000F0);
		buffer.endBatch();

		float scaledSize = 18 * scale;
		int scaledHoverSize = (int) (16 * scale);

		SpellPiece[][] gridData = grid.gridData;
		for (int i = 0; i < gridData.length; i++) {
			SpellPiece[] data = gridData[i];
			for (int j = 0; j < data.length; j++) {
				SpellPiece piece = data[j];
				if (piece != null && context.isAreaHovered(mouseX, mouseY, (int) (x + i * scaledSize), (int) (y + j * scaledSize), scaledHoverSize, scaledHoverSize)) {
					PatchouliUtils.setPieceTooltip(context, piece);
				}
			}
		}
		ms.popPose();
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		spell = lookup.apply(spell);
		halfsize = lookup.apply(halfsize);
	}
}
