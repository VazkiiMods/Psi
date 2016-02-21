/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [21/02/2016, 14:05:54 (GMT)]
 */
package vazkii.psi.api.exosuit;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PsiArmorEvent extends PlayerEvent {

	// DO NOT FIRE AN EVENT WITH THIS
	public static final String NONE = "psi.event.none";

	public static final String DAMAGE = "psi.event.damage";
	public static final String TICK = "psi.event.tick";
	public static final String JUMP = "psi.event.jump";

	public static final String LOW_LIGHT = "psi.event.lowLight";
	public static final String UNDERWATER = "psi.event.underwater";
	public static final String ON_FIRE = "psi.event.onFire";
	public static final String LOW_HP = "psi.event.lowHp";

	private static boolean posting = false;

	public final String type;
	public final double damage;
	public final EntityLivingBase attacker;

	public PsiArmorEvent(EntityPlayer player, String type) {
		this(player, type, 0, null);
	}

	public PsiArmorEvent(EntityPlayer player, String type, double damage, EntityLivingBase attacker) {
		super(player);
		this.type = type;
		this.damage = damage;
		this.attacker = attacker;

		if(type.equals(NONE))
			throw new IllegalArgumentException("Can't you read?");
	}

	public static void post(PsiArmorEvent event) {
		if(!posting) {
			posting = true;
			MinecraftForge.EVENT_BUS.post(event);
			posting = false;
		}
	}

}
