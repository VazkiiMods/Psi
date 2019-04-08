/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [09/03/2016, 14:45:53 (GMT)]
 */
package vazkii.psi.client.core.helper;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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
			
			if(Desktop.isDesktopSupported())
				Desktop.getDesktop().browse(new URI(redditUrl));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void uploadAndOpen(String title, String export) {
		String url = uploadImage(title, export);
		try {
			if(Desktop.isDesktopSupported())
				Desktop.getDesktop().browse(new URI(url));
		} catch(Exception e) {
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
			if(resJson.has("success") && resJson.get("success").getAsBoolean()) {
				JsonObject data = resJson.get("data").getAsJsonObject();
				String id = data.get("id").getAsString();
				
				return "https://imgur.com/" + id;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return "N/A";
	}

	public static String takeScreenshot() throws Exception {
		Minecraft mc = Minecraft.getMinecraft();

		ScaledResolution res = new ScaledResolution(mc);
		int screenWidth = mc.displayWidth;
		int screenHeight = mc.displayHeight;

		int scale = res.getScaleFactor();
		int width = 380 * scale;
		int height = 200 * scale;

		int left = screenWidth / 2 - width / 2;
		int top = screenHeight / 2 - height / 2;

		int i = width * height;

		if(pixelBuffer == null || pixelBuffer.capacity() < i) {
			pixelBuffer = BufferUtils.createIntBuffer(i);
			pixelValues = new int[i];
		}

		GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		pixelBuffer.clear();

		GL11.glReadPixels(left, top, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);

		pixelBuffer.get(pixelValues);
		TextureUtil.processPixelValues(pixelValues, width, height);
		BufferedImage bufferedimage;

		bufferedimage = new BufferedImage(width, height, 1);
		bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ImageIO.write(bufferedimage, "png", stream);
		byte[] bArray = stream.toByteArray();
		return Base64.getEncoder().encodeToString(bArray);
	}

}
