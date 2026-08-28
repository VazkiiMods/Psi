/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.platform;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.core.handler.PlayerData;
import vazkii.psi.common.platform.PsiPlayerDataStorageService;

public final class NeoForgePsiPlayerDataStorageService implements PsiPlayerDataStorageService {

	private static final String LEGACY_DATA_TAG = "PsiData";

	private DeferredHolder<AttachmentType<?>, AttachmentType<PlayerData>> type;

	@Override
	public void initialize() {
		if(type != null) {
			throw new IllegalStateException("NeoForge player data storage was initialized more than once");
		}
		DeferredRegister<AttachmentType<?>> attachments = DeferredRegister.create(
				NeoForgeRegistries.Keys.ATTACHMENT_TYPES, PsiAPI.MOD_ID);
		type = attachments.register("player_data", () -> AttachmentType.builder(() -> new PlayerData())
				.serialize(PlayerData.CODEC)
				.copyOnDeath()
				.build());
		attachments.register(NeoForgePsiPlatform.modBus());
	}

	@Override
	public PlayerData get(Player player) {
		if(!player.hasData(type)) {
			CompoundTag legacy = legacyData(player);
			if(legacy != null) {
				PlayerData imported = new PlayerData();
				imported.readFromNBT(legacy);
				player.setData(type, imported);
			}
		}
		PlayerData data = player.getData(type);
		data.bind(player);
		return data;
	}

	@Override
	public void save(Player player, PlayerData data) {
		player.setData(type, data);
	}

	private static CompoundTag legacyData(Player player) {
		CompoundTag forgeData = player.getPersistentData();
		if(!forgeData.contains(Player.PERSISTED_NBT_TAG, Tag.TAG_COMPOUND)) {
			return null;
		}
		CompoundTag persisted = forgeData.getCompound(Player.PERSISTED_NBT_TAG);
		return persisted.contains(LEGACY_DATA_TAG, Tag.TAG_COMPOUND)
				? persisted.getCompound(LEGACY_DATA_TAG)
				: null;
	}

}
