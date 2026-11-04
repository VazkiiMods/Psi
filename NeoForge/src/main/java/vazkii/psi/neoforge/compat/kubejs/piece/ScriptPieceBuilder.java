/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.piece;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.spell.SpellPieceType;

import java.util.ArrayList;
import java.util.List;

import dev.latvian.mods.kubejs.client.LangKubeEvent;
import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.rhino.util.ReturnsSelf;

/**
 * Common ground of every script piece kind. Every builder call is mandatory; registration fails
 * naming whichever calls are missing.
 */
@ReturnsSelf
public abstract class ScriptPieceBuilder extends BuilderBase<SpellPieceType> {

	private Integer complexity;
	private Integer potency;
	private Integer cost;
	private Integer projection;
	private Integer bandwidth;
	private String description;

	protected ScriptPieceBuilder(ResourceLocation id) {
		super(id);
	}

	public ScriptPieceBuilder complexity(int value) {
		complexity = value;
		return this;
	}

	public ScriptPieceBuilder potency(int value) {
		potency = value;
		return this;
	}

	public ScriptPieceBuilder cost(int value) {
		cost = value;
		return this;
	}

	public ScriptPieceBuilder projection(int value) {
		projection = value;
		return this;
	}

	public ScriptPieceBuilder bandwidth(int value) {
		bandwidth = value;
		return this;
	}

	public ScriptPieceBuilder description(String value) {
		description = value;
		return this;
	}

	@Override
	public String getBuilderTranslationKey() {
		return id.getNamespace() + ".spellpiece." + id.getPath();
	}

	@Override
	public void generateLang(LangKubeEvent event) {
		super.generateLang(event);
		if(description == null) {
			return;
		}
		event.add(id.getNamespace(), getBuilderTranslationKey() + ".desc", description);
	}

	@Override
	public final SpellPieceType createObject() {
		List<String> missing = new ArrayList<>();
		require(missing, "complexity", complexity);
		require(missing, "potency", potency);
		require(missing, "cost", cost);
		require(missing, "projection", projection);
		require(missing, "bandwidth", bandwidth);
		collectMissing(missing);
		if(!missing.isEmpty()) {
			throw new KubeRuntimeException("Spell piece '" + id + "' is missing mandatory builder calls: " + String.join(", ", missing)).source(sourceLine);
		}

		return build(ScriptPieceStats.of(complexity, potency, cost, projection, bandwidth));
	}

	protected static void require(List<String> missing, String name, @Nullable Object value) {
		if(value == null) {
			missing.add(name);
		}
	}

	protected abstract void collectMissing(List<String> missing);

	protected abstract SpellPieceType build(ScriptPieceStats stats);

}
