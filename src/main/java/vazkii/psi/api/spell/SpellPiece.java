/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import com.google.common.base.CaseFormat;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;

import org.lwjgl.opengl.GL11;

import vazkii.psi.api.ClientPsiAPI;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.spell.SpellParam.ArrowType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A basic abstract piece of a spell. Instances of this class are created as needed
 * by the {@link Spell} object.
 */
public abstract class SpellPiece {

	@OnlyIn(Dist.CLIENT)
	private static RenderType layer;
	private static final String TAG_KEY_LEGACY = "spellKey";

	private static final String TAG_KEY = "key";
	private static final String TAG_PARAMS = "params";
	private static final String TAG_COMMENT = "comment";

	private static final String PSI_PREFIX = "psi.spellparam.";

	public final ResourceLocation registryKey;
	public final Spell spell;

	public boolean isInGrid = false;
	public int x, y;
	public String comment;

	public final Map<String, SpellParam<?>> params = new LinkedHashMap<>();
	public final Map<SpellParam<?>, SpellParam.Side> paramSides = new LinkedHashMap<>();

	public SpellPiece(Spell spell) {
		this.spell = spell;
		registryKey = PsiAPI.getSpellPieceKey(getClass());
		initParams();
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
	 * Gets what type this piece evaluates as. Use for parameter dependent evaluation
	 * types to avoid infinite loops. Only called for pieces in a spell grid.
	 */
	public Class<?> getEvaluationType(Set<SpellPiece> visited) {
		return getEvaluationType();
	}

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
	public ITextComponent getEvaluationTypeString() {
		Class<?> evalType = getEvaluationType();
		String evalStr = evalType == null ? "null" : CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, evalType.getSimpleName());
		TranslationTextComponent s = new TranslationTextComponent("psi.datatype." + evalStr);
		if (getPieceType() == EnumPieceType.CONSTANT) {
			s.append(new StringTextComponent(" ")).append(new TranslationTextComponent("psimisc.constant"));
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
	 * Adds this piece's stats to the Spell's metadata.
	 * Also called on pieces not referenced anywhere in the spell.
	 */
	public void addModifierToMetadata(SpellMetadata meta) throws SpellCompilationException {
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
	public <T> T getParamValueOrDefault(SpellContext context, SpellParam<T> param, T def) throws SpellRuntimeException {
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
	public <T> T getNonnullParamValue(SpellContext context, SpellParam<T> param) throws SpellRuntimeException {
		T v = getParamValue(context, param);
		if (v == null) {
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
		if (returnValue instanceof Number) {
			Number number = (Number) returnValue;
			if (Double.isNaN(number.doubleValue()) || Double.isInfinite(number.doubleValue())) {
				throw new SpellRuntimeException(SpellRuntimeException.NAN);
			}
		}
		return returnValue;
	}

	/**
	 * Gets the value of one of this piece's params in the given context.
	 */
	public Object getRawParamValue(SpellContext context, SpellParam<?> param) {
		SpellParam.Side side = paramSides.get(param);
		if (!side.isEnabled()) {
			return null;
		}

		try {
			SpellPiece piece = spell.grid.getPieceAtSideWithRedirections(x, y, side);
			if (piece == null || !param.canAccept(piece)) {
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
	public <T> T getNonNullParamEvaluation(SpellParam<T> param) throws SpellCompilationException {
		T v = getParamEvaluation(param);
		if (v == null) {
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
		if (!side.isEnabled()) {
			return null;
		}

		SpellPiece piece = spell.grid.getPieceAtSideWithRedirections(x, y, side);

		if (piece == null || !param.canAccept(piece)) {
			return null;
		}

		return (T) piece.evaluate();
	}

	public String getUnlocalizedName() {
		return registryKey.getNamespace() + ".spellpiece." + registryKey.getPath();
	}

	public String getSortingName() {
		return new TranslationTextComponent(getUnlocalizedName()).getString();
	}

	public String getUnlocalizedDesc() {
		return registryKey.getNamespace() + ".spellpiece." + registryKey.getPath() + ".desc";
	}

	/**
	 * Draws this piece onto the programmer GUI or the programmer TE projection.<br>
	 * All appropriate transformations are already done. Canvas is 16x16 starting from (0, 0, 0).<br>
	 * To avoid z-fighting in the TE projection, translations are applied every step.
	 */
	@OnlyIn(Dist.CLIENT)
	public void draw(MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		ms.push();
		drawBackground(ms, buffers, light);
		ms.translate(0F, 0F, 0.1F);
		drawAdditional(ms, buffers, light);
		if (isInGrid) {
			ms.translate(0F, 0F, 0.1F);
			drawParams(ms, buffers, light);
			ms.translate(0F, 0F, 0.1F);
			drawComment(ms, buffers, light);
		}

		ms.pop();
	}

	@OnlyIn(Dist.CLIENT)
	public static RenderType getLayer() {
		if (layer == null) {
			RenderType.State glState = RenderType.State.getBuilder()
					.texture(new RenderState.TextureState(ClientPsiAPI.PSI_PIECE_TEXTURE_ATLAS, false, false))
					.lightmap(new RenderState.LightmapState(true))
					.alpha(new RenderState.AlphaState(0.004F))
					.cull(new RenderState.CullState(false))
					.build(false);
			layer = RenderType.makeType(ClientPsiAPI.PSI_PIECE_TEXTURE_ATLAS.toString(), DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, GL11.GL_QUADS, 64, glState);
		}
		return layer;
	}

	/**
	 * Draws this piece's background.
	 */
	@OnlyIn(Dist.CLIENT)
	public void drawBackground(MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		RenderMaterial material = ClientPsiAPI.getSpellPieceMaterial(registryKey);
		IVertexBuilder buffer = material.getBuffer(buffers, ignored -> getLayer());
		Matrix4f mat = ms.getLast().getMatrix();
		// Cannot call .texture() on the chained object because SpriteAwareVertexBuilder is buggy
		// and does not return itself, it returns the inner buffer
		// This leads to .texture() using the implementation of the inner buffer,
		// not of the SpriteAwareVertexBuilder, which is not what we want.
		// Split the chain apart so that .texture() is called on the original buffer
		buffer.pos(mat, 0, 16, 0).color(1F, 1F, 1F, 1F);
		buffer.tex(0, 1).lightmap(light).endVertex();

		buffer.pos(mat, 16, 16, 0).color(1F, 1F, 1F, 1F);
		buffer.tex(1, 1).lightmap(light).endVertex();

		buffer.pos(mat, 16, 0, 0).color(1F, 1F, 1F, 1F);
		buffer.tex(1, 0).lightmap(light).endVertex();

		buffer.pos(mat, 0, 0, 0).color(1F, 1F, 1F, 1F);
		buffer.tex(0, 0).lightmap(light).endVertex();
	}

	/**
	 * Draws any additional stuff for this piece. Used in connectors
	 * to draw the lines.
	 */
	@OnlyIn(Dist.CLIENT)
	public void drawAdditional(MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		// NO-OP
	}

	/**
	 * Draws the little comment indicator in this piece, if one exists.
	 */
	@OnlyIn(Dist.CLIENT)
	public void drawComment(MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		if (comment != null && !comment.isEmpty()) {
			IVertexBuilder buffer = buffers.getBuffer(PsiAPI.internalHandler.getProgrammerLayer());

			float wh = 6F;
			float minU = 150 / 256F;
			float minV = 184 / 256F;
			float maxU = (150 + wh) / 256F;
			float maxV = (184 + wh) / 256F;
			Matrix4f mat = ms.getLast().getMatrix();

			buffer.pos(mat, -2, 4, 0).color(1F, 1F, 1F, 1F).tex(minU, maxV).lightmap(light).endVertex();
			buffer.pos(mat, 4, 4, 0).color(1F, 1F, 1F, 1F).tex(maxU, maxV).lightmap(light).endVertex();
			buffer.pos(mat, 4, -2, 0).color(1F, 1F, 1F, 1F).tex(maxU, minV).lightmap(light).endVertex();
			buffer.pos(mat, -2, -2, 0).color(1F, 1F, 1F, 1F).tex(minU, minV).lightmap(light).endVertex();
		}
	}

	/**
	 * Draws the parameters coming into this piece.
	 */
	@OnlyIn(Dist.CLIENT)
	public void drawParams(MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		IVertexBuilder buffer = buffers.getBuffer(PsiAPI.internalHandler.getProgrammerLayer());
		for (SpellParam<?> param : paramSides.keySet()) {
			drawParam(ms, buffer, light, param);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void drawParam(MatrixStack ms, IVertexBuilder buffer, int light, SpellParam<?> param) {
		SpellParam.Side side = paramSides.get(param);
		if (!side.isEnabled() || param.getArrowType() == ArrowType.NONE) {
			return;
		}

		int index = 0, count = 0;
		for (SpellParam<?> p : paramSides.keySet()) {
			if (p == param) {
				index = count;
			}
			if (p.getArrowType() != ArrowType.NONE && paramSides.get(p) == side) {
				count++;
			}
		}
		SpellPiece neighbour = spell.grid.getPieceAtSideSafely(x, y, side);
		if (neighbour != null) {
			int nbcount = 0;
			for (SpellParam<?> p : neighbour.paramSides.keySet()) {
				if (p.getArrowType() != ArrowType.NONE && neighbour.paramSides.get(p) == side.getOpposite()) {
					nbcount++;
				}
			}
			if (side.asInt() > side.getOpposite().asInt()) {
				index += nbcount;
			}
			count += nbcount;
		}

		float minX = 4;
		float minY = 4;
		if (count == 1) {
			minX += (side.minx + side.maxx) / 2;
			minY += (side.miny + side.maxy) / 2;
		} else {
			float percent = (float) index / (count - 1);
			minX += side.minx * percent + side.maxx * (1 - percent);
			minY += side.miny * percent + side.maxy * (1 - percent);
		}

		float maxX = minX + 8;
		float maxY = minY + 8;

		if (param.getArrowType() == ArrowType.OUT) {
			side = side.getOpposite();
		}
		float wh = 8F;
		float minU = side.u / 256F;
		float minV = side.v / 256F;
		float maxU = (side.u + wh) / 256F;
		float maxV = (side.v + wh) / 256F;
		int r = PsiRenderHelper.r(param.color);
		int g = PsiRenderHelper.g(param.color);
		int b = PsiRenderHelper.b(param.color);
		int a = 255;
		Matrix4f mat = ms.getLast().getMatrix();

		buffer.pos(mat, minX, maxY, 0).color(r, g, b, a).tex(minU, maxV).lightmap(light).endVertex();
		buffer.pos(mat, maxX, maxY, 0).color(r, g, b, a).tex(maxU, maxV).lightmap(light).endVertex();
		buffer.pos(mat, maxX, minY, 0).color(r, g, b, a).tex(maxU, minV).lightmap(light).endVertex();
		buffer.pos(mat, minX, minY, 0).color(r, g, b, a).tex(minU, minV).lightmap(light).endVertex();
	}

	/**
	 * Draws this piece's tooltip.
	 */
	@OnlyIn(Dist.CLIENT)
	public void drawTooltip(MatrixStack ms, int tooltipX, int tooltipY, List<ITextComponent> tooltip, Screen screen) {
		PsiAPI.internalHandler.renderTooltip(ms, tooltipX, tooltipY, tooltip, 0x505000ff, 0xf0100010, screen.width, screen.height);
	}

	/**
	 * Draws this piece's comment tooltip.
	 */
	@OnlyIn(Dist.CLIENT)
	public void drawCommentText(MatrixStack ms, int tooltipX, int tooltipY, List<ITextComponent> commentText, Screen screen) {
		PsiAPI.internalHandler.renderTooltip(ms, tooltipX, tooltipY - 9 - commentText.size() * 10, commentText, 0x5000a000, 0xf0001e00, screen.width, screen.height);
	}

	@OnlyIn(Dist.CLIENT)
	public void getTooltip(List<ITextComponent> tooltip) {
		tooltip.add(new TranslationTextComponent(getUnlocalizedName()));
		tooltip.add(new TranslationTextComponent(getUnlocalizedDesc()).mergeStyle(TextFormatting.GRAY));
		TooltipHelper.tooltipIfShift(tooltip, () -> addToTooltipAfterShift(tooltip));

		String addon = registryKey.getNamespace();
		if (!addon.equals("psi")) {

			if (ModList.get().getModContainerById(addon).isPresent()) {
				tooltip.add(new TranslationTextComponent("psimisc.provider_mod", ModList.get().getModContainerById(addon).get().getNamespace()));
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void addToTooltipAfterShift(List<ITextComponent> tooltip) {
		tooltip.add(new StringTextComponent(""));
		IFormattableTextComponent eval = getEvaluationTypeString().copyRaw().mergeStyle(TextFormatting.GOLD);
		tooltip.add(new StringTextComponent("Output ").append(eval));

		for (SpellParam<?> param : paramSides.keySet()) {
			ITextComponent pName = new TranslationTextComponent(param.name).mergeStyle(TextFormatting.YELLOW);
			ITextComponent pEval = new StringTextComponent(" [").append(param.getRequiredTypeString()).appendString("]").mergeStyle(TextFormatting.YELLOW);
			tooltip.add(new StringTextComponent(param.canDisable ? "[Input] " : " Input  ").append(pName).append(pEval));
		}
	}

	/**
	 * Checks whether this piece should intercept keystrokes in the programmer interface.
	 * This is used for the number constant piece to change its value.
	 */
	@OnlyIn(Dist.CLIENT)
	public boolean interceptKeystrokes() {
		return false;
	}

	/**
	 * Due to changes on LWJGL, it is no longer easily possible to get a key from a keycode.
	 * It is technically possible but it is unadvisable.
	 */

	@OnlyIn(Dist.CLIENT)
	public boolean onCharTyped(char character, int keyCode, boolean doit) {
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public boolean onKeyPressed(int keyCode, int scanCode, boolean doit) {
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public boolean hasConfig() {
		return !params.isEmpty();
	}

	@OnlyIn(Dist.CLIENT)
	public void getShownPieces(List<SpellPiece> pieces) {
		pieces.add(this);
	}

	public static SpellPiece createFromNBT(Spell spell, CompoundNBT cmp) {
		String key;
		if (cmp.contains(TAG_KEY_LEGACY)) {
			key = cmp.getString(TAG_KEY_LEGACY);
		} else {
			key = cmp.getString(TAG_KEY);
		}

		if (key.startsWith("_")) {
			key = PSI_PREFIX + key.substring(1);
		}
		try {
			key = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key);
		} catch (Exception e) {
			//Haha yes
		}
		boolean exists = false;
		ResourceLocation rl = new ResourceLocation(key);
		if (PsiAPI.isPieceRegistered(rl)) {
			exists = true;
		} else {
			Set<String> pieceNamespaces = PsiAPI.getSpellPieceRegistry().keySet().stream().map(ResourceLocation::getNamespace).collect(Collectors.toSet());
			for (String namespace : pieceNamespaces) {
				rl = new ResourceLocation(namespace, key);
				if (PsiAPI.isPieceRegistered(rl)) {
					exists = true;
					break;
				}
			}
		}

		if (exists) {
			Class<? extends SpellPiece> clazz = PsiAPI.getSpellPiece(rl);
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

	public SpellPiece copy() {
		CompoundNBT cmp = new CompoundNBT();
		writeToNBT(cmp);
		return createFromNBT(spell, cmp);
	}

	public SpellPiece copyFromSpell(Spell spell) {
		CompoundNBT cmp = new CompoundNBT();
		writeToNBT(cmp);
		return createFromNBT(spell, cmp);
	}

	public void readFromNBT(CompoundNBT cmp) {
		CompoundNBT paramCmp = cmp.getCompound(TAG_PARAMS);
		for (String s : params.keySet()) {
			SpellParam<?> param = params.get(s);

			String key = s;
			if (paramCmp.contains(key)) {
				paramSides.put(param, SpellParam.Side.fromInt(paramCmp.getInt(key)));
			} else {
				if (key.startsWith(SpellParam.PSI_PREFIX)) {
					key = "_" + key.substring(SpellParam.PSI_PREFIX.length());
				}
				paramSides.put(param, SpellParam.Side.fromInt(paramCmp.getInt(key)));
			}
		}

		comment = cmp.getString(TAG_COMMENT);
	}

	public void writeToNBT(CompoundNBT cmp) {
		if (comment == null) {
			comment = "";
		}

		cmp.putString(TAG_KEY, registryKey.toString().replaceAll("^" + PSI_PREFIX, "_"));

		int paramCount = 0;
		CompoundNBT paramCmp = new CompoundNBT();
		for (String s : params.keySet()) {
			SpellParam<?> param = params.get(s);
			SpellParam.Side side = paramSides.get(param);
			paramCmp.putInt(s.replaceAll("^" + SpellParam.PSI_PREFIX, "_"), side.asInt());
			paramCount++;
		}

		if (paramCount > 0) {
			cmp.put(TAG_PARAMS, paramCmp);
		}
		if (!comment.isEmpty()) {
			cmp.putString(TAG_COMMENT, comment);
		}
	}

}
