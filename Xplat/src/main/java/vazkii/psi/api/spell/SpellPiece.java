/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import com.google.common.base.CaseFormat;
import com.mojang.serialization.Codec;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellParam.ArrowType;

import java.util.*;
import java.util.stream.Collectors;

import io.netty.buffer.ByteBuf;

/**
 * A basic abstract piece of a spell. Instances of this class are created as needed
 * by the {@link Spell} object.
 */
public abstract class SpellPiece {

	public static final Spell dummySpell = new Spell();
	private static final String TAG_KEY_LEGACY = "spellKey";
	private static final String TAG_KEY = "key";
	private static final String TAG_PARAMS = "params";
	private static final String TAG_COMMENT = "comment";
	private static final String PSI_PREFIX = "psi.spellparam.";
	public static final Codec<SpellPiece> CODEC = CompoundTag.CODEC.xmap(t -> SpellPiece.createFromNBT(dummySpell, t), p -> {
		var tag = new CompoundTag();
		p.writeToNBT(tag);
		return tag;
	});
	public static final StreamCodec<ByteBuf, SpellPiece> STREAM_CODEC = ByteBufCodecs.COMPOUND_TAG.map(t -> SpellPiece.createFromNBT(dummySpell, t), p -> {
		var tag = new CompoundTag();
		p.writeToNBT(tag);
		return tag;
	});
	public final ResourceLocation registryKey;
	public final Spell spell;
	public final Map<String, SpellParam<?>> params = new LinkedHashMap<>();
	public final Map<SpellParam<?>, SpellParam.Side> paramSides = new LinkedHashMap<>();
	private final Map<EnumSpellStat, StatLabel> statLabels = new HashMap<>();
	public boolean isInGrid = false;
	public int x, y;
	public String comment;

	public SpellPiece(Spell spell) {
		this.spell = spell;
		registryKey = PsiAPI.SPELL_PIECE_REGISTRY.getKey(getClass());
		initParams();
	}

	public static SpellPiece createFromNBT(Spell spell, CompoundTag cmp) {
		String key;
		if(cmp.contains(TAG_KEY_LEGACY)) {
			key = cmp.getString(TAG_KEY_LEGACY);
		} else {
			key = cmp.getString(TAG_KEY);
		}

		if(key.startsWith("_")) {
			key = PSI_PREFIX + key.substring(1);
		}
		try {
			key = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key);
		} catch (Exception e) {
			//Haha yes
		}
		boolean exists = false;
		ResourceLocation rl = ResourceLocation.parse(key);
		if(PsiAPI.SPELL_PIECE_REGISTRY.containsKey(rl)) {
			exists = true;
		} else {
			Set<String> pieceNamespaces = PsiAPI.SPELL_PIECE_REGISTRY.keySet().stream().map(ResourceLocation::getNamespace).collect(Collectors.toSet());
			for(String namespace : pieceNamespaces) {
				rl = ResourceLocation.fromNamespaceAndPath(namespace, key);
				if(PsiAPI.SPELL_PIECE_REGISTRY.containsKey(rl)) {
					exists = true;
					break;
				}
			}
		}

		if(exists) {
			Class<? extends SpellPiece> clazz = PsiAPI.SPELL_PIECE_REGISTRY.get(rl);
			SpellPiece p = create(clazz, spell);
			p.readFromNBT(cmp);
			return p;
		}
		return null;
	}

	public static SpellPiece create(Class<? extends SpellPiece> clazz, Spell spell) {
		try {
			return clazz.getConstructor(Spell.class).newInstance(spell);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static SpellPiece create(ResourceLocation location) {
		return PsiAPI.SPELL_PIECE_REGISTRY.getOptional(location)
				.map(clazz -> SpellPiece.create(clazz, dummySpell))
				.orElse(null);
	}

	/**
	 * Called to init this SpellPiece's {@link #params}. It's recommended you keep all params
	 * registered here as fields in your implementation, as they should be used in {@link #getParamValue(SpellContext,
	 * SpellParam)} or {@link #getParamEvaluation(SpellParam)}.
	 */
	public void initParams() {
		// NO-OP
	}

	/**
	 * Gets what type of piece this is.
	 */
	public abstract EnumPieceType getPieceType();

	/**
	 * Gets what type this piece evaluates as. This is what other pieces
	 * linked to it will read. For example, a number sum operator will return
	 * Double.class, whereas a vector sum operator will return Vector3.class.<br>
	 * If you want this piece to not evaluate to anything (for Tricks, for example),
	 * return {@link Void}.class.
	 */
	public abstract Class<?> getEvaluationType();

	/**
	 * Evaluates this piece for the purpose of spell metadata calculation. If the piece
	 * is not a constant, you can safely return null.
	 */
	public abstract Object evaluate() throws SpellCompilationException;

	/**
	 * Executes this piece and returns the value of this piece for later pieces to pick up
	 * on. For example, the number sum operator would use this function to act upon its parameters
	 * and return the result.
	 */
	public abstract Object execute(SpellContext context) throws SpellRuntimeException;

	/**
	 * Gets the string to be displayed describing this piece's evaluation type.
	 *
	 * @see #getEvaluationType()
	 */
	public Component getEvaluationTypeString() {
		Class<?> evalType = getEvaluationType();
		String evalStr = evalType == null ? "null" : CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, evalType.getSimpleName());
		MutableComponent s = Component.translatable("psi.datatype." + evalStr);
		if(getPieceType() == EnumPieceType.CONSTANT) {
			s.append(" ").append(Component.translatable("psimisc.constant"));
		}

		return s;
	}

	/**
	 * Adds this piece's stats to the Spell's metadata.
	 */
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		// NO-OP
	}

	/**
	 * Adds a {@link SpellParam} to this piece.
	 */
	public void addParam(SpellParam<?> param) {
		params.put(param.name, param);
		paramSides.put(param, SpellParam.Side.OFF);
	}

	/**
	 * Checks whether the piece accepts an input on the given side.
	 * Used by connectors to display output lines.
	 */
	public boolean isInputSide(SpellParam.Side side) {
		return paramSides.containsValue(side);
	}

	/**
	 * Defaulted version of getParamValue
	 * Should be used for optional params
	 */
	public <T> T getParamValueOrDefault(SpellContext context, SpellParam<T> param, T def) {
		try {
			T v = getParamValue(context, param);
			return v == null ? def : v;
		} catch (SpellRuntimeException e) {
			return def;
		}
	}

	/**
	 * Null safe version of getParamValue
	 */
	public <T> T getNotNullParamValue(SpellContext context, SpellParam<T> param) throws SpellRuntimeException {
		T v = getParamValue(context, param);
		if(v == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		}
		return v;
	}

	/**
	 * Gets the value of one of this piece's params in the given context.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getParamValue(SpellContext context, SpellParam<T> param) throws SpellRuntimeException {
		T returnValue = (T) getRawParamValue(context, param);
		if(returnValue instanceof Number number) {
			if(Double.isNaN(number.doubleValue()) || Double.isInfinite(number.doubleValue())) {
				throw new SpellRuntimeException(SpellRuntimeException.NAN, Component.translatable(param.name));
			}
		}
		return returnValue;
	}

	/**
	 * Gets the value of one of this piece's params in the given context.
	 */
	public Object getRawParamValue(SpellContext context, SpellParam<?> param) {
		SpellParam.Side side = paramSides.get(param);
		if(!side.isEnabled()) {
			return null;
		}

		try {
			SpellPiece piece = spell.grid.getPieceAtSideWithRedirections(x, y, side);
			if(piece == null || !param.canAccept(piece)) {
				return null;
			}

			return context.evaluatedObjects[piece.x][piece.y];
		} catch (SpellCompilationException e) {
			return null;
		}
	}

	/**
	 * Defaulted version of getParamEvaluation
	 * Should be used for optional params
	 */
	public <T> T getParamEvaluationeOrDefault(SpellParam<T> param, T def) throws SpellCompilationException {
		T v = getParamEvaluation(param);
		return v == null ? def : v;
	}

	/**
	 * Null safe version of getParamEvaluation()
	 */
	public <T> T getNotNullParamEvaluation(SpellParam<T> param) throws SpellCompilationException {
		T v = getParamEvaluation(param);
		if(v == null) {
			throw new SpellCompilationException(SpellCompilationException.NULL_PARAM, this.x, this.y);
		}
		return v;
	}

	/**
	 * Gets the evaluation of one of this piece's params in the given context. This calls
	 * {@link #evaluate()} and should only be used for {@link #addToMetadata(SpellMetadata)}
	 */
	@SuppressWarnings("unchecked")
	public <T> T getParamEvaluation(SpellParam<?> param) throws SpellCompilationException {
		SpellParam.Side side = paramSides.get(param);
		if(!side.isEnabled()) {
			return null;
		}

		SpellPiece piece = spell.grid.getPieceAtSideWithRedirections(x, y, side);

		if(piece == null || !param.canAccept(piece)) {
			return null;
		}

		return (T) piece.evaluate();
	}

	public String getUnlocalizedName() {
		return registryKey.getNamespace() + ".spellpiece." + registryKey.getPath();
	}

	public String getSortingName() {
		return Component.translatable(getUnlocalizedName()).getString();
	}

	public String getUnlocalizedDesc() {
		return registryKey.getNamespace() + ".spellpiece." + registryKey.getPath() + ".desc";
	}

	/**
	 * Sets a {@link StatLabel}'s value.
	 */
	public void setStatLabel(EnumSpellStat type, StatLabel descriptor) {
		if(descriptor == null) {
			statLabels.remove(type);
		} else {
			statLabels.put(type, descriptor);
		}
	}

	public int getParamArrowCount(SpellParam.Side side) {
		int count = 0;
		for(SpellParam<?> p : paramSides.keySet()) {
			if(p.getArrowType() != ArrowType.NONE && paramSides.get(p) == side) {
				count++;
			}
		}
		return count;
	}

	public int getParamArrowIndex(SpellParam<?> param) {
		SpellParam.Side side = paramSides.get(param);
		int count = 0;
		for(SpellParam<?> p : paramSides.keySet()) {
			if(p == param) {
				return count;
			}
			if(p.getArrowType() != ArrowType.NONE && paramSides.get(p) == side) {
				count++;
			}
		}
		return 0;
	}

	public Map<EnumSpellStat, StatLabel> getStatLabels() {
		return Collections.unmodifiableMap(statLabels);
	}

	/**
	 * Checks whether this piece should intercept keystrokes in the programmer interface.
	 * This is used for the number constant piece to change its value.
	 */
	public boolean interceptKeystrokes() {
		return false;
	}

	/**
	 * Due to changes on LWJGL, it is no longer easily possible to get a key from a keycode.
	 * It is technically possible but it is unadvisable.
	 */

	public boolean onCharTyped(char character, int keyCode, boolean doit) {
		return false;
	}

	public boolean onKeyPressed(int keyCode, int scanCode, boolean doit) {
		return false;
	}

	public boolean hasConfig() {
		return !params.isEmpty();
	}

	public void getShownPieces(List<SpellPiece> pieces) {
		pieces.add(this);
	}

	public SpellPiece copy() {
		CompoundTag cmp = new CompoundTag();
		writeToNBT(cmp);
		return createFromNBT(spell, cmp);
	}

	public SpellPiece copyFromSpell(Spell spell) {
		CompoundTag cmp = new CompoundTag();
		writeToNBT(cmp);
		return createFromNBT(spell, cmp);
	}

	public void readFromNBT(CompoundTag cmp) {
		CompoundTag paramCmp = cmp.getCompound(TAG_PARAMS);
		for(String s : params.keySet()) {
			SpellParam<?> param = params.get(s);

			String key = s;
			if(paramCmp.contains(key)) {
				paramSides.put(param, SpellParam.Side.fromInt(paramCmp.getInt(key)));
			} else {
				if(key.startsWith(SpellParam.PSI_PREFIX)) {
					key = "_" + key.substring(SpellParam.PSI_PREFIX.length());
				}
				paramSides.put(param, SpellParam.Side.fromInt(paramCmp.getInt(key)));
			}
		}

		comment = cmp.getString(TAG_COMMENT);
	}

	public void writeToNBT(CompoundTag cmp) {
		if(comment == null) {
			comment = "";
		}

		cmp.putString(TAG_KEY, registryKey.toString().replaceAll("^" + PSI_PREFIX, "_"));

		int paramCount = 0;
		CompoundTag paramCmp = new CompoundTag();
		for(String s : params.keySet()) {
			SpellParam<?> param = params.get(s);
			SpellParam.Side side = paramSides.get(param);
			paramCmp.putInt(s.replaceAll("^" + SpellParam.PSI_PREFIX, "_"), side.asInt());
			paramCount++;
		}

		if(paramCount > 0) {
			cmp.put(TAG_PARAMS, paramCmp);
		}
		if(!comment.isEmpty()) {
			cmp.putString(TAG_COMMENT, comment);
		}
	}
}
