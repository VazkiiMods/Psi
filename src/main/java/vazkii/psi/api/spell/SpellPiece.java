/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [16/01/2016, 15:19:22 (GMT)]
 */
package vazkii.psi.api.spell;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.TooltipHelper;

public class SpellPiece {

	private static final String TAG_KEY = "spellKey"; 

	public final String registryKey;
	public final Spell spell;

	public SpellPiece(Spell spell) {
		this.spell = spell;
		registryKey = PsiAPI.spellPieceRegistry.getNameForObject(getClass());
	}

	@SideOnly(Side.CLIENT)
	public void draw() {
		ResourceLocation res = PsiAPI.simpleSpellTextures.get(registryKey);
		Minecraft.getMinecraft().renderEngine.bindTexture(res);
		WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();

		wr.begin(7, DefaultVertexFormats.POSITION_TEX);
		wr.pos(0, 16, 0).tex(0, 1).endVertex();
		wr.pos(16, 16, 0).tex(1, 1).endVertex();
		wr.pos(16, 0, 0).tex(1, 0).endVertex();;
		wr.pos(0, 0, 0).tex(0, 0).endVertex();
		Tessellator.getInstance().draw();
	}
	
	@SideOnly(Side.CLIENT)
	public void getTooltip(List<String> tooltip) {
		TooltipHelper.addToTooltip(tooltip, getUnlocalizedName());
	}
	
	public String getUnlocalizedName() {
		return "psi.spellpiece." + registryKey;
	}
	
	public void getShownPieces(List<SpellPiece> pieces) {
		pieces.add(this);
	}
	
	public static SpellPiece createFromNBT(Spell spell, NBTTagCompound cmp) {
		String key = cmp.getString(TAG_KEY);
		Class<? extends SpellPiece> clazz = PsiAPI.spellPieceRegistry.getObject(key);
		if(clazz != null) {
			SpellPiece p = create(clazz, spell);
			p.readFromNBT(cmp);
			return p;
		}

		return null;
	}

	public static SpellPiece create(Class<? extends SpellPiece> clazz, Spell spell) {
		try {
			return clazz.getConstructor(Spell.class).newInstance(spell);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public SpellPiece copy() {
		NBTTagCompound cmp = new NBTTagCompound();
		writeToNBT(cmp);
		return createFromNBT(spell, cmp);
	}

	public void readFromNBT(NBTTagCompound cmp) {
		// do later
	}

	public void writeToNBT(NBTTagCompound cmp) {
		cmp.setString(TAG_KEY, registryKey);
	}

}
