/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [03/02/2016, 18:10:29 (GMT)]
 */
package vazkii.psi.client.gui.button;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import vazkii.psi.client.gui.GuiIntroduction;

public class GuiButtonBoolean extends Button {
	public final boolean yes;

	public GuiButtonBoolean(int par2, int par3, boolean yes) {
		super(par2, par3, 12, 11, "", button -> {
		});
		this.yes = yes;
	}

	public GuiButtonBoolean(int par2, int par3, boolean yes, Button.IPressable pressable) {
		super(par2, par3, 12, 11, "", pressable);
		this.yes = yes;
	}

	@Override
	public void renderButton(int par2, int par3, float pTicks) {
		super.renderButton(par2, par2, pTicks);
		if (active) {
			isHovered = par2 >= x && par3 >= y && par2 < x + width && par3 < y + height;

			Minecraft.getInstance().textureManager.bindTexture(GuiIntroduction.texture);
			RenderSystem.color4f(1F, 1F, 1F, 1F);
			blit(x, y, yes ? 0 : 12, isHovered() ? 184 : 195, width, height);
		}
	}


}
