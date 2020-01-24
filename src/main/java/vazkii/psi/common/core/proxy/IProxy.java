package vazkii.psi.common.core.proxy;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.world.World;

import java.awt.*;

public interface IProxy {
    default void registerHandlers() {
    }

    boolean isTheClientPlayer(LivingEntity entity);

    PlayerEntity getClientPlayer();

    long getWorldElapsedTicks();

    int getClientRenderDistance();

    // Side-safe version of world.addParticle with noDistanceLimit flag set to true
    default void addParticleForce(World world, IParticleData particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
    }

    void onLevelUp(int level);

    void savePersistency();

    default int getColorForCAD(ItemStack cadStack) {
        return -1;
    }

    default int getColorForColorizer(ItemStack colorizer) {
        return -1;
    }

    @Deprecated
    default Color getCADColor(ItemStack cadStack) {
        return new Color(getColorForCAD(cadStack));
    }

    @Deprecated
    default Color getColorizerColor(ItemStack colorizer) {
        return new Color(getColorForColorizer(colorizer));
    }

    void setWispFXDistanceLimit(boolean limit);

    void setWispFXDepthTest(boolean depth);

    default void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m) {
        sparkleFX(world, x, y, z, r, g, b, 0, 0, 0, size, m);
    }

    default void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float gravity, float size, int m) {
        sparkleFX(world, x, y, z, r, g, b, 0, -gravity, 0, size, m);
    }

    void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float motionx, float motiony, float motionz, float size, int m);

    default void sparkleFX(double x, double y, double z, float r, float g, float b, float size, int m) {
        sparkleFX(x, y, z, r, g, b, 0, 0, 0, size, m);
    }

    default void sparkleFX(double x, double y, double z, float r, float g, float b, float gravity, float size, int m) {
        sparkleFX(x, y, z, r, g, b, 0, -gravity, 0, size, m);
    }

    void sparkleFX(double x, double y, double z, float r, float g, float b, float motionx, float motiony, float motionz, float size, int m);

    default void wispFX(World world, double x, double y, double z, float r, float g, float b, float size) {
        wispFX(world, x, y, z, r, g, b, size, 0F);
    }

    default void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float gravity) {
        wispFX(world, x, y, z, r, g, b, size, gravity, 1F);
    }

    default void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float gravity, float maxAgeMul) {
        wispFX(world, x, y, z, r, g, b, size, 0, -gravity, 0, maxAgeMul);
    }

    default void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz) {
        wispFX(world, x, y, z, r, g, b, size, motionx, motiony, motionz, 1F);
    }

    void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul);


    default void wispFX(double x, double y, double z, float r, float g, float b, float size) {
        wispFX(x, y, z, r, g, b, size, 0F);
    }

    default void wispFX(double x, double y, double z, float r, float g, float b, float size, float gravity) {
        wispFX(x, y, z, r, g, b, size, gravity, 1F);
    }

    default void wispFX(double x, double y, double z, float r, float g, float b, float size, float gravity, float maxAgeMul) {
        wispFX(x, y, z, r, g, b, size, 0, -gravity, 0, maxAgeMul);
    }

    default void wispFX(double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz) {
        wispFX(x, y, z, r, g, b, size, motionx, motiony, motionz, 1F);
    }

    void wispFX(double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul);


}
