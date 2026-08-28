/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import net.minecraft.network.chat.Component;

public class StatLabel {
	private StringBuilder tooltip;
	private byte order;

	public StatLabel(double value) {
		init();
		append(formatDouble(value));
	}

	public StatLabel(String value, boolean translate) {
		init();
		append(translate ? translate(value) : value);
	}

	public StatLabel(String value) {
		init();
		append(value);
	}

	private void init() {
		tooltip = new StringBuilder();
		order = 3;
	}

	@Override
	public String toString() {
		return tooltip.toString();
	}

	public StatLabel plus() {
		order = 1;
		return append(" + ");
	}

	public StatLabel add(String value, boolean translate) {
		return plus().append(translate ? translate(value) : value);
	}

	public StatLabel add(String value) {
		return plus().append(value);
	}

	public StatLabel add(StatLabel other) {
		return plus().append(other);
	}

	public StatLabel add(double value) {
		return plus().append(formatDouble(value));
	}

	public StatLabel minus() {
		order = 1;
		return append(" - ");
	}

	public StatLabel sub(String value, boolean translate) {
		return minus().append(translate ? translate(value) : value);
	}

	public StatLabel sub(String value) {
		return minus().append(value);
	}

	public StatLabel sub(StatLabel other) {
		return minus().append(other);
	}

	public StatLabel sub(double value) {
		return minus().append(formatDouble(value));
	}

	public StatLabel times() {
		if(order < 2) {
			parenthesize();
		}
		order = 2;
		return append(" × ");
	}

	public StatLabel mul(String value, boolean translate) {
		return times().append(translate ? translate(value) : value);
	}

	public StatLabel mul(String value) {
		return times().append(value);
	}

	public StatLabel mul(StatLabel other) {
		return times().append(other);
	}

	public StatLabel mul(double value) {
		return times().append(formatDouble(value));
	}

	public StatLabel div() {
		if(order < 2) {
			parenthesize();
		}
		order = 2;
		return append(" ÷ ");
	}

	public StatLabel div(String value, boolean translate) {
		return div().append(translate ? translate(value) : value);
	}

	public StatLabel div(String value) {
		return div().append(value);
	}

	public StatLabel div(StatLabel other) {
		return div().append(other);
	}

	public StatLabel div(double value) {
		return div().append(formatDouble(value));
	}

	public StatLabel floor() {
		return prepend("⌊").append("⌋");
	}

	public StatLabel ceil() {
		return prepend("⌈").append("⌉");
	}

	public StatLabel round() {
		return prepend("⌊").append("⌉");
	}

	public StatLabel min(double value) {
		return prepend("min(").append(", ").append(formatDouble(value)).append(")");
	}

	public StatLabel min(StatLabel other) {
		prepend("min(").append(", ").append(other).append(")");
		return this;
	}

	public StatLabel max(double value) {
		return prepend("max(").append(", ").append(formatDouble(value)).append(")");
	}

	public StatLabel max(StatLabel other) {
		return prepend("max(").append(", ").append(other).append(")");
	}

	public StatLabel abs() {
		return prepend("|").append("|");
	}

	public StatLabel parenthesize() {
		order = 3;
		return prepend("(").append(")");
	}

	public StatLabel square() {
		return append("²");
	}

	public StatLabel cube() {
		return append("³");
	}

	public StatLabel pow(String str, boolean translate) {
		return append("^(").append(translate ? translate(str) : str).append(")");
	}

	public StatLabel pow(String str) {
		return append("^(").append(str).append(")");
	}

	public StatLabel pow(StatLabel other) {
		return append("^(").append(other).append(")");
	}

	public StatLabel pow(double value) {
		return append("^(").append(formatDouble(value)).append(")");
	}

	public StatLabel sqrt() {
		return parenthesize().prepend("√");
	}

	public StatLabel prepend(String str) {
		this.tooltip.insert(0, str);
		return this;
	}

	public StatLabel prepend(String str, boolean translate) {
		return translate ? prepend(translate(str)) : prepend(str);
	}

	public StatLabel prepend(char c) {
		this.tooltip.insert(0, c);
		return this;
	}

	public StatLabel prepend(double d) {
		return prepend(formatDouble(d));
	}

	public StatLabel prepend(StatLabel other) {
		this.tooltip.insert(0, other.tooltip);
		return this;
	}

	public StatLabel append(String str) {
		this.tooltip.append(str);
		return this;
	}

	public StatLabel append(String str, boolean translate) {
		return translate ? append(translate(str)) : append(str);
	}

	public StatLabel append(char c) {
		this.tooltip.append(c);
		return this;
	}

	public StatLabel append(StatLabel other) {
		this.tooltip.append(other.tooltip);
		return this;
	}

	public StatLabel append(double d) {
		return append(formatDouble(d));
	}

	private String translate(String str) {
		return Component.translatable(str).getString();
	}

	private String formatDouble(double value) {
		String s = String.valueOf(value);
		if(s.endsWith(".0")) {
			s = s.substring(0, s.length() - 2);
		}
		return s;
	}
}
