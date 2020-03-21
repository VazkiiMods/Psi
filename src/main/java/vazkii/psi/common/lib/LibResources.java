/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [08/01/2016, 21:52:28 (GMT)]
 */
package vazkii.psi.common.lib;

import net.minecraft.util.ResourceLocation;

public class LibResources {

	public static final String PREFIX_MOD = "psi:";
	public static final ResourceLocation PATCHOULI_BOOK = new ResourceLocation(LibMisc.MOD_ID, "taurus_silver_handbook");

	public static final String PREFIX_SHADER = "/assets/psi/shaders/";
	public static final String PREFIX_GUI = PREFIX_MOD + "textures/gui/";
	public static final String PREFIX_SPELL = PREFIX_MOD + "textures/spell/";
	public static final String PREFIX_MODEL = PREFIX_MOD + "textures/model/";
	public static final String PREFIX_MISC = PREFIX_MOD + "textures/misc/";

	public static final String SHADER_RAW_COLOR = PREFIX_SHADER + "raw_color";
	public static final String SHADER_PSI_BAR = PREFIX_SHADER + "psi_bar";
	public static final String SHADER_SIMPLE_BLOOM = PREFIX_SHADER + "simple_bloom";

	public static final String GUI_CREATIVE = "psi.png";
	public static final String GUI_CAD_ASSEMBLER = PREFIX_GUI + "cad_assembler.png";
	public static final String GUI_PSI_BAR = PREFIX_GUI + "psi_bar.png";
	public static final String GUI_PSI_BAR_MASK = PREFIX_GUI + "psi_bar_mask.png";
	public static final String GUI_PSI_BAR_SHATTER = PREFIX_GUI + "psi_bar_shatter.png";
	public static final String GUI_SIGN = PREFIX_GUI + "signs/sign%d.png";
	public static final String GUI_PROGRAMMER = PREFIX_GUI + "programmer.png";
	public static final String GUI_LEVELING = PREFIX_GUI + "leveling.png";
	public static final String GUI_INTRODUCTION = PREFIX_GUI + "introduction.png";

	public static final String SPELL_CONNECTOR_LINES = PREFIX_SPELL + "connector_lines.png";

	public static final String MODEL_PSIMETAL_EXOSUIT = PREFIX_MODEL + "psimetal_exosuit.png";
	public static final String MODEL_PSIMETAL_EXOSUIT_SENSOR = PREFIX_MODEL + "psimetal_exosuit_sensor.png";

	public static final String MISC_SPELL_CIRCLE = PREFIX_MISC + "spell_circle%d.png";

}
