/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@OnlyIn(Dist.CLIENT)
public class ClientPsiAPI {

	//public static final ResourceLocation PSI_PIECE_TEXTURE_ATLAS = ResourceLocation.fromNamespaceAndPath(MOD_ID, "spell_pieces");
	private static final Map<ResourceLocation, Material> simpleSpellTextures = new ConcurrentHashMap<>();

	/**
	 * Register the texture of a piece
	 * <p>
	 * On Forge, call this at any time before registry events finish (e.g. during item registration).
	 * Note that common setup event is <em>too late</em>!
	 * <p>
	 * NB: Why the strange restriction? Because in 1.16.2+ forge, texture stitching, model baking, etc. go on
	 * <em>concurrently</em> with all setup events (client, common). So by then, it is way too late to tell the game
	 * to load these textures. Registry events are the final thing that runs serially until loading is done, so
	 * we have to receive all registrations by then. This makes the setup events, particularly the client one,
	 * pretty much useless for doing anything that interacts with the vanilla game. Awesome system design right there!
	 *
	 * @param pieceId ID of the piece whose texture to register
	 * @param texture Path to the piece's texture, where <code>domain:foo/bar</code> translates to
	 *                <code>/assets/domain/textures/foo/bar.png</code>.
	 *                In other words, do <b>not</b> prefix with textures/ nor suffix with .png.
	 */
	@OnlyIn(Dist.CLIENT)
	public static void registerPieceTexture(ResourceLocation pieceId, ResourceLocation texture) {
		ClientPsiAPI.simpleSpellTextures.put(pieceId, new Material(TextureAtlas.LOCATION_BLOCKS, texture));
	}

	public static Material getSpellPieceMaterial(ResourceLocation key) {
		return simpleSpellTextures.get(key);
	}

	public static Collection<Material> getAllSpellPieceMaterial() {
		return simpleSpellTextures.values();
	}

}
