/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [13/01/2016, 17:01:34 (GMT)]
 */
package vazkii.psi.api.internal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * This is a dummy class. You'll never interact with it, it's just here so
 * in case something goes really wrong the field isn't null.
 */
public final class DummyMethodHandler implements IInternalMethodHandler {

	@Override
	public IPlayerData getDataForPlayer(EntityPlayer player) {
		return new DummyPlayerData();
	}

	@Override
	public ResourceLocation getProgrammerTexture() {
		return new ResourceLocation("");
	}

}
