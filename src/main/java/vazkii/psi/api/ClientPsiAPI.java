/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api;

import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static vazkii.psi.api.PsiAPI.MOD_ID;

@OnlyIn(Dist.CLIENT)
public class ClientPsiAPI {

	public static final ResourceLocation PSI_PIECE_TEXTURE_ATLAS = new ResourceLocation(MOD_ID, "spell_pieces");
	private static final Map<ResourceLocation, RenderMaterial> simpleSpellTextures = new HashMap<>();

	/**
	 * Register the texture of a piece
	 *
	 * @param pieceId ID of the piece whose texture to register
	 * @param texture Path to the piece's texture, where <code>domain:foo/bar</code> translates to
	 *                <code>/assets/domain/textures/foo/bar.png</code>.
	 *                In other words, do <b>not</b> prefix with textures/ nor suffix with .png.
	 */
	@OnlyIn(Dist.CLIENT)
	public static void registerPieceTexture(ResourceLocation pieceId, ResourceLocation texture) {
		ClientPsiAPI.simpleSpellTextures.put(pieceId, new RenderMaterial(PSI_PIECE_TEXTURE_ATLAS, texture));
	}

	public static RenderMaterial getSpellPieceMaterial(ResourceLocation key) {
		return simpleSpellTextures.get(key);
	}

	public static Collection<RenderMaterial> getAllSpellPieceMaterial() {
		return simpleSpellTextures.values();
	}

}
