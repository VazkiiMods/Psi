/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [22/01/2016, 22:56:10 (GMT)]
 */
package vazkii.psi.api.spell.wrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;

/**
 * Wrapper class for an Entity list.
 */
public class EntityListWrapper implements Iterable<Entity> {

	private final List<Entity> list;

	public EntityListWrapper(List<Entity> list) {
		List<Entity> copy = new ArrayList();
		for(Entity e : list)
			if(e != null)
				copy.add(e);
		this.list = copy;
	}

	public List<Entity> unwrap() {
		return list;
	}

	@Override
	public String toString() {
		return list.toString();
	}

	@Override
	public Iterator<Entity> iterator() {
		return list.iterator();
	}

}
