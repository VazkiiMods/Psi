/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [08/01/2016, 21:22:59 (GMT)]
 */
package vazkii.psi.common.core.proxy;

import java.awt.Color;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.InternalMethodHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.crafting.ModCraftingRecipes;
import vazkii.psi.common.entity.ModEntities;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.network.GuiHandler;
import vazkii.psi.common.network.NetworkHandler;
import vazkii.psi.common.spell.base.ModSpellPieces;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		PsiAPI.internalHandler = new InternalMethodHandler();

		ConfigHandler.init(event.getSuggestedConfigurationFile());

		ModItems.init();
		ModBlocks.init();
		ModEntities.init();
		ModSpellPieces.init();
		ModCraftingRecipes.init();

		NetworkHandler.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(Psi.instance, new GuiHandler());

		MinecraftForge.EVENT_BUS.register(new PlayerDataHandler.EventHandler());
	}

	public EntityPlayer getClientPlayer() {
		return null;
	}

	public void onLevelUp(int level) {
		// proxy override
	}

	public void savePersistency() {
		// proxy override
	}

	public Color getCADColor(ItemStack cadStack) {
		return new Color(1F, 1F, 1F);
	}

	public Color getColorizerColor(ItemStack colorizer) {
		return new Color(1F, 1F, 1F);
	}

	public void setSparkleFXNoClip(boolean noclip) {
		// Proxy override
	}

	public void setWispFXDistanceLimit(boolean limit) {
		// Proxy override
	}

	public void setWispFXDepthTest(boolean depth) {
		// Proxy override
	}

	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m) {
		sparkleFX(world, x, y, z, r, g, b, 0, 0, 0, size, m);
	}

	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float gravity, float size, int m) {
		sparkleFX(world, x, y, z, r, g, b, 0, -gravity, 0, size, m);
	}

	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float motionx, float motiony, float motionz, float size, int m) {
		// Proxy override
	}

	public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size) {
		wispFX(world, x, y, z, r, g, b, size, 0F);
	}

	public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float gravity) {
		wispFX(world, x, y, z, r, g, b, size, gravity, 1F);
	}

	public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float gravity, float maxAgeMul) {
		wispFX(world, x, y, z, r, g, b, size, 0, -gravity, 0, maxAgeMul);
	}

	public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz) {
		wispFX(world, x, y, z, r, g, b, size, motionx, motiony, motionz, 1F);
	}

	public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
		// Proxy override
	}

}
