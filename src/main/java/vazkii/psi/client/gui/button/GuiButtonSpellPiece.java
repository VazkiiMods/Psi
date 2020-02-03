/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 17:35:40 (GMT)]
 */
package vazkii.psi.client.gui.button;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.gui.GuiProgrammer;

public class GuiButtonSpellPiece extends Button {
    public final SpellPiece piece;
    final GuiProgrammer gui;

    public GuiButtonSpellPiece(GuiProgrammer gui, SpellPiece piece, int x, int y) {
        super(x, y, 16, 16, "", button -> {
        });
        this.gui = gui;
        this.piece = piece;
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float pTicks) {
        if (active && visible) {
            isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;

            RenderSystem.pushMatrix();
            RenderSystem.color3f(1F, 1F, 1F);
            RenderSystem.translatef(x, y, 0);
            piece.draw();
            RenderSystem.popMatrix();

            Minecraft.getInstance().getTextureManager().bindTexture(GuiProgrammer.texture);

            if (isHovered()) {
                piece.getTooltip(gui.tooltip);
                blit(x, y, 16, gui.ySize, 16, 16);
            }

        }
    }
}
