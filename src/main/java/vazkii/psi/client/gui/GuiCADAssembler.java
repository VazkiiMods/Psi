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

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;

public class GuiCADAssembler extends ContainerScreen {
    public GuiCADAssembler(Container p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {

    }
	/*
	private static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_CAD_ASSEMBLER);
	private final PlayerEntity player;
	private final TileCADAssembler assembler;

	public GuiCADAssembler(PlayerEntity player, TileCADAssembler assembler) {
		super(new ContainerCADAssembler(player, assembler));
		this.player = player;
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

		ItemStack cad = assembler.getCachedCAD(player);
		if(!cad.isEmpty()) {
			color = 0xFFFFFF;

			int i = 0;
			ICAD cadItem = (ICAD) cad.getItem();
			String stats = TooltipHelper.local("psimisc.stats");
			String s = TextFormatting.BOLD + stats;
			fontRenderer.drawStringWithShadow(s, 213 - fontRenderer.getStringWidth(s) / 2f, 34, color);

			for(EnumCADStat stat : EnumCADStat.class.getEnumConstants()) {
				s = (Psi.magical ? TextFormatting.LIGHT_PURPLE : TextFormatting.AQUA) + TooltipHelper.local(stat.getName()) + TextFormatting.RESET + ": " + cadItem.getStatValue(cad, stat);
				fontRenderer.drawStringWithShadow(s, 179, 50 + i * 10, color);
				i++;
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color3f(1F, 1F, 1F);
		minecraft.getTextureManager().bindTexture(texture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		blit(x, y, 0, 0, xSize, ySize);

		for(int i = 0; i < 12; i++)
			if(!assembler.isBulletSlotEnabled(i))
				blit(x + 17 + i % 3 * 18, y + 57 + i / 3 * 18, 16, ySize, 16, 16);
	}*/

}
