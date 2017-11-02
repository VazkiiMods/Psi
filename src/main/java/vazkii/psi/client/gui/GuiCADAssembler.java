/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [10/01/2016, 16:55:16 (GMT)]
 */
package vazkii.psi.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.block.tile.container.ContainerCADAssembler;
import vazkii.psi.common.lib.LibResources;

public class GuiCADAssembler extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_CAD_ASSEMBLER);
	TileCADAssembler assembler;

	public GuiCADAssembler(EntityPlayer player, TileCADAssembler assembler) {
		super(new ContainerCADAssembler(player, assembler));
		this.assembler = assembler;
	}

	@Override
	public void initGui() {
		xSize = 256;
		ySize = 225;
		super.initGui();
	}

	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int color = 4210752;

		String name = new ItemStack(ModBlocks.cadAssembler).getDisplayName();
		fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 10, color);

		ItemStack cad = assembler.getStackInSlot(0);
		if(!cad.isEmpty()) {
			color = 0xFFFFFF;

			int i = 0;
			ICAD cadItem = (ICAD) cad.getItem();
			String stats = I18n.translateToLocal("psimisc.stats");
			String s = TextFormatting.BOLD + stats;
			fontRenderer.drawStringWithShadow(s, 213 - fontRenderer.getStringWidth(s) / 2, 34, color);

			for(EnumCADStat stat : EnumCADStat.class.getEnumConstants()) {
				s = (Psi.magical ? TextFormatting.LIGHT_PURPLE : TextFormatting.AQUA) + I18n.translateToLocal(stat.getName()) + TextFormatting.RESET + ": " + cadItem.getStatValue(cad, stat);
				fontRenderer.drawStringWithShadow(s, 179, 50 + i * 10, color);
				i++;
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1F, 1F, 1F);
		mc.getTextureManager().bindTexture(texture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		for(int i = 0; i < 12; i++)
			if(!assembler.isBulletSlotEnabled(i))
				drawTexturedModalRect(x + 17 + i % 3 * 18, y + 57 + i / 3 * 18, 16, ySize, 16, 16);
	}

}
