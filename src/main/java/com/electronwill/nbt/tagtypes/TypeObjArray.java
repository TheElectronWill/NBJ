package com.electronwill.nbt.tagtypes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reads and writes object arrays as NBT lists.
 */
public class TypeObjArray implements TagType<Object[]> {
	TypeObjArray() {}

	@Override
	public int id() {
		return 9;
	}

	@Override
	public Object[] readValue(DataInput input) throws IOException {
		byte typeId = input.readByte();
		TagType<?> type = Types.get(typeId);
		int length = input.readInt();
		Object[] array = new Object[length];
		for (int i = 0; i < length; i++) {
			Object element = type.readValue(input);
			array[i] = element;
		}
		return array;
	}

	@Override
	public void writeValue(Object[] value, DataOutput output) throws IOException {
		Object firstElement = value.length == 0 ? null : value[0];
		TagType<Object> type = Types.get(firstElement);
		output.writeByte(type.id());
		output.writeInt(value.length);
		for (Object element : value) {
			type.writeValue(element, output);
		}
	}
}