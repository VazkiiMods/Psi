/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 15:19:22 (GMT)]
 */
package vazkii.psi.api.spell;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

	private static final String TAG_KEY_LEGACY = "spellKey";
	
	private static final String TAG_KEY = "key";
	private static final String TAG_PARAMS = "params";
	private static final String TAG_COMMENT = "comment";

	private static final String PSI_PREFIX = "psi.spellparam.";
	
	public final String registryKey;
	public final Spell spell;

	public boolean isInGrid = false;
	public int x, y;
	public String comment;
	
	public final Map<String, SpellParam> params = new LinkedHashMap<>();
	public final Map<SpellParam, SpellParam.Dist> paramSides = new LinkedHashMap<>();

	public SpellPiece(Spell spell) {
		this.spell = spell;
		registryKey = PsiAPI.spellPieceRegistry.getNameForObject(getClass());
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
	 * return {@link Null}.class.
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
	 * @see #getEvaluationType()
	 */
	public String getEvaluationTypeString() {
		Class<?> evalType = getEvaluationType();
		String evalStr = evalType == null ? "Null" : evalType.getSimpleName();
		String s = TooltipHelper.local("psi.datatype." + evalStr);
		if(getPieceType() == EnumPieceType.CONSTANT)
			s += " " + TooltipHelper.local("psimisc.constant");

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
	public void addParam(SpellParam param) {
		params.put(param.name, param);
		paramSides.put(param, SpellParam.Dist.OFF);
	}

	/**
	 * Gets the value of one of this piece's params in the given context.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getParamValue(SpellContext context, SpellParam param) {
		SpellParam.Dist side = paramSides.get(param);
		if(!side.isEnabled())
			return null;

		try {
			SpellPiece piece = spell.grid.getPieceAtSideWithRedirections(x, y, side);
			if(piece == null || !param.canAccept(piece))
				return null;

			return (T) context.evaluatedObjects[piece.x][piece.y];
		} catch(SpellCompilationException e) {
			return null;
		}
	}

	/**
	 * Gets the evaluation of one of this piece's params in the given context. This calls
	 * {@link #evaluate()} and should only be used for {@link #addToMetadata(SpellMetadata)}
	 */
	@SuppressWarnings("unchecked")
	public <T> T getParamEvaluation(SpellParam param) throws SpellCompilationException {
		SpellParam.Dist side = paramSides.get(param);
		if(!side.isEnabled())
			return null;

		SpellPiece piece = spell.grid.getPieceAtSideWithRedirections(x, y, side);

		if(piece == null || !param.canAccept(piece))
			return null;

		return (T) piece.evaluate();
	}

	public String getUnlocalizedName() {
		return "psi.spellpiece." + registryKey;
	}

	public String getSortingName() {
		return TooltipHelper.local(getUnlocalizedName());
	}

	public String getUnlocalizedDesc() {
		return "psi.spellpiece." + registryKey + ".desc";
	}

	/**
	 * Draws this piece onto the programmer GUI or the programmer TE projection.<br>
	 * All appropriate transformations are already done. Canvas is 16x16 starting from (0, 0, 0).<br>
	 * To avoid z-fighting in the TE projection, translations are applied every step.
	 */
	@OnlyIn(Dist.CLIENT)
	public void draw() {
		drawBackground();
		GlStateManager.translate(0F, 0F, 0.1F);
		drawAdditional();
		if(isInGrid) {
			GlStateManager.translate(0F, 0F, 0.1F);
			drawParams();
			GlStateManager.translate(0F, 0F, 0.1F);
			drawComment();
		}

		GlStateManager.color(1F, 1F, 1F);
	}

	/**
	 * Draws this piece's background.
	 * @see #draw()
	 */
	@OnlyIn(Dist.CLIENT)
	public void drawBackground() {
		ResourceLocation res = PsiAPI.simpleSpellTextures.get(registryKey);
		Minecraft.getMinecraft().renderEngine.bindTexture(res);

		GlStateManager.color(1F, 1F, 1F);
		BufferBuilder wr = Tessellator.getInstance().getBuffer();
		wr.begin(7, DefaultVertexFormats.POSITION_TEX);
		wr.pos(0, 16, 0).tex(0, 1).endVertex();
		wr.pos(16, 16, 0).tex(1, 1).endVertex();
		wr.pos(16, 0, 0).tex(1, 0).endVertex();
		wr.pos(0, 0, 0).tex(0, 0).endVertex();
		Tessellator.getInstance().draw();
	}
	
	/**
	 * Draws any additional stuff for this piece. Used in connectors
	 * to draw the lines.
	 * @see #draw()
	 */
	@OnlyIn(Dist.CLIENT)
	public void drawAdditional() {
		// NO-OP
	}
	
	/**
	 * Draws the little comment indicator in this piece, if one exists.
	 * @see #draw()
	 */
	@OnlyIn(Dist.CLIENT)
	public void drawComment() {
		if(comment != null && !comment.isEmpty()) {
			Minecraft.getMinecraft().renderEngine.bindTexture(PsiAPI.internalHandler.getProgrammerTexture());
			
			float wh = 6F;
			float minU = 150 / 256F;
			float minV = 184 / 256F;
			float maxU = (150 + wh) / 256F;
			float maxV = (184 + wh) / 256F;
			GlStateManager.color(1F, 1F, 1F, 1F);

			BufferBuilder wr = Tessellator.getInstance().getBuffer();
			wr.begin(7, DefaultVertexFormats.POSITION_TEX);
			wr.pos(-2, 4, 0).tex(minU, maxV).endVertex();
			wr.pos(4, 4, 0).tex(maxU, maxV).endVertex();
			wr.pos(4, -2, 0).tex(maxU, minV).endVertex();
			wr.pos(-2, -2, 0).tex(minU, minV).endVertex();
			Tessellator.getInstance().draw();
		}
	}
	
	/**
	 * Draws the parameters coming into this piece.
	 * @see #draw()
	 */
	@OnlyIn(Dist.CLIENT)
	public void drawParams() {
		Minecraft.getMinecraft().renderEngine.bindTexture(PsiAPI.internalHandler.getProgrammerTexture());
		GlStateManager.enableAlpha();
		for(SpellParam param : paramSides.keySet()) {
			SpellParam.Dist side = paramSides.get(param);
			if(side.isEnabled()) {
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
				GlStateManager.color(PsiRenderHelper.r(param.color) / 255F,
						PsiRenderHelper.g(param.color) / 255F,
						PsiRenderHelper.b(param.color) / 255F, 1F);

				BufferBuilder wr = Tessellator.getInstance().getBuffer();
				wr.begin(7, DefaultVertexFormats.POSITION_TEX);
				wr.pos(minX, maxY, 0).tex(minU, maxV).endVertex();
				wr.pos(maxX, maxY, 0).tex(maxU, maxV).endVertex();
				wr.pos(maxX, minY, 0).tex(maxU, minV).endVertex();
				wr.pos(minX, minY, 0).tex(minU, minV).endVertex();
				Tessellator.getInstance().draw();
			}
		}
	}

	/**
	 * Draws this piece's tooltip.
	 */
	@OnlyIn(Dist.CLIENT)
	public void drawTooltip(int tooltipX, int tooltipY, List<String> tooltip) {
		PsiAPI.internalHandler.renderTooltip(tooltipX, tooltipY, tooltip, 0x505000ff, 0xf0100010);
	}

	/**
	 * Draws this piece's comment tooltip.
	 */
	@OnlyIn(Dist.CLIENT)
	public void drawCommentText(int tooltipX, int tooltipY, List<String> commentText) {
		PsiAPI.internalHandler.renderTooltip(tooltipX, tooltipY - 9 - commentText.size() * 10, commentText, 0x5000a000, 0xf0001e00);
	}

	@OnlyIn(Dist.CLIENT)
	public void getTooltip(List<String> tooltip) {
		TooltipHelper.addToTooltip(tooltip, getUnlocalizedName());
		TooltipHelper.tooltipIfShift(tooltip, () -> addToTooltipAfterShift(tooltip));

		String addon = PsiAPI.pieceMods.get(getClass());
		if(!addon.equals("psi")) {
			ModContainer container = Loader.instance().getIndexedModList().get(addon);
			if (container != null)
				TooltipHelper.addToTooltip(tooltip, "psimisc.providerMod", container.getName());
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void addToTooltipAfterShift(List<String> tooltip) {
		tooltip.add(TextFormatting.GRAY + TooltipHelper.local(getUnlocalizedDesc()).replaceAll("&", "\u00a7"));

		tooltip.add("");
		String eval = getEvaluationTypeString();
		tooltip.add("<- " + TextFormatting.GOLD + eval);

		for(SpellParam param : paramSides.keySet()) {
			String pName = TooltipHelper.local(param.name);
			String pEval = param.getRequiredTypeString();
			tooltip.add((param.canDisable ? "[->] " : " ->  ") + TextFormatting.YELLOW + pName + " [" + pEval + "]");
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

	@OnlyIn(Dist.CLIENT)
	public boolean onKeyPressed(char c, int i, boolean doit) {
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
		if(cmp.hasKey(TAG_KEY_LEGACY))
			key = cmp.getString(TAG_KEY_LEGACY);
		else key = cmp.getString(TAG_KEY);
		
		if(key.startsWith("_"))
			key = PSI_PREFIX + key.substring(1);
		
		Class<? extends SpellPiece> clazz = PsiAPI.spellPieceRegistry.getObject(key);
		if(clazz != null) {
			SpellPiece p = create(clazz, spell);
			p.readFromNBT(cmp);
			return p;
		}

		return null;
	}

	public static SpellPiece create(Class<? extends SpellPiece> clazz, Spell spell) {
		try {
			return clazz.getConstructor(Spell.class).newInstance(spell);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public SpellPiece copy() {
		CompoundNBT cmp = new CompoundNBT();
		writeToNBT(cmp);
		return createFromNBT(spell, cmp);
	}

	public void readFromNBT(CompoundNBT cmp) {
		CompoundNBT paramCmp = cmp.getCompoundTag(TAG_PARAMS);
		for(String s : params.keySet()) {
			SpellParam param = params.get(s);

			String key = s;
			if(paramCmp.hasKey(key))
				paramSides.put(param, SpellParam.Dist.fromInt(paramCmp.getInteger(key)));
			else {
				if(key.startsWith(SpellParam.PSI_PREFIX))
					key = "_" + key.substring(SpellParam.PSI_PREFIX.length());
				paramSides.put(param, SpellParam.Dist.fromInt(paramCmp.getInteger(key)));
			}
		}
		
		comment = cmp.getString(TAG_COMMENT);
	}

	public void writeToNBT(CompoundNBT cmp) {
		if(comment == null)
			comment = "";
		
		cmp.setString(TAG_KEY, registryKey.replaceAll("^" + PSI_PREFIX, "_"));
		
		int paramCount = 0;
		CompoundNBT paramCmp = new CompoundNBT();
		for(String s : params.keySet()) {
			SpellParam param = params.get(s);
			SpellParam.Dist side = paramSides.get(param);
			paramCmp.setInteger(s.replaceAll("^" + SpellParam.PSI_PREFIX, "_"), side.asInt());
			paramCount++;
		}
		
		if(paramCount > 0)
			cmp.setTag(TAG_PARAMS, paramCmp);
		if(!comment.isEmpty())
			cmp.setString(TAG_COMMENT, comment);
	}

	/**
	 * Empty helper class for use with evaluation types when none is present.
	 */
	public static class Null { }

}
