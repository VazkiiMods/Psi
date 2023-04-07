/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.core.helper;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public final class TextHelper {

	@OnlyIn(Dist.CLIENT)
	public static List<String> renderText(int x, int y, int width, String unlocalizedText, boolean centered, boolean doit, Object... format) {
		PoseStack matrixStack = new PoseStack();
		Font font = Minecraft.getInstance().font;
		String text = I18n.get(unlocalizedText, format);

		String[] textEntries = text.split("<br>");
		List<List<String>> lines = new ArrayList<>();

		String controlCodes;
		for (String s : textEntries) {
			List<String> words = new ArrayList<>();
			String lineStr = "";
			String[] tokens = s.split(" ");
			for (String token : tokens) {
				String prev = lineStr;
				String spaced = token + " ";
				lineStr += spaced;

				controlCodes = toControlCodes(getControlCodes(prev));
				if (font.width(lineStr) > width) {
					lines.add(words);
					lineStr = controlCodes + spaced;
					words = new ArrayList<>();
				}

				words.add(controlCodes + token);
			}

			if (!lineStr.isEmpty()) {
				lines.add(words);
			}
			lines.add(new ArrayList<>());
		}

		List<String> textLines = new ArrayList<>();

		String lastLine = "";
		for (List<String> words : lines) {
			words.size();
			int xi = x;
			int spacing = 4;

			StringBuilder lineStr = new StringBuilder();
			for (String s : words) {
				int extra = 0;

				int swidth = font.width(s);
				if (doit) {
					if (centered) {
						font.draw(matrixStack, s, xi + width / 2 - swidth / 2, y, 0xFFFFFF);
					} else {
						font.draw(matrixStack, s, xi, y, 0xFFFFFF);
					}
				}
				xi += swidth + spacing + extra;
				lineStr.append(s).append(" ");
			}

			if ((lineStr.length() > 0) || lastLine.isEmpty()) {
				y += 10;
				textLines.add(lineStr.toString());
			}
			lastLine = lineStr.toString();
		}
		return textLines;
	}

	public static String getControlCodes(String s) {
		String controls = s.replaceAll("(?<!\u00a7)(.)", "");
		return controls.replaceAll(".*r", "r");
	}

	public static String toControlCodes(String s) {
		return s.replaceAll("(?i)[\\dA-FK-OR]", "\u00a7$0");
	}

}
