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
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.text.TranslationTextComponent;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.common.lib.LibResources;

public class GuiButtonPage extends Button {

    public final boolean right;
    final GuiProgrammer gui;

    public GuiButtonPage(int x, int y, boolean right, GuiProgrammer gui) {
        super(x, y, 18, 10, "", button -> {
        });
        this.gui = gui;
        this.right = right;
    }

    public GuiButtonPage(int x, int y, boolean right, GuiProgrammer gui, Button.IPressable pressable) {
        super(x, y, 18, 10, "", pressable);
        this.gui = gui;
        this.right = right;
    }

    @Override
    public void renderButton(int par2, int par3, float pTicks) {
		if (active) {
			boolean hover = par2 >= x && par3 >= y && par2 < x + width && par3 < y + height;


			TextureAtlasSprite texture = PsiAPI.getMiscMaterialFromAtlas(LibResources.GUI_PROGRAMMER).getSprite();
			Minecraft.getInstance().textureManager.bindTexture(texture.getAtlas().getId());
			RenderSystem.color4f(1F, 1F, 1F, 1F);
			blit(x, y, getBlitOffset(), hover ? 216 : 198, right ? 145 : 155, texture);

			if (hover)
				gui.tooltip.add(new TranslationTextComponent(right ? "psimisc.next_page" : "psimisc.prev_page"));
		}
	}

	public boolean isRight() {
		return right;
	}
}
