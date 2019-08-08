/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [21/12/2018, 06:21:33 (GMT)]
 */
package vazkii.psi.common.command;

import com.google.common.collect.Lists;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.PlayerEntity;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.PieceGroup;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;

public class CommandPsiUnlearn extends CommandPsiLearn {
	@Nonnull
	@Override
	public String getName() {
		return "psi-unlearn";
	}

	@Override
	public String localizationKey() {
		return "command." + LibMisc.MOD_ID + ".unlearn";
	}

	@Override
	public boolean shouldNotApply(PlayerEntity player, String group) {
		return !super.shouldNotApply(player, group);
	}

	@Override
	public void applyPlayerData(PlayerEntity player, PlayerDataHandler.PlayerData data, String group, ICommandSender sender) {
		if (group.equals(level0)) {
			lockPieceGroup(data, group);
			notify(sender, "success", player.getDisplayName(), getGroupComponent(group));
		} else if (getGroups().contains(group)) {
			List<String> superGroups = Lists.newArrayList(data.spellGroupsUnlocked);
			for (String superGroup : superGroups) {
				if (data.isPieceGroupUnlocked(superGroup)) {
					PieceGroup superPieceGroup = PsiAPI.groupsForName.get(group);
					if (superPieceGroup != null && superPieceGroup.requirements.contains(group))
						applyPlayerData(player, data, superGroup, sender);
				}

			}

			if (data.isPieceGroupUnlocked(group)) {
				lockPieceGroup(data, group);
				notify(sender, "success", player.getDisplayName(), getGroupComponent(group));
			}
		}
	}

	@Override
	public void applyAll(PlayerDataHandler.PlayerData data, PlayerEntity player, ICommandSender sender) {
		lockAll(data);
		notify(sender, "success.all", player.getDisplayName());
	}
}
