package com.electronwill.nbj;

import com.electronwill.nbj.tagtypes.TagType;
import com.electronwill.nbj.tagtypes.Types;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
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
				TagType<Object> type = Types.forValue(next.getValue());
				return new Tag<>(type, next.getKey(), next.getValue());
			}
		};
	}

	public static TagCompound readNamed(DataInput input) throws IOException {
		int typeId = input.readByte();
		if (typeId != Types.COMPOUND.id()) {
			throw new NbtException("Invalid first id in TagCompound, expected Compound's id, not " + typeId);
		}
		String name = Types.STRING.readValue(input);
		Map<String, Object> values = Types.COMPOUND.readValue(input);
		return new TagCompound(name, values);
	}

	public static TagCompound readNamed(InputStream input) throws IOException {
		DataInput dataInput = (input instanceof DataInput) ? (DataInput)input : new DataInputStream(input);
		return readNamed(dataInput);
	}
}