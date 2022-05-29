/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CallbackTextFieldWidget extends EditBox {
	protected final CallbackTextFieldWidget.IPressable pressable;

	public CallbackTextFieldWidget(Font font, int x, int y, int width, int height, CallbackTextFieldWidget.IPressable pressable) {
		super(font, x, y, width, height, null, TextComponent.EMPTY);
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
		void onPress(AbstractWidget p_onPress_1_);
	}
}
