package com.electronwill.nbj;

import com.electronwill.nbj.tagtypes.TagType;
import com.electronwill.nbj.tagtypes.Types;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * NBT compound tag.
 *
 * @author TheElectronWill
 */
public final class TagCompound extends Tag<Map<String, Object>> implements Iterable<Tag<?>> {

	public TagCompound(String name, Map<String, Object> value) {
		super(Types.COMPOUND, name, value);
	}

	public TagCompound(String name) {
		super(Types.COMPOUND, name, new HashMap<String, Object>());
	}

	public Object get(String name) {
		return value.get(name);
	}

	@Override
	public Iterator<Tag<?>> iterator() {
		return new Iterator<Tag<?>>() {
			private final Iterator<Map.Entry<String, Object>> entryIterator = value.entrySet().iterator();

			@Override
			public boolean hasNext() {
				return entryIterator.hasNext();
			}

			@Override
			public void remove() {
				entryIterator.remove();
			}

			@Override
			public Tag<?> next() {
				Map.Entry<String, Object> next = entryIterator.next();
				TagType<Object> type = Types.get(next.getValue());
				return new Tag<>(type, next.getKey(), next.getValue());
			}
		};
	}
}