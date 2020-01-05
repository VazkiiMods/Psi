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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.CrashReportHandler;
import vazkii.psi.common.core.handler.InternalMethodHandler;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.core.handler.capability.CapabilityHandler;
import vazkii.psi.common.crafting.ModCraftingRecipes;
import vazkii.psi.common.entity.ModEntities;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.network.GuiHandler;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.spell.base.ModSpellPieces;

import java.awt.*;

@SuppressWarnings("EmptyMethod")
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		PsiAPI.internalHandler = new InternalMethodHandler();

		FMLCommonHandler.instance().registerCrashCallable(new CrashReportHandler());

		ConfigHandler.init(event.getSuggestedConfigurationFile());

		ModItems.preInit();
		ModEntities.init();
		ModSpellPieces.init();
		ModCraftingRecipes.init();
		PsiSoundHandler.init();

		CapabilityHandler.register();
		MessageRegister.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(Psi.instance, new GuiHandler());
	}
	
	public PlayerEntity getClientPlayer() {
		return null;
	}

	public void onLevelUp(int level) {
		// proxy override
	}

	public void savePersistency() {
		// proxy override
	}

	public int getColorForCAD(ItemStack cadStack) {
		return -1;
	}

	public int getColorForColorizer(ItemStack colorizer) {
		return -1;
	}

	@Deprecated
	public Color getCADColor(ItemStack cadStack) {
		return new Color(getColorForCAD(cadStack));
	}

	@Deprecated
	public Color getColorizerColor(ItemStack colorizer) {
		return new Color(getColorForColorizer(colorizer));
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

	public void sparkleFX(double x, double y, double z, float r, float g, float b, float size, int m) {
		sparkleFX(x, y, z, r, g, b, 0, 0, 0, size, m);
	}

	public void sparkleFX(double x, double y, double z, float r, float g, float b, float gravity, float size, int m) {
		sparkleFX(x, y, z, r, g, b, 0, -gravity, 0, size, m);
	}

	public void sparkleFX(double x, double y, double z, float r, float g, float b, float motionx, float motiony, float motionz, float size, int m) {
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


	public void wispFX(double x, double y, double z, float r, float g, float b, float size) {
		wispFX(x, y, z, r, g, b, size, 0F);
	}

	public void wispFX(double x, double y, double z, float r, float g, float b, float size, float gravity) {
		wispFX(x, y, z, r, g, b, size, gravity, 1F);
	}

	public void wispFX(double x, double y, double z, float r, float g, float b, float size, float gravity, float maxAgeMul) {
		wispFX(x, y, z, r, g, b, size, 0, -gravity, 0, maxAgeMul);
	}

	public void wispFX(double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz) {
		wispFX(x, y, z, r, g, b, size, motionx, motiony, motionz, 1F);
	}

	public void wispFX(double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
		// Proxy override
	}

	@SuppressWarnings("deprecation")
	public String localize(String key, Object... arguments) {
		return net.minecraft.util.text.translation.I18n.translateToLocalFormatted(key, arguments);
	}

}
