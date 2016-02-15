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
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class CommandDownloadLatest extends CommandBase {

	private static final boolean ENABLED = true;

	@Override
	public String getCommandName() {
		return "psi-download-latest";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/psi-download-latest <version>";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		if(!ENABLED)
			var1.addChatMessage(new ChatComponentTranslation("psi.versioning.disabled").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));

		else if(var2.length == 1)
			if(VersionChecker.downloadedFile)
				var1.addChatMessage(new ChatComponentTranslation("psi.versioning.downloadedAlready").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
			else if(VersionChecker.startedDownload)
				var1.addChatMessage(new ChatComponentTranslation("psi.versioning.downloadingAlready").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
			else new ThreadDownloadMod("Psi-" + var2[0] + ".jar");
	}

}