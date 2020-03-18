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
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.PieceGroup;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageDataSync;

import java.util.List;

public class CommandPsiUnlearn extends CommandPsiLearn {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("psi-unlearn")
                        .requires(s -> s.hasPermissionLevel(2))
                        .then(Commands.argument("group", StringArgumentType.string())
								.suggests((ctx, sb) -> ISuggestionProvider.suggest(getGroups().stream(), sb))
								.executes(ctx -> run(ctx, true))
								.then(Commands.argument("player", EntityArgument.player())
										.executes(ctx -> run(ctx, false))))
        );
    }

    public static int run(CommandContext<CommandSource> ctx, boolean onSelf) throws CommandSyntaxException {
		PlayerEntity player = onSelf ? ctx.getSource().asPlayer() : EntityArgument.getPlayer(ctx, "player");
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
		String group = StringArgumentType.getString(ctx, "group");
		CommandSource sender = ctx.getSource();
		if (group.equals("*")) {
			applyAll(data, player, sender);
			MessageRegister.HANDLER.sendToPlayer(new MessageDataSync(data), (ServerPlayerEntity) player);
		} else if (!getGroups().contains(group))
			error("not_a_group", group);
		else if (shouldNotApply(player, group))
			error("should_not", player.getDisplayName(), group);
		else {
			applyPlayerData(player, data, group, sender);
			MessageRegister.HANDLER.sendToPlayer(new MessageDataSync(data), (ServerPlayerEntity) player);
		}
		return 1;
	}

	public static String localizationKey() {
		return "command." + LibMisc.MOD_ID + ".unlearn";
	}

	protected static void notify(CommandSource sender, String result, Object... format) {
		sender.sendFeedback(new TranslationTextComponent(localizationKey() + "." + result, format), false);
	}

	protected static void error(String result, Object... format) throws CommandException {
		throw new CommandException(new TranslationTextComponent(localizationKey() + "." + result, format));
	}

	public static void applyPlayerData(PlayerEntity player, PlayerDataHandler.PlayerData data, String group, CommandSource sender) {
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

    public static void applyAll(PlayerDataHandler.PlayerData data, PlayerEntity player, CommandSource sender) {
        lockAll(data);
        notify(sender, "success.all", player.getDisplayName());
    }
}
