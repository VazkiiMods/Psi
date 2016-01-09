/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * {todo-put-license-here}
 * 
 * File Created @ [08/01/2016, 21:53:27 (GMT)]
 */
package vazkii.psi.common.item.component;

import vazkii.psi.common.lib.LibItemNames;

public class ItemCADAssembly extends ItemCADComponent {

	public static final String[] VARIANTS = {
		"cadAssemblyIron",
		"cadAssemblyGold"
	};
	
	public ItemCADAssembly() {
		super(LibItemNames.CAD_ASSEMBLY, VARIANTS);
	}
	
}
