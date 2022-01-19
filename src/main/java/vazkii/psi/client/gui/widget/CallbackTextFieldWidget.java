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
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CallbackTextFieldWidget extends TextFieldWidget {
	protected final CallbackTextFieldWidget.IPressable pressable;

	public CallbackTextFieldWidget(FontRenderer font, int x, int y, int width, int height, CallbackTextFieldWidget.IPressable pressable) {
		super(font, x, y, width, height, null, StringTextComponent.EMPTY);
		this.pressable = pressable;
	}

	@Override
	public void insertText(String textToWrite) {
		super.insertText(textToWrite);
		onPress();
	}

	@Override
	public void deleteChars(int num) {
		super.deleteChars(num);
		onPress();
	}

	@Override
	public void deleteWords(int num) {
		super.deleteWords(num);
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
