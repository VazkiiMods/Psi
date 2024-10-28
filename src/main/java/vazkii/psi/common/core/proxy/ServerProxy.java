/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.proxy;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import vazkii.psi.common.block.tile.TileProgrammer;

public class ServerProxy implements IProxy {
    @Override
    public Player getClientPlayer() {
        return null;
    }

    @Override
    public long getWorldElapsedTicks() {
        return ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).getGameTime();
    }

    @Override
    public int getClientRenderDistance() {
        return 0;
    }

    @Override
    public void sparkleFX(Level world, double x, double y, double z, float r, float g, float b, float motionx, float motiony, float motionz, float size, int m) {
        //NOOP
    }

    @Override
    public void sparkleFX(double x, double y, double z, float r, float g, float b, float motionx, float motiony, float motionz, float size, int m) {
        //NOOP
    }

    @Override
    public void wispFX(Level world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
        //NOOP
    }

    @Override
    public void wispFX(double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
        //NOOP
    }

    @Override
    public void openProgrammerGUI(TileProgrammer programmer) {
        //NOOP
    }

    @Override
    public boolean hasAdvancement(ResourceLocation advancement, Player playerEntity) {
        if (playerEntity instanceof ServerPlayer serverPlayerEntity) {
            return serverPlayerEntity.getServer().getAdvancements().get(advancement) != null && serverPlayerEntity.getAdvancements().getOrStartProgress(serverPlayerEntity.getServer().getAdvancements().get(advancement)).isDone();
        }
        return false;
    }
}
