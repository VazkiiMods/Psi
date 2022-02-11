/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.ChatFormatting;

import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.block.tile.container.ContainerCADAssembler;
import vazkii.psi.common.lib.LibResources;

public class GuiCADAssembler extends AbstractContainerScreen<ContainerCADAssembler> {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_CAD_ASSEMBLER);
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
	public void render(PoseStack ms, int x, int y, float pTicks) {
		this.renderBackground(ms);
		super.render(ms, x, y, pTicks);
		this.renderTooltip(ms, x, y);
		this.renderComponentHoverEffect(ms, Style.EMPTY, x, y);
	}

	@Override
	protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
		int color = 4210752;

		String name = new ItemStack(ModBlocks.cadAssembler).getHoverName().getString();
		font.draw(ms, name, imageWidth / 2 - font.width(name) / 2, 10, color);

		ItemStack cad = assembler.getCachedCAD(player);
		if (!cad.isEmpty()) {
			color = 0xFFFFFF;

			int i = 0;
			ICAD cadItem = (ICAD) cad.getItem();
			String stats = I18n.get("psimisc.stats");
			String s = ChatFormatting.BOLD + stats;
			font.drawShadow(ms, s, 213 - font.width(s) / 2f, 34, color);

			for (EnumCADStat stat : EnumCADStat.class.getEnumConstants()) {
				s = (Psi.magical ? ChatFormatting.LIGHT_PURPLE : ChatFormatting.AQUA) + I18n.get(stat.getName()) + ChatFormatting.RESET + ": " + cadItem.getStatValue(cad, stat);
				font.drawShadow(ms, s, 179, 50 + i * 10, color);
				i++;
			}
		}
	}

	@Override
	protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		getMinecraft().getTextureManager().bindForSetup(texture);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		blit(ms, x, y, 0, 0, imageWidth, imageHeight);

		for (int i = 0; i < 12; i++) {
			if (!assembler.isBulletSlotEnabled(i)) {
				blit(ms, x + 17 + i % 3 * 18, y + 57 + i / 3 * 18, 16, imageHeight, 16, 16);
			}
		}
	}

}
