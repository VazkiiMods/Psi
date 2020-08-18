/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui.widget;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CallbackTextFieldWidget extends TextFieldWidget {
	protected final CallbackTextFieldWidget.IPressable pressable;

	public CallbackTextFieldWidget(FontRenderer font, int x, int y, int width, int height, CallbackTextFieldWidget.IPressable pressable) {
		super(font, x, y, width, height, null, ITextComponent.func_241827_a_(""));
		this.pressable = pressable;
	}

	@Override
	public void writeText(String p_146191_1_) {
		super.writeText(p_146191_1_);
		onPress();
	}

	@Override
	public void deleteFromCursor(int p_146175_1_) {
		super.deleteFromCursor(p_146175_1_);
		onPress();
	}

	@Override
	public void deleteWords(int p_146177_1_) {
		super.deleteWords(p_146177_1_);
		onPress();
	}

	public void onPress() {
		pressable.onPress(this);
	}

	@OnlyIn(Dist.CLIENT)
	public interface IPressable {
		void onPress(Widget p_onPress_1_);
	}
}
