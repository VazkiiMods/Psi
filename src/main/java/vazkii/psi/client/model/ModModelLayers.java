/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;

import vazkii.psi.common.Psi;

public class ModModelLayers {

	public static final ModelLayerLocation PSIMETAL_EXOSUIT_INNER_ARMOR = make("inner_armor");
	public static final ModelLayerLocation PSIMETAL_EXOSUIT_OUTER_ARMOR = make("outer_armor");

	private static ModelLayerLocation make(String layer) {
		// Don't add to vanilla's ModelLayers. It seems to only be used for error checking
		// And would be annoying to do under Forge's parallel mod loading
		return new ModelLayerLocation(Psi.location("psimetal_exosuit"), layer);
	}
}
