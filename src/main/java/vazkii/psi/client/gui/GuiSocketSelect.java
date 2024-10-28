/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.cad.ISocketableController;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.client.core.handler.KeybindHandler;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageChangeControllerSlot;
import vazkii.psi.common.network.message.MessageChangeSocketableSlot;

import java.util.ArrayList;
import java.util.List;

public class GuiSocketSelect extends Screen {

    final Minecraft mc;
    float timeIn = 0;
    int slotSelected = -1;
    ItemStack controllerStack;
    ISocketableController controller;
    ItemStack[] controlledStacks;
    int controlSlot;
    ItemStack socketableStack;
    ISocketable socketable;
    List<Integer> slots;
    List<ResourceLocation> signs;

    public GuiSocketSelect(ItemStack stack) {
        super(Component.empty());
        mc = Minecraft.getInstance();

        controllerStack = ItemStack.EMPTY;
        socketableStack = ItemStack.EMPTY;

        if (ISocketable.isSocketable(stack)) {
            setSocketable(stack);
        } else if (stack.getItem() instanceof ISocketableController) {
            controllerStack = stack;
            controller = (ISocketableController) stack.getItem();
            controlledStacks = controller.getControlledStacks(mc.player, stack);
            controlSlot = controller.getDefaultControlSlot(controllerStack);
            if (controlSlot >= controlledStacks.length) {
                controlSlot = 0;
            }

            setSocketable(controlledStacks.length == 0 ? ItemStack.EMPTY : controlledStacks[controlSlot]);
        }
    }

    private static double mouseAngle(int x, int y, int mx, int my) {
        return (Mth.atan2(my - y, mx - x) + Math.PI * 2) % (Math.PI * 2);
    }

    public void setSocketable(ItemStack stack) {
        if (stack.isEmpty()) {
            slots = new ArrayList<>();
            return;
        }

        socketableStack = stack;
        socketable = ISocketable.socketable(stack);
        slots = socketable.getRadialMenuSlots();
        signs = socketable.getRadialMenuIcons();
    }

    @Override
    public void render(GuiGraphics graphics, int mx, int my, float delta) {
        super.render(graphics, mx, my, delta);

        timeIn += delta;

        int x = width / 2;
        int y = height / 2;
        int maxRadius = 80;

        double angle = mouseAngle(x, y, mx, my);

        int segments = slots.size();
        float step = (float) Math.PI / 180;
        float degPer = (float) Math.PI * 2 / segments;

        ItemStack cadStack = PsiAPI.getPlayerCAD(Minecraft.getInstance().player);
        slotSelected = -1;

        Tesselator tess = Tesselator.getInstance();
        RenderSystem.disableCull();
        //RenderSystem.disableTexture();  TODO(Kamefrede): 1.20 figure out what this breaks
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        if(segments != 0) {
            BufferBuilder buf = tess.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);

            for (int seg = 0; seg < segments; seg++) {
                boolean mouseInSector = degPer * seg < angle && angle < degPer * (seg + 1);
                float radius = Math.max(0F, Math.min((timeIn - ((float) seg * 6F / (float) segments)) * 40F, (float) maxRadius));
                if (mouseInSector || seg == socketable.getSelectedSlot()) {
                    radius *= 1.025f;
                }

                int gs = 0x40;
                if (seg % 2 == 0) {
                    gs += 0x19;
                }
                int r = gs;
                int g = gs;
                int b = gs;
                int a = 0x66;

                if (seg == 0) {
                    buf.addVertex(x, y, 0).setColor(r, g, b, a);
                }

                if (mouseInSector) {
                    slotSelected = seg;

                    if (!cadStack.isEmpty()) {
                        int color = Psi.proxy.getColorForCAD(cadStack);
                        r = PsiRenderHelper.r(color);
                        g = PsiRenderHelper.g(color);
                        b = PsiRenderHelper.b(color);
                    } else {
                        r = g = b = 0xFF;
                    }
                } else if (seg == socketable.getSelectedSlot()) {
                    if (!cadStack.isEmpty()) {
                        int color = Psi.proxy.getColorForCAD(cadStack);
                        r = 0xFF - PsiRenderHelper.r(color);
                        g = 0xFF - PsiRenderHelper.g(color);
                        b = 0xFF - PsiRenderHelper.b(color);
                    } else {
                        r = 0x00;
                        g = 0xFF;
                        b = 0x00;
                    }
                }

                for (float i = 0; i < degPer + step / 2; i += step) {
                    float rad = i + seg * degPer;
                    float xp = x + Mth.cos(rad) * radius;
                    float yp = y + Mth.sin(rad) * radius;

                    if (i == 0) {
                        buf.addVertex(xp, yp, 0).setColor(r, g, b, a);
                    }
                    buf.addVertex(xp, yp, 0).setColor(r, g, b, a);
                }
            }
            BufferUploader.drawWithShader(buf.buildOrThrow());
        }
        // RenderSystem.enableTexture(); TODO(Kamefrede): 1.20 figure out what this breaks

        for (int seg = 0; seg < segments; seg++) {
            boolean mouseInSector = degPer * seg < angle && angle < degPer * (seg + 1);
            float radius = Math.max(0F, Math.min((timeIn - ((float) seg * 6F / (float) segments)) * 40F, (float) maxRadius));
            if (mouseInSector || seg == socketable.getSelectedSlot()) {
                radius *= 1.025f;
            }

            float rad = (seg + 0.5f) * degPer;
            float xp = x + Mth.cos(rad) * radius;
            float yp = y + Mth.sin(rad) * radius;

            ItemStack stack = socketable.getBulletInSocket(seg);
            if (!stack.isEmpty()) {
                float xsp = xp - 4;
                float ysp = yp;
                String name = (mouseInSector ? ChatFormatting.UNDERLINE : ChatFormatting.RESET) + stack.getHoverName().getString();
                int width = font.width(name);

                double mod = 0.6;
                int xdp = (int) ((xp - x) * mod + x);
                int ydp = (int) ((yp - y) * mod + y);

                graphics.renderFakeItem(stack, xdp - 8, ydp - 8);

                if (xsp < x) {
                    xsp -= width - 8;
                }
                if (ysp < y) {
                    ysp -= 9;
                }

                graphics.drawString(this.font, name, xsp, ysp, 0xFFFFFF, true);
                if (seg == socketable.getSelectedSlot()) {
                    int color = 0x00FF00;
                    if (!cadStack.isEmpty()) {
                        color = 0xFF0000 - Psi.proxy.getColorForCAD(cadStack);
                    }
                    graphics.drawString(this.font, I18n.get("psimisc.selected"), xsp + width / 4, ysp + font.lineHeight, color, true);
                }

                mod = 0.8;
                xdp = (int) ((xp - x) * mod + x);
                ydp = (int) ((yp - y) * mod + y);

                graphics.blit(signs.get(seg), xdp - 8, ydp - 8, 0, 0, 16, 16, 16, 16);
            }
        }

        float shift = Math.min(5, timeIn) / 5;
        float scale = 3 * shift;
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        //RenderSystem.enableLighting();
        //RenderSystem.enableColorMaterial();

        if (controlledStacks != null && controlledStacks.length > 0) {
            int xs = width / 2 - 18 * controlledStacks.length / 2 + 1;
            int ys = height / 2;

            for (int i = 0; i < controlledStacks.length; i++) {
                float yoff = 20F + maxRadius;
                if (i == controlSlot) {
                    yoff += 5F;
                }

                ItemStack stack = controlledStacks[i];
                int rx = xs + i * 18;
                float ry = ys + (-yoff * shift);
                graphics.renderFakeItem(stack, rx, (int) ry);
            }

        }

        if (!socketableStack.isEmpty()) {
            graphics.pose().pushPose();
            graphics.pose().scale(scale, scale, scale);
            graphics.renderFakeItem(socketableStack,
                    (int) (x / scale) - 8, (int) (y / scale) - 8);
            graphics.pose().popPose();
        }
        //Lighting.turnOff();
        RenderSystem.disableBlend();
        //RenderSystem.disableRescaleNormal();

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (!controllerStack.isEmpty() && controlledStacks.length > 0) {
            if (mouseButton == 0) {
                controlSlot++;
                if (controlSlot >= controlledStacks.length) {
                    controlSlot = 0;
                }
            } else if (mouseButton == 1) {
                controlSlot--;
                if (controlSlot < 0) {
                    controlSlot = controlledStacks.length - 1;
                }
            }

            setSocketable(controlledStacks[controlSlot]);
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!isKeyDown(KeybindHandler.keybind)) {
            mc.setScreen(null);
            if (slotSelected != -1) {
                int slot = slots.get(slotSelected);
                PlayerDataHandler.get(mc.player).stopLoopcast();

                CustomPacketPayload message;
                if (!controllerStack.isEmpty()) {
                    message = new MessageChangeControllerSlot(controlSlot, slot);
                } else {
                    message = new MessageChangeSocketableSlot(slot);
                }
                MessageRegister.sendToServer(message);
            }
        }

        ImmutableSet<KeyMapping> set = ImmutableSet.of(mc.options.keyUp, mc.options.keyLeft, mc.options.keyDown, mc.options.keyRight, mc.options.keyShift, mc.options.keySprint, mc.options.keyJump);
        for (KeyMapping k : set) {
            KeyMapping.set(k.getKey(), isKeyDown(k));
        }
    }

    public boolean isKeyDown(KeyMapping keybind) {
        InputConstants.Key key = keybind.getKey();
        if (key.getType() == InputConstants.Type.MOUSE) {
            return GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), key.getValue()) == 1;
        }
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getValue());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
