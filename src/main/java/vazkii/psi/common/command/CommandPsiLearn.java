/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [21/12/2018, 05:52:23 (GMT)]
 */
package vazkii.psi.common.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.HoverEvent;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.PieceGroup;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageDataSync;

import javax.annotation.Nonnull;
import java.util.List;

public class CommandPsiLearn {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("psi-learn")
                        .requires(s -> s.hasPermissionLevel(2))
                        .then(Commands.argument("group", StringArgumentType.string())
                                .suggests((ctx, sb) -> ISuggestionProvider.suggest(getGroups().stream(), sb))
                                .executes(ctx -> run(ctx, true))
                                .then(Commands.argument("player", EntityArgument.player())
                                     .executes(ctx -> run (ctx, false))))
        );
    }

    public static final String level0 = "psidust";

    private static List<String> groups;

    public static List<String> getGroups() {
        if (groups == null) {
            groups = Lists.newArrayList(level0);
            groups.addAll(PsiAPI.groupsForName.keySet());
        }
        return groups;
    }

    public static void lockPieceGroup(PlayerDataHandler.PlayerData data, String group) {
        if (hasGroup(data, group)) {
            if (group.equals(level0)) {
                data.level = 0;
            } else {
                data.spellGroupsUnlocked.remove(group);
                data.level--;

			}
			data.save();
		}
	}

	public static void unlockPieceGroupFree(PlayerDataHandler.PlayerData data, String group) {
		if (!hasGroup(data, group)) {
			if (group.equals(level0)) {
				if (data.level == 0) {
					data.level++;
					data.levelPoints = 1;
					data.lastSpellGroup = "";
					data.learning = false;
				}
			} else {
				data.spellGroupsUnlocked.add(group);
				data.lastSpellGroup = "";
				data.level++;
				data.learning = false;

			}
			data.save();
		}
	}

	public static void unlockAll(PlayerDataHandler.PlayerData data) {
		for (String group : getGroups()) {
			if (!hasGroup(data, group))
				unlockPieceGroupFree(data, group);
		}
		data.lastSpellGroup = "";
		data.learning = false;
		data.save();
	}

	public static void lockAll(PlayerDataHandler.PlayerData data) {
		List<String> unlocked = Lists.newArrayList(data.spellGroupsUnlocked);
		for (String group : unlocked)
			lockPieceGroup(data, group);
		data.level = 0;
		data.levelPoints = 0;
		data.save();
	}

	public static boolean hasGroup(PlayerDataHandler.PlayerData data, String group) {
		if (data == null)
			return false;

		if (group.equals(level0))
			return data.level > 0;
		return data.isPieceGroupUnlocked(group);
	}

	public static boolean hasGroup(PlayerEntity player, String group) {
		return hasGroup(PlayerDataHandler.get(player), group);
	}

	public static ITextComponent getGroupComponent(String group) {
		if (group.equals(level0)) {
			ITextComponent nameComponent = new StringTextComponent("[")
					.appendSibling(new TranslationTextComponent("psimisc.fakeLevel.psidust"))
					.appendText("]");
            nameComponent.getStyle().setColor(TextFormatting.AQUA);
            nameComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("psimisc.levelDisplay", 0)));
            return nameComponent;
        }

        PieceGroup pieceGroup = PsiAPI.groupsForName.get(group);
        if (pieceGroup == null) {
            ITextComponent errorComponent = new StringTextComponent("ERROR");
            errorComponent.getStyle().setColor(TextFormatting.RED);
            return errorComponent;
        }
        ITextComponent nameComponent = new StringTextComponent("[")
                .appendSibling(new TranslationTextComponent(pieceGroup.getUnlocalizedName()))
                .appendText("]");

        nameComponent.getStyle().setColor(TextFormatting.AQUA);
        nameComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("psimisc.levelDisplay", pieceGroup.levelRequirement)));
        return nameComponent;
    }

    public static String localizationKey() {
        return "command." + LibMisc.MOD_ID + ".learn";
    }

    protected static void notify(CommandSource sender, String result, Object... format) {
        sender.sendFeedback(new TranslationTextComponent(localizationKey() + "." + result, format), false);
    }

    protected static void error(String result, Object... format) throws CommandException {
        throw new CommandException(new TranslationTextComponent(localizationKey() + "." + result, format));
    }

    public static void applyPlayerData(PlayerEntity player, PlayerDataHandler.PlayerData data, String group, CommandSource sender) {
        unlockPieceGroupFree(data, level0);

        if (group.equals(level0))
            notify(sender, "success", player.getDisplayName(), getGroupComponent(group));
        else if (getGroups().contains(group)) {
            PieceGroup pieceGroup = PsiAPI.groupsForName.get(group);
            if (pieceGroup != null && !data.isPieceGroupUnlocked(group)) {
                for (String subGroup : pieceGroup.requirements) {
                    if (!data.isPieceGroupUnlocked(subGroup))
                        applyPlayerData(player, data, subGroup, sender);
                }

                unlockPieceGroupFree(data, group);

                notify(sender, "success", player.getDisplayName(), getGroupComponent(group));
            }
        }
    }

    public static void applyAll(PlayerDataHandler.PlayerData data, PlayerEntity player, CommandSource sender) {
        unlockAll(data);
        notify(sender, "success.all", player.getDisplayName());
    }

    public static boolean shouldNotApply(PlayerEntity player, String group) {
        return hasGroup(player, group);
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


}
