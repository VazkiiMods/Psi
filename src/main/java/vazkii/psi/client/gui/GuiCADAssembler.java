/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.block.tile.container.ContainerCADAssembler;
import vazkii.psi.common.lib.LibResources;

public class GuiCADAssembler extends ContainerScreen<ContainerCADAssembler> {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_CAD_ASSEMBLER);
	private final PlayerEntity player;
	private final TileCADAssembler assembler;

	public GuiCADAssembler(ContainerCADAssembler containerCADAssembler, PlayerInventory inventory, ITextComponent component) {
		super(containerCADAssembler, inventory, component);
		this.player = inventory.player;
		this.assembler = containerCADAssembler.assembler;
		xSize = 256;
		ySize = 225;
	}

	@Override
	public void render(MatrixStack ms, int x, int y, float pTicks) {
		this.renderBackground(ms);
		super.render(ms, x, y, pTicks);
		//this.renderHoveredToolTip(ms, x, y);
		this.renderTextHoverEffect(ms, Style.EMPTY, x, y);
	}

	@Override
	protected void drawForeground(MatrixStack ms, int mouseX, int mouseY) {
		int color = 4210752;

		String name = new ItemStack(ModBlocks.cadAssembler).getDisplayName().getString();
		textRenderer.draw(ms, name, xSize / 2 - textRenderer.getStringWidth(name) / 2, 10, color);

		ItemStack cad = assembler.getCachedCAD(player);
		if (!cad.isEmpty()) {
			color = 0xFFFFFF;

			int i = 0;
			ICAD cadItem = (ICAD) cad.getItem();
			String stats = I18n.format("psimisc.stats");
			String s = TextFormatting.BOLD + stats;
			textRenderer.drawWithShadow(ms, s, 213 - textRenderer.getStringWidth(s) / 2f, 34, color);

			for (EnumCADStat stat : EnumCADStat.class.getEnumConstants()) {
				s = (Psi.magical ? TextFormatting.LIGHT_PURPLE : TextFormatting.AQUA) + I18n.format(stat.getName()) + TextFormatting.RESET + ": " + cadItem.getStatValue(cad, stat);
				textRenderer.drawWithShadow(ms, s, 179, 50 + i * 10, color);
				i++;
			}
		}
	}


	@Override
	protected void drawBackground(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color3f(1F, 1F, 1F);
		getMinecraft().getTextureManager().bindTexture(texture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexture(ms, x, y, 0, 0, xSize, ySize);

		for (int i = 0; i < 12; i++) {
			if (!assembler.isBulletSlotEnabled(i)) {
				drawTexture(ms, x + 17 + i % 3 * 18, y + 57 + i / 3 * 18, 16, ySize, 16, 16);
			}
		}
	}

}
