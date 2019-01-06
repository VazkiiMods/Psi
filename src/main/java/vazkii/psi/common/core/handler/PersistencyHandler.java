/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [05/02/2016, 18:22:14 (GMT)]
 */
package vazkii.psi.common.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;

import java.io.*;

@SideOnly(Side.CLIENT)
public final class PersistencyHandler {

	private static boolean doneInit = false;
	private static File persistentFile;
	public static int persistentLevel;
	public static boolean ignore = false;

	public static void init() {
		if(doneInit)
			return;
		doneInit = true;

		if(!ConfigHandler.usePersistentData)
			return;

		String userhome = System.getProperty("user.home");
		String os = System.getProperty("os.name");
		if(os.startsWith("Windows"))
			userhome += "\\AppData\\Roaming\\.minecraft\\psi_persistent";
		else if(os.startsWith("Mac"))
			userhome += "/Library/Application Support/minecraft/psi_persistent";
		else userhome += "/.minecraft/psi_persistent";

		File dir = new File(userhome);
		if(!dir.exists())
			if (!dir.mkdirs())
				throw new RuntimeException("Couldn't create home directory!");

		File info = new File(userhome, "info.txt");
		if(!info.exists()) {
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(info))) {
				if (info.createNewFile()) {
					writer.write("This is Psi's Persistent Data directory.\n");
					writer.write("Files stored here are persistent info on what levels each player has gotten to.\n");
					writer.write("The files in here are the same for every instance and modpack you play, they always end up here.\n");
					writer.write("This allows you to skip tutorials on new worlds or even new modpacks.\n");
					writer.write("\n");
					writer.write("If you wish to disable this feature, you can turn it off in the Psi config file.");
					writer.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Minecraft mc = Minecraft.getMinecraft();
		String uuid = EntityPlayer.getUUID(mc.player.getGameProfile()).toString();
		persistentFile = new File(userhome, uuid);

		if(persistentFile.exists()) {
			try(BufferedReader reader = new BufferedReader(new FileReader(persistentFile))) {
				String l = reader.readLine();
				if(l != null) {
					int n = Integer.parseInt(l.trim());
					n = Math.min(n, PsiAPI.levelCap);
					persistentLevel = n;
				}
			} catch(NumberFormatException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void save(int level) {
		Minecraft mc = Minecraft.getMinecraft();
		if(!ConfigHandler.usePersistentData || level <= persistentLevel || mc.player == null || mc.player.capabilities.isCreativeMode)
			return;

		try {
			if (persistentFile != null && (persistentFile.exists() || persistentFile.createNewFile())) {
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(persistentFile))) {
					writer.write("" + level);
					writer.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
