/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 16:22:49 (GMT)]
 */
package vazkii.psi.client.gui;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.glfw.GLFW;
import vazkii.arl.util.RenderHelper;
import vazkii.arl.util.TooltipHandler;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.spell.*;
import vazkii.psi.client.core.helper.SharingHelper;
import vazkii.psi.client.gui.button.GuiButtonIO;
import vazkii.psi.client.gui.button.GuiButtonPage;
import vazkii.psi.client.gui.button.GuiButtonSideConfig;
import vazkii.psi.client.gui.button.GuiButtonSpellPiece;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageSpellModified;
import vazkii.psi.common.spell.SpellCompiler;
import vazkii.psi.common.spell.constant.PieceConstantNumber;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuiProgrammer extends Screen {
    public static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_PROGRAMMER);
    private static final int PIECES_PER_PAGE = 25;

    public final TileProgrammer programmer;
    public Spell spell;
    public List<ITextComponent> tooltip = new ArrayList<>();
    public final Stack<Spell> undoSteps = new Stack<>();
    public final Stack<Spell> redoSteps = new Stack<>();
    public static SpellPiece clipboard = null;

    public SpellCompiler compiler;

    public int xSize, ySize, padLeft, padTop, left, top, gridLeft, gridTop;
    public int cursorX, cursorY;
    public static int selectedX, selectedY;
    public boolean panelEnabled, configEnabled, commentEnabled;
	public int panelX, panelY, panelWidth, panelHeight, panelCursor;
	public int page = 0;
	public boolean scheduleButtonUpdate = false;
	public final List<SpellPiece> visiblePieces = new ArrayList<>();
	public final List<Button> panelButtons = new ArrayList<>();
	public final List<Button> configButtons = new ArrayList<>();
	public TextFieldWidget searchField;
	public TextFieldWidget spellNameField;
	public TextFieldWidget commentField;
	
	public boolean takingScreenshot = false;
	public boolean shareToReddit = false;
	boolean spectator;

	public GuiProgrammer(TileProgrammer programmer) {
		this(programmer, programmer.spell);
	}


    public GuiProgrammer(TileProgrammer tile, Spell spell) {
        super(new StringTextComponent(""));
        programmer = tile;
        this.spell = spell;
        compiler = new SpellCompiler(spell);
    }


    @Override
    protected void init() {
        super.init();

        xSize = 174;
        ySize = 184;
        padLeft = 7;
        padTop = 7;
        left = (width - xSize) / 2;
        top = (height - ySize) / 2;
        gridLeft = left + padLeft;
        gridTop = top + padTop;
        panelWidth = 100;
        panelHeight = 125;
        cursorX = cursorY = -1;
        searchField = new TextFieldWidget(font, 0, 0, 70, 10, "");
        searchField.setCanLoseFocus(false);
        searchField.setFocused2(true);
        searchField.setEnableBackgroundDrawing(false);

        if (programmer == null)
            spectator = false;
        else
            spectator = programmer.playerLock != null && !programmer.playerLock.isEmpty() && !programmer.playerLock.equals(minecraft.player.getName());

        spellNameField = new TextFieldWidget(font, left + xSize - 130, top + ySize - 14, 120, 10, "");
        spellNameField.setEnableBackgroundDrawing(false);
        spellNameField.setMaxStringLength(20);
        spellNameField.setEnabled(!spectator);

        commentField = new TextFieldWidget(font, left, top + ySize / 2 - 10, xSize, 20, "");

        commentField.setEnabled(false);
        commentField.setVisible(false);
        commentField.setMaxStringLength(500);

        if (spell == null)
            spell = new Spell();

        if (programmer != null && programmer.spell == null)
            programmer.spell = spell;

        spellNameField.setText(spell.name);

        buttons.clear();
        onSelectedChanged();
        buttons.add(new GuiButtonIO(left + xSize + 2, top + ySize - (spectator ? 16 : 32), true, this, button -> {
            if (hasShiftDown()) {
                if (((GuiButtonIO) button).out) {
                    CompoundNBT cmp = new CompoundNBT();
                    if (spell != null)
                        spell.writeToNBT(cmp);
                    minecraft.keyboardListener.setClipboardString(cmp.toString());
                } else {
                    if (spectator)
                        return;

                    String cb = minecraft.keyboardListener.getClipboardString();

                    try {
                        cb = cb.replaceAll("([^a-z0-9])\\d+:", "$1"); // backwards compatibility with pre 1.12 nbt json
                        CompoundNBT cmp = JsonToNBT.getTagFromJson(cb);
                        spell = Spell.createFromNBT(cmp);
                        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(minecraft.player);
                        for (int i = 0; i < SpellGrid.GRID_SIZE; i++)
                            for (int j = 0; j < SpellGrid.GRID_SIZE; j++) {
                                SpellPiece piece = spell.grid.gridData[i][j];
                                if (piece != null) {
                                    PieceGroup group = PsiAPI.groupsForPiece.get(piece.getClass());
                                    if (!minecraft.player.isCreative() && (group == null || !data.isPieceGroupUnlocked(group.name, piece.registryKey))) {
                                        minecraft.player.sendMessage(new TranslationTextComponent("psimisc.missingPieces").setStyle(new Style().setColor(TextFormatting.RED)));
                                        return;
                                    }
                                }
                            }

                        pushState(true);
                        spellNameField.setText(spell.name);
                        onSpellChanged(false);
                    } catch (Throwable t) {
                        minecraft.player.sendMessage(new TranslationTextComponent("psimisc.malformedJson", t.getMessage()).setStyle(new Style().setColor(TextFormatting.RED)));
                    }
                }
            }
        }));
        if (!spectator)
            buttons.add(new GuiButtonIO(left + xSize + 2, top + ySize - 16, false, this, button -> {
                if (hasShiftDown()) {
                    if (((GuiButtonIO) button).out) {
                        CompoundNBT cmp = new CompoundNBT();
                        if (spell != null)
                            spell.writeToNBT(cmp);
                        minecraft.keyboardListener.setClipboardString(cmp.toString());
                    } else {
                        if (spectator)
                            return;

                        String cb = minecraft.keyboardListener.getClipboardString();

                        try {
                            cb = cb.replaceAll("([^a-z0-9])\\d+:", "$1"); // backwards compatibility with pre 1.12 nbt json
                            CompoundNBT cmp = JsonToNBT.getTagFromJson(cb);
                            spell = Spell.createFromNBT(cmp);
                            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(minecraft.player);
                            for (int i = 0; i < SpellGrid.GRID_SIZE; i++)
                                for (int j = 0; j < SpellGrid.GRID_SIZE; j++) {
                                    SpellPiece piece = spell.grid.gridData[i][j];
                                    if (piece != null) {
                                        PieceGroup group = PsiAPI.groupsForPiece.get(piece.getClass());
                                        if (!minecraft.player.isCreative() && (group == null || !data.isPieceGroupUnlocked(group.name, piece.registryKey))) {
                                            minecraft.player.sendMessage(new TranslationTextComponent("psimisc.missingPieces").setStyle(new Style().setColor(TextFormatting.RED)));
                                            return;
                                        }
                                    }
                                }

                            pushState(true);
                            spellNameField.setText(spell.name);
                            onSpellChanged(false);
                        } catch (Throwable t) {
                            minecraft.player.sendMessage(new TranslationTextComponent("psimisc.malformedJson", t.getMessage()).setStyle(new Style().setColor(TextFormatting.RED)));
                        }
                    }
                }
            }));
    }


    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (programmer != null && (programmer.getWorld().getTileEntity(programmer.getPos()) != programmer || !programmer.canPlayerInteract(minecraft.player))) {
            minecraft.displayGuiScreen(null);
            return;
        }

        ITooltipFlag tooltipFlag = minecraft.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL;

        String comment = "";
        int color = Psi.magical ? 0 : 0xFFFFFF;

        if (scheduleButtonUpdate) {
            updatePanelButtons();
            scheduleButtonUpdate = false;
        }

        RenderSystem.pushMatrix();
        tooltip.clear();
        renderBackground();

        RenderSystem.color3f(1F, 1F, 1F);
        minecraft.getTextureManager().bindTexture(texture);

        blit(left, top, 0, 0, xSize, ySize);
        blit(left - 48, top + 5, xSize, 0, 48, 30);

        int statusX = left - 16;
        int statusY = top + 13;
        blit(statusX, statusY, compiler.isErrored() ? 12 : 0, ySize + 28, 12, 12);
        if (mouseX > statusX - 1 && mouseY > statusY - 1 && mouseX < statusX + 13 && mouseY < statusY + 13) {
            if (compiler.isErrored()) {
                tooltip.add(new TranslationTextComponent("psimisc.errored").applyTextStyle(TextFormatting.RED));
                tooltip.add(new TranslationTextComponent(compiler.getError()).applyTextStyle(TextFormatting.GRAY));
                Pair<Integer, Integer> errorPos = compiler.getErrorLocation();
                if (errorPos != null && errorPos.getLeft() != -1 && errorPos.getRight() != -1)
                    tooltip.add(new StringTextComponent("[" + (errorPos.getLeft() + 1) + ", " + (errorPos.getRight() + 1) + "]").applyTextStyle(TextFormatting.GRAY));
            } else
                tooltip.add(new TranslationTextComponent("psimisc.compiled").applyTextStyle(TextFormatting.GREEN));
        }

        ItemStack cad = PsiAPI.getPlayerCAD(minecraft.player);
        if (!cad.isEmpty()) {
            int cadX = left - 42;
            int cadY = top + 12;
            RenderSystem.enableRescaleNormal();
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(770, 771, 1, 0);
            RenderSystem.enableLighting();
            RenderSystem.enableColorMaterial();
            minecraft.getItemRenderer().renderItemAndEffectIntoGUI(cad, cadX, cadY);
            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
            RenderSystem.disableRescaleNormal();

            if (mouseX > cadX && mouseY > cadY && mouseX < cadX + 16 && mouseY < cadY + 16) {
                List<ITextComponent> itemTooltip = cad.getTooltip(minecraft.player, tooltipFlag);
                for (int i = 0; i < itemTooltip.size(); ++i)
                    if (i == 0)
                        itemTooltip.set(i, new StringTextComponent(cad.getRarity().color + itemTooltip.get(i).toString()));
                    else
                        itemTooltip.set(i, new StringTextComponent(TextFormatting.GRAY + itemTooltip.get(i).toString()));

                tooltip.addAll(itemTooltip);
            }
        }
        minecraft.getTextureManager().bindTexture(texture);

        if (!compiler.isErrored()) {
            int i = 0;
            SpellMetadata meta = compiler.getCompiledSpell().metadata;

            for (EnumSpellStat stat : meta.stats.keySet()) {
                int statX = left + xSize + 3;
                int statY = top + (takingScreenshot ? 40 : 20) + i * 20;
                int val = meta.stats.get(stat);

                EnumCADStat cadStat = stat.getTarget();
                int cadVal = 0;
                if (cadStat == null)
                    cadVal = -1;
                else if (!cad.isEmpty()) {
                    ICAD cadItem = (ICAD) cad.getItem();
                    cadVal = cadItem.getStatValue(cad, cadStat);
                }
                String s = "" + val;
                if (stat == EnumSpellStat.COST)
                    s += " (" + Math.max(0, ItemCAD.getRealCost(cad, ItemStack.EMPTY, val)) + ")";
                else s += "/" + (cadVal == -1 ? "\u221E" : cadVal);

                RenderSystem.color3f(1F, 1F, 1F);
                blit(statX, statY, (stat.ordinal() + 1) * 12, ySize + 16, 12, 12);
                font.drawString(s, statX + 16, statY + 2, cadStat != null && cadVal < val && cadVal != -1 ? 0xFF6666 : 0xFFFFFF);
                minecraft.getTextureManager().bindTexture(texture);

                if (mouseX > statX && mouseY > statY && mouseX < statX + 12 && mouseY < statY + 12) {
                    tooltip.add(new TranslationTextComponent(stat.getName()).applyTextStyle(Psi.magical ? TextFormatting.LIGHT_PURPLE : TextFormatting.AQUA));
                    tooltip.add(new TranslationTextComponent(stat.getDesc()).applyTextStyle(TextFormatting.GRAY));
                }

                i++;
            }
        }
        RenderSystem.color3f(1F, 1F, 1F);

        SpellPiece piece = null;
        if (SpellGrid.exists(selectedX, selectedY))
            piece = spell.grid.gridData[selectedX][selectedY];

        if (configEnabled && !takingScreenshot) {
            blit(left - 81, top + 55, xSize, 30, 81, 115);
            String configStr = I18n.format("psimisc.config");
            font.drawString(configStr, left - font.getStringWidth(configStr) - 2, top + 45, 0xFFFFFF);

            int i = 0;
            if (piece != null) {
                int param = -1;
                for (int j = 0; j < 4; j++)
                    if (InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_1 + j))
                        param = j;

                for (String s : piece.params.keySet()) {
                    int x = left - 75;
                    int y = top + 70 + i * 26;

                    RenderSystem.color3f(1F, 1F, 1F);
                    minecraft.getTextureManager().bindTexture(texture);
                    blit(x + 50, y - 8, xSize, 145, 24, 24);

                    String localized = I18n.format(s);
                    if (i == param)
                        localized = TextFormatting.UNDERLINE + localized;

                    font.drawString(localized, x, y, 0xFFFFFF);
                    i++;
                }
            }
        }

        cursorX = (mouseX - gridLeft) / 18;
        cursorY = (mouseY - gridTop) / 18;
        if (panelEnabled || cursorX > 8 || cursorY > 8 || cursorX < 0 || cursorY < 0 || mouseX < gridLeft || mouseY < gridTop) {
            cursorX = -1;
            cursorY = -1;
        }
        int tooltipX = mouseX;
        int tooltipY = mouseY;

        RenderSystem.pushMatrix();
        RenderSystem.translatef(gridLeft, gridTop, 0);
        spell.draw();

        if (compiler.isErrored()) {
            Pair<Integer, Integer> errorPos = compiler.getErrorLocation();
            if (errorPos != null && errorPos.getLeft() != -1 && errorPos.getRight() != -1) {
                int errorX = errorPos.getLeft() * 18 + 12;
                int errorY = errorPos.getRight() * 18 + 8;
                font.drawStringWithShadow("!!", errorX, errorY, 0xFF0000);
            }
        }

        RenderSystem.popMatrix();
        RenderSystem.color3f(1F, 1F, 1F);
        RenderSystem.translatef(0, 0, 1);

        minecraft.getTextureManager().bindTexture(texture);

        if (selectedX != -1 && selectedY != -1 && !takingScreenshot)
            blit(gridLeft + selectedX * 18, gridTop + selectedY * 18, 32, ySize, 16, 16);

        if (hasAltDown()) {
            tooltip.clear();
            cursorX = selectedX;
            cursorY = selectedY;
            tooltipX = gridLeft + cursorX * 18 + 10;
            tooltipY = gridTop + cursorY * 18 + 8;
        }

        SpellPiece pieceAt = null;
        if (cursorX != -1 && cursorY != -1) {
            pieceAt = spell.grid.gridData[cursorX][cursorY];
            if (pieceAt != null) {
                pieceAt.getTooltip(tooltip);
                comment = pieceAt.comment;
            }

            if (!takingScreenshot) {
                if (cursorX == selectedX && cursorY == selectedY)
                    blit(gridLeft + cursorX * 18, gridTop + cursorY * 18, 16, ySize, 8, 16);
                else blit(gridLeft + cursorX * 18, gridTop + cursorY * 18, 16, ySize, 16, 16);
            }
        }

        int topy = top - 12;
        if (!takingScreenshot) {
            int topyText = topy;
            if (spectator) {
                String betaTest = TextFormatting.RED + I18n.format("psimisc.spectator");
                font.drawStringWithShadow(betaTest, left + xSize / 2f - font.getStringWidth(betaTest) / 2f, topyText, 0xFFFFFF);
                topyText -= 10;
            }
            if (LibMisc.BETA_TESTING) {
                String betaTest = I18n.format("psimisc.wip");
                font.drawStringWithShadow(betaTest, left + xSize / 2f - font.getStringWidth(betaTest) / 2f, topyText, 0xFFFFFF);
            }
            if (piece != null) {
                String name = I18n.format(piece.getUnlocalizedName());
                font.drawStringWithShadow(name, left + xSize / 2f - font.getStringWidth(name) / 2f, topyText, 0xFFFFFF);
            }

            String coords;
            if (SpellGrid.exists(cursorX, cursorY))
                coords = I18n.format("psimisc.programmerCoords", selectedX + 1, selectedY + 1, cursorX + 1, cursorY + 1);
            else
                coords = I18n.format("psimisc.programmerCoordsNoCursor", selectedX + 1, selectedY + 1);
            font.drawString(coords, left + 4, topy + ySize + 14, 0x44FFFFFF);
        }

        if (Psi.magical)
            font.drawString(I18n.format("psimisc.name"), left + padLeft, spellNameField.y + 1, color);
        else
            font.drawStringWithShadow(I18n.format("psimisc.name"), left + padLeft, spellNameField.y + 1, color);

        spellNameField.setVisible(true);
        spellNameField.active = true;
        spellNameField.setEnableBackgroundDrawing(true);
        if (panelEnabled) {
            tooltip.clear();
            minecraft.getTextureManager().bindTexture(texture);

            fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0x88000000);

            if (panelButtons.size() > 0) {
                Button button = panelButtons.get(Math.max(0, Math.min(panelCursor, panelButtons.size() - 1)));
                int panelPieceX = button.x;
                int panelPieceY = button.y;
                fill(panelPieceX - 1, panelPieceY - 1, panelPieceX + 17, panelPieceY + 17, 0x559999FF);
            }

            RenderSystem.color3f(1F, 1F, 1F);
            blit(searchField.x - 14, searchField.y - 2, 0, ySize + 16, 12, 12);
            searchField.setVisible(true);
            searchField.active = true;
            String s = page + 1 + "/" + getPageCount();
            font.drawStringWithShadow(s, panelX + panelWidth / 2f - font.getStringWidth(s) / 2f, panelY + panelHeight - 12, 0xFFFFFF);
        }

        commentField.setVisible(true);
        commentField.active = true;
        if (commentEnabled) {
            String s = I18n.format("psimisc.enterCommit");
            font.drawStringWithShadow(s, left + xSize / 2f - font.getStringWidth(s) / 2f, commentField.y + 24, 0xFFFFFF);
            s = I18n.format("psimisc.semicolonLine");
            font.drawStringWithShadow(s, left + xSize / 2f - font.getStringWidth(s) / 2f, commentField.y + 34, 0xFFFFFF);
        }
        RenderSystem.color3f(1F, 1F, 1F);

        if (!takingScreenshot) {
            minecraft.getTextureManager().bindTexture(texture);
            int helpX = left + xSize + 2;
            int helpY = top + ySize - (spectator ? 32 : 48);
            boolean overHelp = mouseX > helpX && mouseY > helpY && mouseX < helpX + 12 && mouseY < helpY + 12;
            blit(helpX, helpY, xSize + (overHelp ? 12 : 0), ySize + 9, 12, 12);
            List<String> vazkiiWhyDoYouDoThisToMe = new ArrayList<>();
            for (ITextComponent component : tooltip) {
                vazkiiWhyDoYouDoThisToMe.add(component.toString());
            }

            if (overHelp && !hasAltDown()) {
                TooltipHandler.addToTooltip(vazkiiWhyDoYouDoThisToMe, I18n.format("psimisc.programmerHelp"));
                tooltip.add(new TranslationTextComponent("psimisc.programmerHelp"));
                String ctrl = I18n.format(Minecraft.IS_RUNNING_ON_MAC ? "psimisc.ctrlMac" : "psimisc.ctrlWindows");
                TooltipHandler.tooltipIfShift(vazkiiWhyDoYouDoThisToMe, () -> {
                    int i = 0;
                    while (I18n.hasKey("psi.programmerReference" + i)) {
                        vazkiiWhyDoYouDoThisToMe.add(I18n.format("psi.programmerReference" + i++, ctrl));
                        tooltip.add(new TranslationTextComponent("psi.programmerReference" + i++, ctrl));
                    }

                });
            }
        }
        List<ITextComponent> legitTooltip = null;
        List<String> legitVazkiiWhyDoYouDoThisToMe = null;
        if (hasAltDown()) {
            legitTooltip = new ArrayList<>(tooltip);
            for (ITextComponent component : tooltip) {
                legitVazkiiWhyDoYouDoThisToMe.add(component.toString());
            }
        }


        super.render(mouseX, mouseY, partialTicks);

        if (hasAltDown()) {
            tooltip = legitTooltip;
        }


        if (!takingScreenshot && pieceAt != null) {
            if (tooltip != null && !tooltip.isEmpty())
                pieceAt.drawTooltip(tooltipX, tooltipY, tooltip);


            if (comment != null && !comment.isEmpty()) {
                List<String> l = Arrays.asList(comment.split(";"));
                List<ITextComponent> componentL = new ArrayList<>();
                for (String s : l) {
                    componentL.add(new StringTextComponent(s));
                }

                pieceAt.drawCommentText(tooltipX, tooltipY, componentL);
            }
        } else if (!takingScreenshot && tooltip != null && !tooltip.isEmpty()) {
            legitVazkiiWhyDoYouDoThisToMe.clear();
            for (ITextComponent component : tooltip) {
                legitVazkiiWhyDoYouDoThisToMe.add(component.toString());
            }
            RenderHelper.renderTooltip(tooltipX, tooltipY, legitVazkiiWhyDoYouDoThisToMe);
        }


        RenderSystem.popMatrix();

        if (takingScreenshot) {
            String name = spellNameField.getText();
            CompoundNBT cmp = new CompoundNBT();
            if (spell != null)
                spell.writeToNBT(cmp);
            String export = cmp.toString();

            if (shareToReddit)
                SharingHelper.uploadAndShare(name, export);
            else SharingHelper.uploadAndOpen(name, export);

            takingScreenshot = false;
            shareToReddit = false;
        }
    }

    @Override
    public boolean mouseScrolled(double par1, double par2, double par3) {
        super.mouseScrolled(par1, par2, par3);

        int max = getPageCount();
        if (par3 != 0) {
            int next = (int) (page - par3 / Math.abs(par3));

            if (next >= 0 && next < max) {
                page = next;
                updatePanelButtons();
            }

        }
        return false;
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (programmer != null)
            spell = programmer.spell;

        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (panelEnabled) {
            searchField.mouseClicked(mouseX, mouseY, mouseButton);

            if (mouseX < panelX || mouseY < panelY || mouseX > panelX + panelWidth || mouseY > panelY + panelHeight)
                closePanel();
        } else if (!commentEnabled) {
            spellNameField.mouseClicked(mouseX, mouseY, mouseButton);
            if (commentField.getVisible())
                commentField.mouseClicked(mouseX, mouseY, mouseButton);

            if (cursorX != -1 && cursorY != -1) {
                selectedX = cursorX;
                selectedY = cursorY;
                if (mouseButton == 1 && !spectator) {
                    if (hasShiftDown()) {
                        pushState(true);
                        spell.grid.gridData[selectedX][selectedY] = null;
                        onSpellChanged(false);
                        return true;
                    }
                    openPanel();
                }
                onSelectedChanged();
            }
        }
        return false;
    }


    @Override
    public boolean charTyped(char par1, int par2) {
        if (programmer != null)
            spell = programmer.spell;

        if (par2 == GLFW.GLFW_KEY_ESCAPE) {
            if (panelEnabled) {
                closePanel();
                return true;
            }

            if (commentEnabled) {
                closeComment(false);
                return true;
            }
        }

        super.charTyped(par1, par2);

        if (spectator)
            return false;

        if (panelEnabled) {
            String last = searchField.getText();
            searchField.charTyped(par1, par2);
            if (!searchField.getText().equals(last)) {
                page = 0;
                updatePanelButtons();
            }

            if (panelButtons.size() >= 1) {
                if (par2 == GLFW.GLFW_KEY_ENTER)
                    //TODO dunno about this one
                    panelButtons.get(panelCursor).onPress();
                else if (par2 == GLFW.GLFW_KEY_TAB) {
                    int newCursor = panelCursor + (hasShiftDown() ? -1 : 1);
                    panelCursor = Math.max(0, Math.min(newCursor, panelButtons.size() - 1));
                }
            }
        } else if (commentEnabled) {
            if (par2 == GLFW.GLFW_KEY_ENTER)
                closeComment(true);
            commentField.charTyped(par1, par2);
        } else {
            boolean pieceHandled = false;
            boolean intercepts = false;
            SpellPiece piece = null;
            if (selectedX != -1 && selectedY != -1) {
                piece = spell.grid.gridData[selectedX][selectedY];
                if (piece != null && piece.interceptKeystrokes()) {
                    intercepts = true;
                    if (piece.onKeyPressed(par1, par2, false)) {
                        pushState(true);
                        piece.onKeyPressed(par1, par2, true);
                        onSpellChanged(false);
                        pieceHandled = true;
                    }
                }
            }

            boolean shift = hasShiftDown();
            boolean ctrl = hasControlDown();

            if (!pieceHandled) {
                if ((par2 == GLFW.GLFW_KEY_DELETE || par2 == GLFW.GLFW_KEY_BACKSPACE) && !spellNameField.isFocused()) {
                    if (shift && ctrl) {
                        if (!spell.grid.isEmpty()) {
                            pushState(true);
                            spell = new Spell();
                            spellNameField.setText("");
                            onSpellChanged(false);
                            return true;
                        }
                    }
                    if (selectedX != -1 && selectedY != -1 && piece != null) {
                        pushState(true);
                        spell.grid.gridData[selectedX][selectedY] = null;
                        onSpellChanged(false);
                        return true;
                    }
                }

                String lastName = spellNameField.getText();
                spellNameField.charTyped(par1, par2);
                String currName = spellNameField.getText();
                if (!lastName.equals(currName)) {
                    spell.name = currName;
                    onSpellChanged(true);
                }

                if (par2 == GLFW.GLFW_KEY_TAB && !intercepts)
                    spellNameField.setFocused2(!spellNameField.isFocused());
                else if (!spellNameField.isFocused()) {
                    if (ctrl) {
                        switch (par2) {
                            case GLFW.GLFW_KEY_UP:
                                if (shift) {
                                    pushState(true);
                                    spell.grid.mirrorVertical();
                                    onSpellChanged(false);
                                } else if (spell.grid.shift(SpellParam.Side.TOP, false)) {
                                    pushState(true);
                                    spell.grid.shift(SpellParam.Side.TOP, true);
                                    onSpellChanged(false);
                                }
                                break;
                            case GLFW.GLFW_KEY_LEFT:
                                if (shift) {
                                    pushState(true);
                                    spell.grid.rotate(false);
                                    onSpellChanged(false);
                                } else if (spell.grid.shift(SpellParam.Side.LEFT, false)) {
                                    pushState(true);
                                    spell.grid.shift(SpellParam.Side.LEFT, true);
                                    onSpellChanged(false);
                                }
                                break;
                            case GLFW.GLFW_KEY_RIGHT:
                                if (shift) {
                                    pushState(true);
                                    spell.grid.rotate(true);
                                    onSpellChanged(false);
                                } else if (spell.grid.shift(SpellParam.Side.RIGHT, false)) {
                                    pushState(true);
                                    spell.grid.shift(SpellParam.Side.RIGHT, true);
                                    onSpellChanged(false);
                                }
                                break;
                            case GLFW.GLFW_KEY_DOWN:
                                if (shift) {
                                    pushState(true);
                                    spell.grid.mirrorVertical();
                                    onSpellChanged(false);
                                } else if (spell.grid.shift(SpellParam.Side.BOTTOM, false)) {
                                    pushState(true);
                                    spell.grid.shift(SpellParam.Side.BOTTOM, true);
                                    onSpellChanged(false);
                                }
                                break;
                            case GLFW.GLFW_KEY_Z:
                                if (!undoSteps.isEmpty()) {
                                    redoSteps.add(spell.copy());
                                    spell = undoSteps.pop();
                                    onSpellChanged(false);
                                }
                                break;
                            case GLFW.GLFW_KEY_Y:
                                if (!redoSteps.isEmpty()) {
                                    pushState(false);
                                    spell = redoSteps.pop();
                                    onSpellChanged(false);
                                }
                                break;
                            case GLFW.GLFW_KEY_C:
                                if (piece != null)
                                    clipboard = piece.copy();
                                break;
                            case GLFW.GLFW_KEY_X:
                                if (piece != null) {
                                    clipboard = piece.copy();
                                    pushState(true);
                                    spell.grid.gridData[selectedX][selectedY] = null;
                                    onSpellChanged(false);
                                }
                                break;
                            case GLFW.GLFW_KEY_V:
                                if (SpellGrid.exists(selectedX, selectedY) && clipboard != null) {
                                    pushState(true);
                                    SpellPiece copy = clipboard.copy();
                                    copy.x = selectedX;
                                    copy.y = selectedY;
                                    spell.grid.gridData[selectedX][selectedY] = copy;
                                    onSpellChanged(false);
                                }
                                break;
                            case GLFW.GLFW_KEY_D:
                                if (piece != null) {
                                    commentField.setVisible(true);
                                    commentField.setFocused2(true);
                                    commentField.setEnabled(true);
                                    spellNameField.setEnabled(false);
                                    commentField.setText(piece.comment);
                                    commentEnabled = true;
                                }
                                break;
                            case GLFW.GLFW_KEY_G:
                                shareToReddit = false;
                                if (shift && hasAltDown())
                                    takingScreenshot = true;
                                break;
                            case GLFW.GLFW_KEY_R:
                                shareToReddit = true;
                                if (shift && hasAltDown())
                                    takingScreenshot = true;
                                break;
                        }
                    } else {
                        int param = -1;
                        for (int i = 0; i < 4; i++)
                            if (InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_1 + i))
                                param = i;

                        switch (par2) {
                            case GLFW.GLFW_KEY_UP:
                                if (!onSideButtonKeybind(piece, param, SpellParam.Side.TOP) && selectedY > 0) {
                                    selectedY--;
                                    onSelectedChanged();
                                }
                                break;
                            case GLFW.GLFW_KEY_LEFT:
                                if (!onSideButtonKeybind(piece, param, SpellParam.Side.LEFT) && selectedX > 0) {
                                    selectedX--;
                                    onSelectedChanged();
                                }
                                break;
                            case GLFW.GLFW_KEY_RIGHT:
                                if (!onSideButtonKeybind(piece, param, SpellParam.Side.RIGHT) && selectedX < SpellGrid.GRID_SIZE - 1) {
                                    selectedX++;
                                    onSelectedChanged();
                                }
                                break;
                            case GLFW.GLFW_KEY_DOWN:
                                if (!onSideButtonKeybind(piece, param, SpellParam.Side.BOTTOM) && selectedY < SpellGrid.GRID_SIZE - 1) {
                                    selectedY++;
                                    onSelectedChanged();
                                }
                                break;
                            case GLFW.GLFW_KEY_ENTER:
                                openPanel();
                                break;
                        }
                    }
                }
            }
        }
        return false;
    }


    public boolean onSideButtonKeybind(SpellPiece piece, int param, SpellParam.Side side) {
        if (param > -1 && piece != null && piece.params.size() >= param) {
            for (Button b : configButtons) {
                GuiButtonSideConfig config = (GuiButtonSideConfig) b;
                if (config.matches(param, side)) {
                    if (side != SpellParam.Side.OFF && piece.paramSides.get(piece.params.get(config.paramName)) == side) {
                        side = SpellParam.Side.OFF;
                        continue;
                    }
                    config.onPress();
                    return true;
                }
            }
        }

        return side == SpellParam.Side.OFF;
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void openPanel() {
        closePanel();
        panelEnabled = true;
        page = Math.min(page, getPageCount() - 1);

        panelX = gridLeft + (selectedX + 1) * 18;
        panelY = gridTop;

        searchField.x = panelX + 18;
        searchField.y = panelY + 4;
        searchField.setText("");
        spellNameField.setFocused2(false);

        updatePanelButtons();
    }

	private void updatePanelButtons() {
        panelCursor = 0;
        buttons.removeAll(panelButtons);
        panelButtons.clear();
        visiblePieces.clear();

        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(minecraft.player);

        HashMap<Class<? extends SpellPiece>, Integer> rankings = new HashMap<>();


        String text = searchField.getText().toLowerCase().trim();
        boolean noSearchTerms = text.isEmpty();

        for (ResourceLocation key : PsiAPI.spellPieceRegistry.keySet()) {
            Class<? extends SpellPiece> clazz = PsiAPI.spellPieceRegistry.getValue(key).get();
            PieceGroup group = PsiAPI.groupsForPiece.get(clazz);
            //TODO Check if works in game
            if (!minecraft.player.isCreative() && (group == null || !data.isPieceGroupUnlocked(group.name, key.getPath())))
                continue;

            SpellPiece p = SpellPiece.create(clazz, spell);

            if (noSearchTerms)
                p.getShownPieces(visiblePieces);
            else {
                int rank = ranking(text, p);
                if (rank > 0) {
                    rankings.put(clazz, rank);
					p.getShownPieces(visiblePieces);
				}
			}
		}

		Comparator<SpellPiece> comparator;

		if (noSearchTerms)
			comparator = Comparator.comparing(SpellPiece::getSortingName);
		else {
			comparator = Comparator.comparingInt((p) -> -rankings.get(p.getClass()));
			comparator = comparator.thenComparing(SpellPiece::getSortingName);
		}


		visiblePieces.sort(comparator);
		if(!text.isEmpty() && text.length() <= 5 && (text.matches("\\d+(?:\\.\\d*)?") || text.matches("\\d*(?:\\.\\d+)?"))) {
			SpellPiece p = SpellPiece.create(PieceConstantNumber.class, spell);
			((PieceConstantNumber) p).valueStr = text;
			visiblePieces.add(0, p);
		}

		int start = page * PIECES_PER_PAGE;

		for(int i = start; i < visiblePieces.size(); i++) {
            int c = i - start;
            if (c >= PIECES_PER_PAGE)
                break;

            SpellPiece piece = visiblePieces.get(i);
            panelButtons.add(new GuiButtonSpellPiece(this, piece, panelX + 5 + c % 5 * 18, panelY + 20 + c / 5 * 18, button -> {
                pushState(true);
                SpellPiece piece1 = ((GuiButtonSpellPiece) button).piece.copy();
                if (piece1.getPieceType() == EnumPieceType.TRICK && spellNameField.getText().isEmpty()) {
                    String pieceName = I18n.format(piece1.getUnlocalizedName());
                    String patternStr = I18n.format("psimisc.trickPattern");
                    Pattern pattern = Pattern.compile(patternStr);
                    Matcher matcher = pattern.matcher(pieceName);
                    if (matcher.matches()) {
                        String spellName = matcher.group(1);
                        spell.name = spellName;
                        spellNameField.setText(spellName);
                    }
                }
                spell.grid.gridData[selectedX][selectedY] = piece1;
                piece1.isInGrid = true;
                piece1.x = selectedX;
                piece1.y = selectedY;
                onSpellChanged(false);
                closePanel();
            }));
        }

        if (page > 0)
            panelButtons.add(new GuiButtonPage(panelX + 4, panelY + panelHeight - 15, false, this, button -> {
                int max = getPageCount();
                int next = page + (((GuiButtonPage) button).right ? 1 : -1);

                if (next >= 0 && next < max) {
                    page = next;
                    scheduleButtonUpdate = true;
                }
            }));

        if (page < getPageCount() - 1)
            panelButtons.add(new GuiButtonPage(panelX + panelWidth - 22, panelY + panelHeight - 15, true, this, button -> {
                int max = getPageCount();
                int next = page + (((GuiButtonPage) button).right ? 1 : -1);

                if (next >= 0 && next < max) {
                    page = next;
                    scheduleButtonUpdate = true;
                }
            }));

        buttons.addAll(panelButtons);
    }

    /**
     * If a piece has a ranking of <= 0, it's excluded from the search.
     */

	private int ranking(String token, SpellPiece p) {
        int rank = 0;
        String name = I18n.format(p.getUnlocalizedName()).toLowerCase();
        String desc = I18n.format(p.getUnlocalizedDesc()).toLowerCase();

        for (String nameToken : token.split("\\s+")) {
            if (nameToken.isEmpty())
                continue;

            if (nameToken.startsWith("in:")) {
                String clippedToken = nameToken.substring(3);
                if (clippedToken.isEmpty())
                    continue;

				int maxRank = 0;
				for(SpellParam param : p.params.values()) {
					String type = param.getRequiredTypeString().getString().toLowerCase();
					maxRank = Math.max(maxRank, rankTextToken(type, clippedToken));
				}

				rank += maxRank;
			} else if (nameToken.startsWith("out:")) {
				String clippedToken = nameToken.substring(4);
				if (clippedToken.isEmpty())
					continue;

				String type = p.getEvaluationTypeString().getString();

				rank += rankTextToken(type, clippedToken);
			} else if (nameToken.startsWith("@")) {
				String clippedToken = nameToken.substring(1);
				if (clippedToken.isEmpty())
					continue;

				String mod = PsiAPI.pieceMods.get(p.getClass());
				if (mod != null) {
					int modRank = rankTextToken(mod, clippedToken);
					if (modRank <= 0)
						return 0;
					rank += modRank;
				} else
					return 0;
			} else {
				int nameRank = rankTextToken(name, nameToken);
				rank += nameRank;
				if (nameRank == 0)
					rank += rankTextToken(desc, nameToken) / 2;
			}
		}

		return rank;
	}

	private int rankTextToken(String haystack, String token) {
		if (token.isEmpty())
			return 0;

		if(token.startsWith("_")) {
			String clippedToken = token.substring(1);
			if (clippedToken.isEmpty())
				return 0;
			if (haystack.endsWith(clippedToken)) {
				if (!Character.isLetterOrDigit(haystack.charAt(haystack.length() - clippedToken.length() - 1)))
					return clippedToken.length() * 3 / 2;
				return clippedToken.length();
			}
		} else if (token.endsWith("_")) {
			String clippedToken = token.substring(0, token.length() - 1);
			if (clippedToken.isEmpty())
				return 0;
			if (haystack.startsWith(clippedToken)) {
				if (!Character.isLetterOrDigit(haystack.charAt(clippedToken.length() + 1)))
					return clippedToken.length() * 2;
				return clippedToken.length();
			}
		} else {
			if (token.startsWith("has:"))
				token = token.substring(4);

			int idx = haystack.indexOf(token);
			if (idx >= 0) {
				int multiplier = 2;
				if (idx == 0 || !Character.isLetterOrDigit(haystack.charAt(idx - 1)))
					multiplier += 2;
				if (idx + token.length() + 1 >= haystack.length() ||
						!Character.isLetterOrDigit(haystack.charAt(idx + token.length() + 1)))
					multiplier++;

				return token.length() * multiplier / 2;
			}
		}

		return 0;
	}

	private int getPageCount() {
		return visiblePieces.size() / PIECES_PER_PAGE + 1;
	}

	private void closePanel() {
        panelEnabled = false;
        buttons.removeAll(panelButtons);
        panelButtons.clear();
    }

	private void closeComment(boolean save) {
		SpellPiece piece = null;
		if(selectedX != -1 && selectedY != -1)
            piece = spell.grid.gridData[selectedX][selectedY];

        if (save && piece != null) {
            String text = commentField.getText();
            pushState(true);
            piece.comment = text;
            onSpellChanged(false);
        }

        spellNameField.setEnabled(!spectator && (piece == null || !piece.interceptKeystrokes()));
        commentField.setFocused2(false);
        commentField.setVisible(false);
        commentField.setEnabled(false);
        commentField.setText("");
        commentEnabled = false;
    }
	
	public void onSpellChanged(boolean nameOnly) {
		if (programmer != null) {
            if (!spectator) {
                MessageSpellModified message = new MessageSpellModified(programmer.getPos(), spell);
                MessageRegister.HANDLER.sendToServer(message);
            }

            programmer.spell = spell;
            programmer.onSpellChanged();
        }

        onSelectedChanged();
        spellNameField.setFocused2(nameOnly);

        if (!nameOnly || compiler != null && compiler.getError() != null && compiler.getError().equals(SpellCompilationException.NO_NAME) || spell.name.isEmpty())
            compiler = new SpellCompiler(spell);
    }

	public void pushState(boolean wipeRedo) {
		if(wipeRedo)
			redoSteps.clear();
		undoSteps.push(spell.copy());
		if(undoSteps.size() > 25)
			undoSteps.remove(0);
	}

	public void onSelectedChanged() {
        buttons.removeAll(configButtons);
        configButtons.clear();
        spellNameField.setEnabled(!spectator);
        spellNameField.setFocused2(false);

        if (selectedX != -1 && selectedY != -1) {
            SpellPiece piece = spell.grid.gridData[selectedX][selectedY];
            if (piece != null) {
                boolean intercept = piece.interceptKeystrokes();
                spellNameField.setEnabled(!spectator && !intercept);

                if (piece.hasConfig()) {
                    int i = 0;
                    for (String paramName : piece.params.keySet()) {
                        SpellParam param = piece.params.get(paramName);
                        int x = left - 17;
                        int y = top + 70 + i * 26;
                        for (SpellParam.Side side : ImmutableSet.of(SpellParam.Side.TOP, SpellParam.Side.BOTTOM, SpellParam.Side.LEFT, SpellParam.Side.RIGHT, SpellParam.Side.OFF)) {
                            if (!side.isEnabled() && !param.canDisable)
                                continue;

                            int xp = x + side.offx * 8;
                            int yp = y + side.offy * 8;
                            configButtons.add(new GuiButtonSideConfig(this, selectedX, selectedY, i, paramName, side, xp, yp, button -> {
                                if (!spectator) {
                                    pushState(true);
                                    ((GuiButtonSideConfig) button).onClick();
                                    onSpellChanged(false);
                                }
                            }));
                        }

                        i++;
                    }

                    buttons.addAll(configButtons);
                    configEnabled = true;
                    return;
                }
			}
		}

		configEnabled = false;
	}

}
