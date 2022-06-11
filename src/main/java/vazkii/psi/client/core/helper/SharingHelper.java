/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.core.helper;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.NativeImage;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public final class SharingHelper {

	private static final String CLIENT_ID = "d5d2258f3526156";

	private static IntBuffer pixelBuffer;
	private static int[] pixelValues;

	public static void uploadAndShare(String title, String export) {
		String url = uploadImage(title, export);

		try {
			String contents = "## " + title + "  \n" +
					"### [Image + Code](" + url + ")\n" +
					"(to get the code click the link, RES won't show it)\n" +
					"\n" +
					"---" +
					"\n" +
					"*REPLACE THIS WITH A DESCRIPTION OF YOUR SPELL  \n" +
					"Make sure you read the rules before posting. Look on the sidebar: https://www.reddit.com/r/psispellcompendium/  \n" +
					"Delete this part before you submit.*";

			String encodedContents = URLEncoder.encode(contents, "UTF-8");
			String encodedTitle = URLEncoder.encode(title, "UTF-8");

			String redditUrl = "https://old.reddit.com/r/psispellcompendium/submit?title=" + encodedTitle + "&text=" + encodedContents;
			Util.getPlatform().openUri(new URI(redditUrl));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void uploadAndOpen(String title, String export) {
		String url = uploadImage(title, export);
		try {
			Util.getPlatform().openUri(new URI(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String uploadImage(String title, String export) {
		try {
			String desc = "Spell Code:\n\n" + export;
			HttpClient client = HttpClients.createDefault();

			String url = "https://api.imgur.com/3/image";
			HttpPost post = new HttpPost(url);

			List<NameValuePair> list = new ArrayList<>();
			list.add(new BasicNameValuePair("type", "base64"));
			list.add(new BasicNameValuePair("image", takeScreenshot()));
			list.add(new BasicNameValuePair("name", title));
			list.add(new BasicNameValuePair("description", desc));

			post.setEntity(new UrlEncodedFormEntity(list));
			post.addHeader("Authorization", "Client-ID " + CLIENT_ID);

			HttpResponse res = client.execute(post);
			JsonObject resJson = new JsonParser().parse(EntityUtils.toString(res.getEntity())).getAsJsonObject();
			if (resJson.has("success") && resJson.get("success").getAsBoolean()) {
				JsonObject data = resJson.get("data").getAsJsonObject();
				String id = data.get("id").getAsString();

				return "https://imgur.com/" + id;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "N/A";
	}

	public static String takeScreenshot() throws Exception {
		Minecraft mc = Minecraft.getInstance();

		NativeImage image = Screenshot.takeScreenshot(mc.getMainRenderTarget());
		byte[] bArray = image.asByteArray();
		return Base64.getEncoder().encodeToString(bArray);
	}

}
