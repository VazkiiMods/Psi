/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.lib;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class LibResources {

    public static final String PREFIX_MOD = "psi:";
    public static final ResourceLocation PATCHOULI_BOOK = ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "encyclopaedia_psionica");

    public static final ResourceKey<DamageType> PSI_OVERLOAD = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "psi_overload"));

    public static final String PREFIX_GUI = PREFIX_MOD + "textures/gui/";
    public static final String GUI_CREATIVE = "psi.png";
    public static final String GUI_CAD_ASSEMBLER = PREFIX_GUI + "cad_assembler.png";
    public static final String GUI_PSI_BAR = PREFIX_GUI + "psi_bar.png";
    public static final String GUI_PSI_BAR_MASK = PREFIX_GUI + "psi_bar_mask.png";
    public static final String GUI_PSI_BAR_SHATTER = PREFIX_GUI + "psi_bar_shatter.png";
    public static final String GUI_SIGN = PREFIX_GUI + "signs/sign%d.png";
    public static final String GUI_PROGRAMMER = PREFIX_GUI + "programmer.png";
    public static final String PREFIX_MODEL = "textures/model/";
    public static final ResourceLocation MODEL_PSIMETAL_EXOSUIT = ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, PREFIX_MODEL + "psimetal_exosuit.png");
    public static final ResourceLocation MODEL_PSIMETAL_EXOSUIT_SENSOR = ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, PREFIX_MODEL +"psimetal_exosuit_sensor.png");
    public static final String PREFIX_MISC = PREFIX_MOD + "textures/misc/";
    public static final String MISC_SPELL_CIRCLE = PREFIX_MISC + "spell_circle%d.png";
    public static final String SHADER_PSI_BAR = "psi_bar";
    public static final String SPELL_CONNECTOR_LINES = PREFIX_MOD + "spell/connector_lines";

}
