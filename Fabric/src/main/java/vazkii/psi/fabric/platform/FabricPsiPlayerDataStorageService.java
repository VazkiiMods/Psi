/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.fabric.platform;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.world.entity.player.Player;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.core.handler.PlayerData;
import vazkii.psi.common.platform.PsiPlayerDataStorageService;

public final class FabricPsiPlayerDataStorageService implements PsiPlayerDataStorageService {

	private AttachmentType<PlayerData> type;

	@Override
	public void initialize() {
		if(type != null) {
			throw new IllegalStateException("Fabric player data storage was initialized more than once");
		}
		type = AttachmentRegistry.create(PsiAPI.location("player_data"), builder -> builder
				.initializer(PlayerData::new)
				.persistent(PlayerData.CODEC)
				.copyOnDeath());
	}

	@Override
	public PlayerData get(Player player) {
		PlayerData data = target(player).getAttachedOrCreate(type);
		data.bind(player);
		return data;
	}

	@Override
	public void save(Player player, PlayerData data) {
		target(player).setAttached(type, data);
	}

	private static AttachmentTarget target(Player player) {
		return (AttachmentTarget) player;
	}

}
