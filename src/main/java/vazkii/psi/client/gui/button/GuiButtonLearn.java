/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 16, 2014, 4:52:06 PM (GMT)]
 */
package vazkii.psi.client.gui.button;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.client.gui.GuiLeveling;

public class GuiButtonLearn extends Button {
    final GuiLeveling gui;

    public GuiButtonLearn(int x, int y, GuiLeveling gui) {
        super(x, y, 26, 22, "", button -> {
        });
        this.gui = gui;
    }

    @Override
    public void renderButton(int par2, int par3, float pTicks) {
        if (active) {
            isHovered = par2 >= x && par3 >= y && par2 < x + width && par3 < y + height;

            Minecraft.getInstance().textureManager.bindTexture(GuiLeveling.texture);
            RenderSystem.color4f(1F, 1F, 1F, 1F);
            blit(x, y, isHovered() ? 44 : 18, 184, width, height);

            if (isHovered())
                gui.tooltip.add(new StringTextComponent(TextFormatting.GREEN + TooltipHelper.local("psimisc.learn").toString()));
        }
    }
}
