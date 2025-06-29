/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.exosuit;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class PsiArmorEvent extends PlayerEvent {

    // DO NOT FIRE AN EVENT WITH THIS
    public static final String NONE = "psi.event.none";

    public static final String DAMAGE = "psi.event.damage";
    public static final String TICK = "psi.event.tick";
    public static final String JUMP = "psi.event.jump";

    public static final String LOW_LIGHT = "psi.event.low_light";
    public static final String UNDERWATER = "psi.event.underwater";
    public static final String ON_FIRE = "psi.event.on_fire";
    public static final String LOW_HP = "psi.event.low_hp";
    public static final String DETONATE = "psi.event.spell_detonate";

    private static boolean posting = false;

    public final String type;
    public final double damage;
    public final LivingEntity attacker;

    public PsiArmorEvent(Player player, String type) {
        this(player, type, 0, null);
    }

    public PsiArmorEvent(Player player, String type, double damage, LivingEntity attacker) {
        super(player);
        this.type = type;
        this.damage = damage;
        this.attacker = attacker;

        if (type.equals(NONE)) {
            throw new IllegalArgumentException("Can't you read?");
        }
    }

    public static void post(PsiArmorEvent event) {
        if (!posting) {
            posting = true;
            NeoForge.EVENT_BUS.post(event);
            posting = false;
        }
    }

}
