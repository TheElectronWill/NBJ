package com.electronwill.nbj;

import com.electronwill.nbj.tagtypes.TagType;
import com.electronwill.nbj.tagtypes.Types;

import java.io.DataOutput;
import java.io.IOException;

/**
 * Represents an NBT tag.
 *
 * @param <V> the tag's value type
 */
public class Tag<V> {
	protected final TagType<V> type;
	protected String name;
	protected V value;

	public Tag(TagType<V> type, String name, V value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}

	public void writeNameless(DataOutput output) throws IOException {
		output.writeByte(type.id());
		type.writeValue(value, output);
	}

	public void writeNamed(DataOutput output) throws IOException {
		output.writeByte(type.id());
		Types.STRING.writeValue(name, output);
		type.writeValue(value, output);
	}

	public TagType<V> getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public V getValue() {
		return value;
	}
}