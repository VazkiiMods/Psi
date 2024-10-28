/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.block.tile.container.ContainerCADAssembler;
import vazkii.psi.common.lib.LibResources;

public class GuiCADAssembler extends AbstractContainerScreen<ContainerCADAssembler> {

    private static final ResourceLocation texture = ResourceLocation.parse(LibResources.GUI_CAD_ASSEMBLER);
    private final Player player;
    private final TileCADAssembler assembler;

    public GuiCADAssembler(ContainerCADAssembler containerCADAssembler, Inventory inventory, Component component) {
        super(containerCADAssembler, inventory, component);
        this.player = inventory.player;
        this.assembler = containerCADAssembler.assembler;
        imageWidth = 256;
        imageHeight = 225;
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, float pTicks) {
        this.renderBackground(graphics, x, y, pTicks);
        super.render(graphics, x, y, pTicks);
        this.renderTooltip(graphics, x, y);
        //this.renderComponentHoverEffect(ms, Style.EMPTY, x, y); TODO(Kamefrede): 1.20 figure out what this breaks
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        int color = 4210752;

        String name = new ItemStack(ModBlocks.cadAssembler).getHoverName().getString();
        graphics.drawString(this.font, name, imageWidth / 2 - font.width(name) / 2, 10, color, false);

        ItemStack cad = assembler.getCachedCAD(player);
        if (!cad.isEmpty()) {
            color = 0xFFFFFF;

            int i = 0;
            ICAD cadItem = (ICAD) cad.getItem();
            String stats = I18n.get("psimisc.stats");
            String s = ChatFormatting.BOLD + stats;
            graphics.drawString(this.font, s, 213 - font.width(s) / 2f, 32, color, true);

            for (EnumCADStat stat : EnumCADStat.class.getEnumConstants()) {
                s = (Psi.magical ? ChatFormatting.LIGHT_PURPLE : ChatFormatting.AQUA) + I18n.get(stat.getName()) + ChatFormatting.RESET + ": " + cadItem.getStatValue(cad, stat);
                graphics.drawString(this.font, s, 179, 45 + i * 10, color, true);
                i++;
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.setColor(1F, 1F, 1F, 1F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(texture, x, y, 0, 0, imageWidth, imageHeight);

        for (int i = 0; i < 12; i++) {
            if (!assembler.isBulletSlotEnabled(i)) {
                graphics.blit(texture, x + 17 + i % 3 * 18, y + 57 + i / 3 * 18, 16, imageHeight, 16, 16);
            }
        }
    }

}
