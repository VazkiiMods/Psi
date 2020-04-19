/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import com.google.common.base.CaseFormat;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;

import org.lwjgl.opengl.GL11;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.internal.TooltipHelper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
		ITextComponent s = new TranslationTextComponent("psi.datatype." + evalStr);
		if (getPieceType() == EnumPieceType.CONSTANT) {
			s.appendSibling(new StringTextComponent(" ")).appendSibling(new TranslationTextComponent("psimisc.constant"));
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
	 * Defaulted version of getParamValue
	 * Should be used for optional params
	 */
	public <T> T getParamValueOrDefault(SpellContext context, SpellParam<T> param, T def) {
		T v = getParamValue(context, param);
		return v == null ? def : v;
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
	public <T> T getParamValue(SpellContext context, SpellParam<T> param) {
		SpellParam.Side side = paramSides.get(param);
		if (!side.isEnabled()) {
			return null;
		}

		try {
			SpellPiece piece = spell.grid.getPieceAtSideWithRedirections(x, y, side);
			if (piece == null || !param.canAccept(piece)) {
				return null;
			}

			return (T) context.evaluatedObjects[piece.x][piece.y];
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
		return new TranslationTextComponent(getUnlocalizedName()).getFormattedText();
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
			RenderType.State glState = RenderType.State.builder()
					.texture(new RenderState.TextureState(PsiAPI.PSI_PIECE_TEXTURE_ATLAS, false, false))
					.lightmap(new RenderState.LightmapState(true))
					.alpha(new RenderState.AlphaState(0.004F))
					.cull(new RenderState.CullState(false))
					.build(false);
			layer = RenderType.of(PsiAPI.PSI_PIECE_TEXTURE_ATLAS.toString(), DefaultVertexFormats.POSITION_COLOR_TEXTURE_LIGHT, GL11.GL_QUADS, 64, glState);
		}
		return layer;
	}

	/**
	 * Draws this piece's background.
	 */
	@OnlyIn(Dist.CLIENT)
	public void drawBackground(MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		Material material = PsiAPI.getSpellPieceMaterial(registryKey);
		IVertexBuilder buffer = material.getVertexConsumer(buffers, ignored -> getLayer());
		Matrix4f mat = ms.peek().getModel();
		// Cannot call .texture() on the chained object because SpriteAwareVertexBuilder is buggy
		// and does not return itself, it returns the inner buffer
		// This leads to .texture() using the implementation of the inner buffer,
		// not of the SpriteAwareVertexBuilder, which is not what we want.
		// Split the chain apart so that .texture() is called on the original buffer
		buffer.vertex(mat, 0, 16, 0).color(1F, 1F, 1F, 1F);
		buffer.texture(0, 1).light(light).endVertex();

		buffer.vertex(mat, 16, 16, 0).color(1F, 1F, 1F, 1F);
		buffer.texture(1, 1).light(light).endVertex();

		buffer.vertex(mat, 16, 0, 0).color(1F, 1F, 1F, 1F);
		buffer.texture(1, 0).light(light).endVertex();

		buffer.vertex(mat, 0, 0, 0).color(1F, 1F, 1F, 1F);
		buffer.texture(0, 0).light(light).endVertex();
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
			Matrix4f mat = ms.peek().getModel();

			buffer.vertex(mat, -2, 4, 0).color(1F, 1F, 1F, 1F).texture(minU, maxV).light(light).endVertex();
			buffer.vertex(mat, 4, 4, 0).color(1F, 1F, 1F, 1F).texture(maxU, maxV).light(light).endVertex();
			buffer.vertex(mat, 4, -2, 0).color(1F, 1F, 1F, 1F).texture(maxU, minV).light(light).endVertex();
			buffer.vertex(mat, -2, -2, 0).color(1F, 1F, 1F, 1F).texture(minU, minV).light(light).endVertex();
		}
	}

	/**
	 * Draws the parameters coming into this piece.
	 */
	@OnlyIn(Dist.CLIENT)
	public void drawParams(MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		IVertexBuilder buffer = buffers.getBuffer(PsiAPI.internalHandler.getProgrammerLayer());
		for (SpellParam<?> param : paramSides.keySet()) {
			SpellParam.Side side = paramSides.get(param);
			if (side.isEnabled()) {
				int minX = 4;
				int minY = 4;
				minX += side.offx * 9;
				minY += side.offy * 9;

				int maxX = minX + 8;
				int maxY = minY + 8;

				float wh = 8F;
				float minU = side.u / 256F;
				float minV = side.v / 256F;
				float maxU = (side.u + wh) / 256F;
				float maxV = (side.v + wh) / 256F;
				int r = PsiRenderHelper.r(param.color);
				int g = PsiRenderHelper.g(param.color);
				int b = PsiRenderHelper.b(param.color);
				int a = 255;
				Matrix4f mat = ms.peek().getModel();

				buffer.vertex(mat, minX, maxY, 0).color(r, g, b, a).texture(minU, maxV).light(light).endVertex();
				buffer.vertex(mat, maxX, maxY, 0).color(r, g, b, a).texture(maxU, maxV).light(light).endVertex();
				buffer.vertex(mat, maxX, minY, 0).color(r, g, b, a).texture(maxU, minV).light(light).endVertex();
				buffer.vertex(mat, minX, minY, 0).color(r, g, b, a).texture(minU, minV).light(light).endVertex();
			}
		}
	}

	/**
	 * Draws this piece's tooltip.
	 */
	@OnlyIn(Dist.CLIENT)
	public void drawTooltip(int tooltipX, int tooltipY, List<ITextComponent> tooltip) {
		PsiAPI.internalHandler.renderTooltip(tooltipX, tooltipY, tooltip, 0x505000ff, 0xf0100010);
	}

	/**
	 * Draws this piece's comment tooltip.
	 */
	@OnlyIn(Dist.CLIENT)
	public void drawCommentText(int tooltipX, int tooltipY, List<ITextComponent> commentText) {
		PsiAPI.internalHandler.renderTooltip(tooltipX, tooltipY - 9 - commentText.size() * 10, commentText, 0x5000a000, 0xf0001e00);
	}

	@OnlyIn(Dist.CLIENT)
	public void getTooltip(List<ITextComponent> tooltip) {
		tooltip.add(new TranslationTextComponent(getUnlocalizedName()));
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
		tooltip.add(new TranslationTextComponent(getUnlocalizedDesc()).applyTextStyle(TextFormatting.GRAY));

		tooltip.add(new StringTextComponent(""));
		ITextComponent eval = getEvaluationTypeString().applyTextStyle(TextFormatting.GOLD);
		tooltip.add(new StringTextComponent("Output ").appendSibling(eval));

		for (SpellParam<?> param : paramSides.keySet()) {
			ITextComponent pName = new TranslationTextComponent(param.name).applyTextStyle(TextFormatting.YELLOW);
			ITextComponent pEval = new StringTextComponent(" [").appendSibling(param.getRequiredTypeString()).appendText("]").applyTextStyle(TextFormatting.YELLOW);
			tooltip.add(new StringTextComponent(param.canDisable ? "[Input] " : " Input  ").appendSibling(pName).appendSibling(pEval));
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
			rl = new ResourceLocation("psi", key);
			if (PsiAPI.isPieceRegistered(rl)) {
				exists = true;
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
