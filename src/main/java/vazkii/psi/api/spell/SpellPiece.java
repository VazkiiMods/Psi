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
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModList;
import org.joml.Matrix4f;
import vazkii.psi.api.ClientPsiAPI;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.spell.SpellParam.ArrowType;

import java.util.*;
import java.util.stream.Collectors;

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
    @OnlyIn(Dist.CLIENT)
    private static RenderType layer;
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
        registryKey = PsiAPI.getSpellPieceKey(getClass());
        initParams();
    }

    @OnlyIn(Dist.CLIENT)
    public static RenderType getLayer() {
        if (layer == null) {
            RenderType.CompositeState glState = RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorTexLightmapShader))
                    .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
                    .setLightmapState(new RenderStateShard.LightmapStateShard(true))
                    .setTransparencyState(new RenderStateShard.TransparencyStateShard("translucent_transparency", () -> {
                        RenderSystem.enableBlend();
                        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                    }, () -> {
                        RenderSystem.disableBlend();
                        RenderSystem.defaultBlendFunc();
                    }))
                    .setCullState(new RenderStateShard.CullStateShard(false))
                    .createCompositeState(false);
            layer = RenderType.create(TextureAtlas.LOCATION_BLOCKS.toString(), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 64, glState);
        }
        return layer;
    }

    public static SpellPiece createFromNBT(Spell spell, CompoundTag cmp) {
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
        ResourceLocation rl = ResourceLocation.parse(key);
        if (PsiAPI.isPieceRegistered(rl)) {
            exists = true;
        } else {
            Set<String> pieceNamespaces = PsiAPI.getSpellPieceRegistry().keySet().stream().map(ResourceLocation::getNamespace).collect(Collectors.toSet());
            for (String namespace : pieceNamespaces) {
                rl = ResourceLocation.fromNamespaceAndPath(namespace, key);
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

    public static SpellPiece create(ResourceLocation location) {
        return PsiAPI.getSpellPieceRegistry().getOptional(location)
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
        if (getPieceType() == EnumPieceType.CONSTANT) {
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
        if (returnValue instanceof Number number) {
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
        return Component.translatable(getUnlocalizedName()).getString();
    }

    public String getUnlocalizedDesc() {
        return registryKey.getNamespace() + ".spellpiece." + registryKey.getPath() + ".desc";
    }

    /**
     * Sets a {@link StatLabel}'s value.
     */
    public void setStatLabel(EnumSpellStat type, StatLabel descriptor) {
        statLabels.put(type, descriptor);
    }

    /**
     * Draws this piece onto the programmer GUI or the programmer TE projection.<br>
     * All appropriate transformations are already done. Canvas is 16x16 starting from (0, 0, 0).<br>
     * To avoid z-fighting in the TE projection, translations are applied every step.
     */
    @OnlyIn(Dist.CLIENT)
    public void draw(PoseStack pPoseStack, MultiBufferSource buffers, int light) {
        pPoseStack.pushPose();
        drawBackground(pPoseStack, buffers, light);
        pPoseStack.translate(0F, 0F, 0.1F);
        drawAdditional(pPoseStack, buffers, light);
        if (isInGrid) {
            pPoseStack.translate(0F, 0F, 0.1F);
            drawParams(pPoseStack, buffers, light);
            pPoseStack.translate(0F, 0F, 0.1F);
            drawComment(pPoseStack, buffers, light);
        }

        pPoseStack.popPose();
    }

    /**
     * Draws this piece's background.
     */
    @OnlyIn(Dist.CLIENT)
    public void drawBackground(PoseStack pPoseStack, MultiBufferSource buffers, int light) {
        Material material = ClientPsiAPI.getSpellPieceMaterial(registryKey);
        VertexConsumer buffer = material.buffer(buffers, ignored -> getLayer());
        Matrix4f mat = pPoseStack.last().pose();
        // Cannot call .texture() on the chained object because SpriteAwareVertexBuilder is buggy
        // and does not return itself, it returns the inner buffer
        // This leads to .texture() using the implementation of the inner buffer,
        // not of the SpriteAwareVertexBuilder, which is not what we want.
        // Split the chain apart so that .texture() is called on the original buffer
        buffer.addVertex(mat, 0, 16, 0).setColor(1F, 1F, 1F, 1F);
        buffer.setUv(0, 1).setLight(light);

        buffer.addVertex(mat, 16, 16, 0).setColor(1F, 1F, 1F, 1F);
        buffer.setUv(1, 1).setLight(light);

        buffer.addVertex(mat, 16, 0, 0).setColor(1F, 1F, 1F, 1F);
        buffer.setUv(1, 0).setLight(light);

        buffer.addVertex(mat, 0, 0, 0).setColor(1F, 1F, 1F, 1F);
        buffer.setUv(0, 0).setLight(light);
    }

    /**
     * Draws any additional stuff for this piece. Used in connectors
     * to draw the lines.
     */
    @OnlyIn(Dist.CLIENT)
    public void drawAdditional(PoseStack pPoseStack, MultiBufferSource buffers, int light) {
        // NO-OP
    }

    /**
     * Draws the little comment indicator in this piece, if one exists.
     */
    @OnlyIn(Dist.CLIENT)
    public void drawComment(PoseStack pPoseStack, MultiBufferSource buffers, int light) {
        if (comment != null && !comment.isEmpty()) {
            VertexConsumer buffer = buffers.getBuffer(PsiAPI.internalHandler.getProgrammerLayer());

            float wh = 6F;
            float minU = 150 / 256F;
            float minV = 184 / 256F;
            float maxU = (150 + wh) / 256F;
            float maxV = (184 + wh) / 256F;
            Matrix4f mat = pPoseStack.last().pose();

            buffer.addVertex(mat, -2, 4, 0).setColor(1F, 1F, 1F, 1F).setUv(minU, maxV).setLight(light);
            buffer.addVertex(mat, 4, 4, 0).setColor(1F, 1F, 1F, 1F).setUv(maxU, maxV).setLight(light);
            buffer.addVertex(mat, 4, -2, 0).setColor(1F, 1F, 1F, 1F).setUv(maxU, minV).setLight(light);
            buffer.addVertex(mat, -2, -2, 0).setColor(1F, 1F, 1F, 1F).setUv(minU, minV).setLight(light);
        }
    }

    /**
     * Draws the parameters coming into this piece.
     */
    @OnlyIn(Dist.CLIENT)
    public void drawParams(PoseStack pPoseStack, MultiBufferSource buffers, int light) {
        VertexConsumer buffer = buffers.getBuffer(PsiAPI.internalHandler.getProgrammerLayer());
        for (SpellParam<?> param : paramSides.keySet()) {
            drawParam(pPoseStack, buffer, light, param);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void drawParam(PoseStack pPoseStack, VertexConsumer buffer, int light, SpellParam<?> param) {
        SpellParam.Side side = paramSides.get(param);
        if (!side.isEnabled() || param.getArrowType() == ArrowType.NONE) {
            return;
        }

        int index = getParamArrowIndex(param);
        int count = getParamArrowCount(side);
        SpellPiece neighbour = spell.grid.getPieceAtSideSafely(x, y, side);
        if (neighbour != null) {
            int nbcount = neighbour.getParamArrowCount(side.getOpposite());
            if (side.asInt() > side.getOpposite().asInt()) {
                index += nbcount;
            }
            count += nbcount;
        }

        float percent = 0.5f;
        if (count > 1) {
            percent = (float) index / (count - 1);
        }
        drawParam(pPoseStack, buffer, light, side, param.color, param.getArrowType(), percent);
    }

    @OnlyIn(Dist.CLIENT)
    public void drawParam(PoseStack pPoseStack, VertexConsumer buffer, int light, SpellParam.Side side, int color, SpellParam.ArrowType arrowType, float percent) {
        if (arrowType == ArrowType.NONE) {
            return;
        }

        float minX = 4 + side.minx * percent + side.maxx * (1 - percent);
        float minY = 4 + side.miny * percent + side.maxy * (1 - percent);
        float maxX = minX + 8;
        float maxY = minY + 8;

        if (arrowType == ArrowType.OUT) {
            side = side.getOpposite();
        }
        float wh = 8F;
        float minU = side.u / 256F;
        float minV = side.v / 256F;
        float maxU = (side.u + wh) / 256F;
        float maxV = (side.v + wh) / 256F;

        int r = PsiRenderHelper.r(color);
        int g = PsiRenderHelper.g(color);
        int b = PsiRenderHelper.b(color);
        int a = 255;
        Matrix4f mat = pPoseStack.last().pose();

        buffer.addVertex(mat, minX, maxY, 0).setColor(r, g, b, a).setUv(minU, maxV).setLight(light);
        buffer.addVertex(mat, maxX, maxY, 0).setColor(r, g, b, a).setUv(maxU, maxV).setLight(light);
        buffer.addVertex(mat, maxX, minY, 0).setColor(r, g, b, a).setUv(maxU, minV).setLight(light);
        buffer.addVertex(mat, minX, minY, 0).setColor(r, g, b, a).setUv(minU, minV).setLight(light);
    }

    @OnlyIn(Dist.CLIENT)
    public int getParamArrowCount(SpellParam.Side side) {
        int count = 0;
        for (SpellParam<?> p : paramSides.keySet()) {
            if (p.getArrowType() != ArrowType.NONE && paramSides.get(p) == side) {
                count++;
            }
        }
        return count;
    }

    @OnlyIn(Dist.CLIENT)
    public int getParamArrowIndex(SpellParam<?> param) {
        SpellParam.Side side = paramSides.get(param);
        int count = 0;
        for (SpellParam<?> p : paramSides.keySet()) {
            if (p == param) {
                return count;
            }
            if (p.getArrowType() != ArrowType.NONE && paramSides.get(p) == side) {
                count++;
            }
        }
        return 0;
    }

    /**
     * Draws this piece's tooltip.
     */
    @OnlyIn(Dist.CLIENT)
    public void drawTooltip(GuiGraphics graphics, int tooltipX, int tooltipY, List<Component> tooltip, Screen screen) {
        PsiAPI.internalHandler.renderTooltip(graphics, tooltipX, tooltipY, tooltip, 0x505000ff, 0xf0100010, screen.width, screen.height);
    }

    /**
     * Draws this piece's comment tooltip.
     */
    @OnlyIn(Dist.CLIENT)
    public void drawCommentText(GuiGraphics graphics, int tooltipX, int tooltipY, List<Component> commentText, Screen screen) {
        PsiAPI.internalHandler.renderTooltip(graphics, tooltipX, tooltipY - 9 - commentText.size() * 10, commentText, 0x5000a000, 0xf0001e00, screen.width, screen.height);
    }

    @OnlyIn(Dist.CLIENT)
    public void getTooltip(List<Component> tooltip) {
        tooltip.add(Component.translatable(getUnlocalizedName()));
        tooltip.add(Component.translatable(getUnlocalizedDesc()).withStyle(ChatFormatting.GRAY));
        TooltipHelper.tooltipIfShift(tooltip, () -> addToTooltipAfterShift(tooltip));
        if (!statLabels.isEmpty()) {
            TooltipHelper.tooltipIfCtrl(tooltip, () -> addToTooltipAfterCtrl(tooltip));
        }

        String addon = registryKey.getNamespace();
        if (!addon.equals("psi")) {

            if (ModList.get().getModContainerById(addon).isPresent()) {
                tooltip.add(Component.translatable("psimisc.provider_mod", ModList.get().getModContainerById(addon).get().getNamespace()));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addToTooltipAfterShift(List<Component> tooltip) {
        tooltip.add(Component.literal(""));
        MutableComponent eval = getEvaluationTypeString().plainCopy().withStyle(ChatFormatting.GOLD);
        tooltip.add(Component.literal("Output ").append(eval));

        for (SpellParam<?> param : paramSides.keySet()) {
            Component pName = Component.translatable(param.name).withStyle(ChatFormatting.YELLOW);
            Component pEval = Component.literal(" [").append(param.getRequiredTypeString()).append("]").withStyle(ChatFormatting.YELLOW);
            tooltip.add(Component.literal(param.canDisable ? "[Input] " : " Input  ").append(pName).append(pEval));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addToTooltipAfterCtrl(List<Component> tooltip) {
        tooltip.add(Component.literal(""));

        statLabels.forEach((type, stat) -> {
            tooltip.add(Component.translatable(type.getName()).append(":"));
            tooltip.add(Component.literal(" " + stat.toString()).withStyle(ChatFormatting.YELLOW));
        });
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

    public void writeToNBT(CompoundTag cmp) {
        if (comment == null) {
            comment = "";
        }

        cmp.putString(TAG_KEY, registryKey.toString().replaceAll("^" + PSI_PREFIX, "_"));

        int paramCount = 0;
        CompoundTag paramCmp = new CompoundTag();
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
