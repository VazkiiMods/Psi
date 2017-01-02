/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 31, 2014, 11:43:13 PM (GMT)]
 */
package vazkii.psi.client.core.version;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandDownloadLatest extends CommandBase {

	private static final boolean ENABLED = true;

	@Override
	public String getName() {
		return "psi-download-latest";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/psi-download-latest <version>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(!ENABLED)
			sender.sendMessage(new TextComponentTranslation("psi.versioning.disabled").setStyle(new Style().setColor(TextFormatting.RED)));

		else if(args.length == 1)
			if(VersionChecker.downloadedFile)
				sender.sendMessage(new TextComponentTranslation("psi.versioning.downloadedAlready").setStyle(new Style().setColor(TextFormatting.RED)));
			else if(VersionChecker.startedDownload)
				sender.sendMessage(new TextComponentTranslation("psi.versioning.downloadingAlready").setStyle(new Style().setColor(TextFormatting.RED)));
			else new ThreadDownloadMod("Psi-" + args[0] + ".jar");
	}

}