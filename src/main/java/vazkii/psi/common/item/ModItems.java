/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * {todo-put-license-here}
 * 
 * File Created @ [08/01/2016, 21:48:12 (GMT)]
 */
package vazkii.psi.common.item;

import vazkii.psi.common.item.component.ItemCADAssembly;

public final class ModItems {

	public static ItemMod material;

	public static ItemMod cadAssembly;
	
	public static void init() {
		material = new ItemMaterial();
		
		cadAssembly = new ItemCADAssembly();
	}
	
}
