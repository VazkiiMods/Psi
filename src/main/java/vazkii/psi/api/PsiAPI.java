/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [13/01/2016, 16:08:02 (GMT)]
 */
package vazkii.psi.api;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.DummyMethodHandler;
import vazkii.psi.api.internal.IInternalMethodHandler;
import vazkii.psi.api.spell.SpellPiece;

public final class PsiAPI {
	
	/**
	 * The internal method handler in use. This object allows the API to interact with the mod.
	 * By default this is a dummy. In the mod itself, this is replaced with an implementation that
	 * can handle all of its queries.<br><br>
	 * 
	 * <b>DO NOT EVER, EVER, OVERWRITE THIS VALUE</b>
	 */
	public static IInternalMethodHandler internalHandler = new DummyMethodHandler();

	public static RegistryNamespaced<String, Class<? extends SpellPiece>> spellPieceRegistry = new RegistryNamespaced();
	public static HashMap<String, ResourceLocation> simpleSpellTextures = new HashMap();
	
	public static void registerSpellPiece(String key, Class<? extends SpellPiece> clazz) {
		spellPieceRegistry.putObject(key, clazz);
	}
	
	public static void registerSpellPieceAndTexture(String key, Class<? extends SpellPiece> clazz) {
		String currMod = Loader.instance().activeModContainer().getModId().toLowerCase();
		registerSpellPieceAndTexture(key, currMod, clazz);
	}
	
	public static void registerSpellPieceAndTexture(String key, String mod, Class<? extends SpellPiece> clazz) {
		registerSpellPiece(key, clazz);
		simpleSpellTextures.put(key, new ResourceLocation(mod, String.format("textures/spell/%s.png", key)));
	}
	
	/**
	 * Gets the CAD the passed EntityPlayer is using. As a player can only have one CAD, if there's
	 * more than one, this will return null.
	 */
	public static ItemStack getPlayerCAD(EntityPlayer player) {
		if(player == null)
			return null;
		
		ItemStack cad = null;
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stackAt = player.inventory.getStackInSlot(i);
			if(stackAt != null && stackAt.getItem() instanceof ICAD) {
				if(cad != null)
					return null; // Player can only have one CAD
				
				cad = stackAt;
			}
		}
		
		return cad;
	}
	
}
