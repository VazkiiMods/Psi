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

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
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
	public final Map<SpellParam, SpellParam.Side> paramSides = new LinkedHashMap<>();

	public SpellPiece(Spell spell) {
		this.spell = spell;
		registryKey = PsiAPI.spellPieceRegistry.getKey(getClass()).toString();
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
	public ITextComponent getEvaluationTypeString() {
        Class<?> evalType = getEvaluationType();
        String evalStr = evalType == null ? "Null" : evalType.getSimpleName();
        ITextComponent s = new TranslationTextComponent("psi.datatype." + evalStr);
        if (getPieceType() == EnumPieceType.CONSTANT)
            s.appendSibling(new TranslationTextComponent("psimisc.const"));

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
		paramSides.put(param, SpellParam.Side.OFF);
	}

	/**
	 * Gets the value of one of this piece's params in the given context.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getParamValue(SpellContext context, SpellParam param) {
		SpellParam.Side side = paramSides.get(param);
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
		SpellParam.Side side = paramSides.get(param);
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
        return new TranslationTextComponent(getUnlocalizedName()).getString();
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
        RenderSystem.translatef(0F, 0F, 0.1F);
        drawAdditional();
        if (isInGrid) {
            RenderSystem.translatef(0F, 0F, 0.1F);
            drawParams();
            RenderSystem.translatef(0F, 0F, 0.1F);
            drawComment();
        }

        RenderSystem.color3f(1F, 1F, 1F);
    }

	/**
	 * Draws this piece's background.
	 * @see #draw()
	 */
	@OnlyIn(Dist.CLIENT)
	public void drawBackground() {
        ResourceLocation res = PsiAPI.simpleSpellTextures.get(registryKey);
        Minecraft.getInstance().textureManager.bindTexture(res);

        RenderSystem.color3f(1F, 1F, 1F);
        BufferBuilder wr = Tessellator.getInstance().getBuffer();
        wr.begin(7, DefaultVertexFormats.POSITION_TEX);
        wr.vertex(0, 16, 0).texture(0, 1).endVertex();
        wr.vertex(16, 16, 0).texture(1, 1).endVertex();
        wr.vertex(16, 0, 0).texture(1, 0).endVertex();
        wr.vertex(0, 0, 0).texture(0, 0).endVertex();
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
            Minecraft.getInstance().textureManager.bindTexture(PsiAPI.internalHandler.getProgrammerTexture());

            float wh = 6F;
            float minU = 150 / 256F;
            float minV = 184 / 256F;
            float maxU = (150 + wh) / 256F;
            float maxV = (184 + wh) / 256F;
            RenderSystem.color4f(1F, 1F, 1F, 1F);

            BufferBuilder wr = Tessellator.getInstance().getBuffer();
            wr.begin(7, DefaultVertexFormats.POSITION_TEX);
            wr.vertex(-2, 4, 0).texture(minU, maxV).endVertex();
            wr.vertex(4, 4, 0).texture(maxU, maxV).endVertex();
            wr.vertex(4, -2, 0).texture(maxU, minV).endVertex();
            wr.vertex(-2, -2, 0).texture(minU, minV).endVertex();
            Tessellator.getInstance().draw();
        }
	}
	
	/**
	 * Draws the parameters coming into this piece.
	 * @see #draw()
	 */
	@OnlyIn(Dist.CLIENT)
	public void drawParams() {
        Minecraft.getInstance().textureManager.bindTexture(PsiAPI.internalHandler.getProgrammerTexture());
        RenderSystem.enableAlphaTest();
        for (SpellParam param : paramSides.keySet()) {
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
                RenderSystem.color4f(PsiRenderHelper.r(param.color) / 255F,
                        PsiRenderHelper.g(param.color) / 255F,
                        PsiRenderHelper.b(param.color) / 255F, 1F);

                BufferBuilder wr = Tessellator.getInstance().getBuffer();
                wr.begin(7, DefaultVertexFormats.POSITION_TEX);
                wr.vertex(minX, maxY, 0).texture(minU, maxV).endVertex();
                wr.vertex(maxX, maxY, 0).texture(maxU, maxV).endVertex();
                wr.vertex(maxX, minY, 0).texture(maxU, minV).endVertex();
                wr.vertex(minX, minY, 0).texture(minU, minV).endVertex();
                Tessellator.getInstance().draw();
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

        String addon = PsiAPI.pieceMods.get(getClass());
        if (!addon.equals("psi")) {

            if (ModList.get().getModContainerById(addon).isPresent())
	            tooltip.add(new TranslationTextComponent("psimisc.providerMod", ModList.get().getModContainerById(addon).get().getNamespace()));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addToTooltipAfterShift(List<ITextComponent> tooltip) {
        tooltip.add(new TranslationTextComponent(getUnlocalizedDesc()).applyTextStyle(TextFormatting.GRAY));

        tooltip.add(new StringTextComponent(""));
        ITextComponent eval = getEvaluationTypeString().applyTextStyle(TextFormatting.GOLD);
        tooltip.add(new StringTextComponent("<- ").appendSibling(eval));

        for (SpellParam param : paramSides.keySet()) {
            ITextComponent pName = new TranslationTextComponent(param.name).applyTextStyle(TextFormatting.YELLOW);
            ITextComponent pEval = new StringTextComponent(" [").appendSibling(param.getRequiredTypeString()).appendText("]").applyTextStyle(TextFormatting.YELLOW);
            tooltip.add(new StringTextComponent(param.canDisable ? "[->] " : " ->  ").appendSibling(pName).appendSibling(pEval));
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
        if (cmp.contains(TAG_KEY_LEGACY))
            key = cmp.getString(TAG_KEY_LEGACY);
        else key = cmp.getString(TAG_KEY);

        if (key.startsWith("_"))
            key = PSI_PREFIX + key.substring(1);

        ;
        if (PsiAPI.spellPieceRegistry.getValue(new ResourceLocation(key)).isPresent()) {
            Class<? extends SpellPiece> clazz = PsiAPI.spellPieceRegistry.getValue(new ResourceLocation(key)).get();
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
        CompoundNBT paramCmp = cmp.getCompound(TAG_PARAMS);
        for (String s : params.keySet()) {
            SpellParam param = params.get(s);

            String key = s;
            if (paramCmp.contains(key))
                paramSides.put(param, SpellParam.Side.fromInt(paramCmp.getInt(key)));
            else {
                if (key.startsWith(SpellParam.PSI_PREFIX))
                    key = "_" + key.substring(SpellParam.PSI_PREFIX.length());
                paramSides.put(param, SpellParam.Side.fromInt(paramCmp.getInt(key)));
            }
        }
		
		comment = cmp.getString(TAG_COMMENT);
	}

	public void writeToNBT(CompoundNBT cmp) {
        if (comment == null)
            comment = "";

        cmp.putString(TAG_KEY, registryKey.replaceAll("^" + PSI_PREFIX, "_"));

        int paramCount = 0;
        CompoundNBT paramCmp = new CompoundNBT();
        for (String s : params.keySet()) {
            SpellParam param = params.get(s);
            SpellParam.Side side = paramSides.get(param);
            paramCmp.putInt(s.replaceAll("^" + SpellParam.PSI_PREFIX, "_"), side.asInt());
            paramCount++;
        }

        if (paramCount > 0)
            cmp.put(TAG_PARAMS, paramCmp);
        if (!comment.isEmpty())
            cmp.putString(TAG_COMMENT, comment);
	}

	/**
	 * Empty helper class for use with evaluation types when none is present.
	 */
	public static class Null { }

}
