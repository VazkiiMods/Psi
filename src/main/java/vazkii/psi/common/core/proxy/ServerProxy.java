package vazkii.psi.common.core.proxy;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import vazkii.psi.common.block.tile.TileProgrammer;

public class ServerProxy implements IProxy {
    @Override
    public boolean isTheClientPlayer(LivingEntity entity) {
        return false;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return null;
    }

    @Override
    public long getWorldElapsedTicks() {
        return ServerLifecycleHooks.getCurrentServer().getWorld(DimensionType.OVERWORLD).getGameTime();
    }

    @Override
    public int getClientRenderDistance() {
        return 0;
    }

    @Override
    public void onLevelUp(int level) {
        //NOOP
    }

    @Override
    public void savePersistency() {
        //NOOP
    }

    @Override
    public void setWispFXDistanceLimit(boolean limit) {
        //NOOP
    }

    @Override
    public void setWispFXDepthTest(boolean depth) {
        //NOOP
    }

    @Override
    public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float motionx, float motiony, float motionz, float size, int m) {
        //NOOP
    }

    @Override
    public void sparkleFX(double x, double y, double z, float r, float g, float b, float motionx, float motiony, float motionz, float size, int m) {
        //NOOP
    }

	@Override
	public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
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
}
