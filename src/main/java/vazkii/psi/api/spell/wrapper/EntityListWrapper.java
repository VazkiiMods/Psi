/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell.wrapper;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Wrapper class for an Entity list.
 * Entities are guaranteed to be sorted deterministically; the list is guaranteed to have no null entities.
 */
public class EntityListWrapper implements Iterable<Entity> {

	public static final EntityListWrapper EMPTY = new EntityListWrapper(Collections.emptyList());
	private final List<Entity> list;

	private EntityListWrapper(@NotNull List<Entity> list) {
		this.list = Objects.requireNonNull(list);
	}

	/**
	 * Constructs an EntityListWrapper from an arbitrary list of entities.
	 */
	public static EntityListWrapper make(@NotNull List<Entity> list) {
		List<Entity> copy = new ArrayList<>();
		for(Entity e : list) {
			if(e != null) {
				copy.add(e);
			}
		}
		copy.sort(EntityListWrapper::compareEntities);
		return new EntityListWrapper(copy);
	}

	public static EntityListWrapper union(@NotNull EntityListWrapper left, @NotNull EntityListWrapper right) {
		List<Entity> l1 = left.list, l2 = right.list;
		List<Entity> entities = new ArrayList<>(l1.size() + l2.size());
		int i = 0, j = 0;
		while(i < l1.size() && j < l2.size()) {
			int cmp = compareEntities(l1.get(i), l2.get(j));
			if(cmp == 0) {
				i++;
				continue;
			}
			entities.add(cmp < 0 ? l1.get(i++) : l2.get(j++));
		}
		entities.addAll(l1.subList(i, l1.size()));
		entities.addAll(l2.subList(j, l2.size()));
		return new EntityListWrapper(entities);
	}

	public static EntityListWrapper exclusion(@NotNull EntityListWrapper list, @NotNull EntityListWrapper remove) {
		List<Entity> result = new ArrayList<>();
		List<Entity> search = remove.list;
		for(Entity e : list) {
			if(Collections.binarySearch(search, e, EntityListWrapper::compareEntities) < 0) {
				result.add(e);
			}
		}

		return new EntityListWrapper(result);
	}

	public static EntityListWrapper intersection(@NotNull EntityListWrapper left, @NotNull EntityListWrapper right) {
		List<Entity> result = new ArrayList<>();
		List<Entity> search = right.list;
		for(Entity e : left) {
			if(Collections.binarySearch(search, e, EntityListWrapper::compareEntities) >= 0) {
				result.add(e);
			}
		}

		return new EntityListWrapper(result);
	}

	//TODO this can probably be implemented lazily with a wrapper-list but w/e
	public static EntityListWrapper withAdded(@NotNull EntityListWrapper base, @NotNull Entity toAdd) {
		List<Entity> list = new ArrayList<>(base.list);
		int index = Collections.binarySearch(list, toAdd, EntityListWrapper::compareEntities);
		if(index < 0) {
			list.add(~index, toAdd);
		}
		return new EntityListWrapper(list);
	}

	public static EntityListWrapper withRemoved(@NotNull EntityListWrapper base, @NotNull Entity toRemove) {
		List<Entity> list = new ArrayList<>(base.list);
		list.remove(toRemove);
		return new EntityListWrapper(list);
	}

	/**
	 * A Comparator for Entities that's deterministic, to keep order with the Entity Lists.
	 */
	public static int compareEntities(Entity l, Entity r) {
		return Integer.compare(l.getId(), r.getId());
	}

	/**
	 * Returns the underlying list for this ELW. Unsafe, as clients are able to modify the list so that it doesn't
	 * maintain its guarantees.
	 */
	@Deprecated
	private List<Entity> unwrap() {
		return list;
	}

	public int size() {
		return list.size();
	}

	public Entity get(int index) {
		return list.get(index);
	}

	@Override
	public String toString() {
		return list.toString();
	}

	@NotNull
	@Override
	public Iterator<Entity> iterator() {
		return list.iterator();
	}

}
