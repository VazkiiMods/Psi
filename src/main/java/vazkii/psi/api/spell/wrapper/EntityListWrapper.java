/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell.wrapper;

import net.minecraft.entity.Entity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Wrapper class for an Entity list.
 * The Wrapper gets "ownership" of the list and will probably modify it.
 * If you don't want to give up ownership use {@link #makeCleanWrapper}.
 */
public class EntityListWrapper implements Iterable<Entity> {

	private final List<Entity> list;

	/**
	 * Constructs an EntityListWrapper from a list.
	 * <b>The list must be sorted by {@link #compareEntities} and have no {@code null} values</b>.
	 * If you can't provide these guarantees, use {@link #makeCleanWrapper}.
	 */
	public EntityListWrapper(@Nonnull List<Entity> list) {
		this.list = Objects.requireNonNull(list);
	}

	public static EntityListWrapper makeCleanWrapper(@Nonnull List<Entity> list) {
		List<Entity> copy = new ArrayList<>();
		for (Entity e : list) {
			if (e != null) {
				copy.add(e);
			}
		}
		copy.sort(EntityListWrapper::compareEntities);
		return new EntityListWrapper(copy);
	}

	/**
	 * A Comparator for Entities that's deterministic, to keep order with the Entity Lists.
	 */
	public static int compareEntities(Entity l, Entity r) {
		return l.getUniqueID().compareTo(r.getUniqueID());
	}

	public List<Entity> unwrap() {
		return list;
	}

	@Override
	public String toString() {
		return list.toString();
	}

	@Nonnull
	@Override
	public Iterator<Entity> iterator() {
		return list.iterator();
	}

}
