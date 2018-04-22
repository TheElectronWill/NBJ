package com.electronwill.nbj.tagtypes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TypeList implements TagType<List<?>> {
	TypeList() {}

	@Override
	public int id() {
		return 9;
	}

	@Override
	public List<?> readValue(DataInput input) throws IOException {
		byte typeId = input.readByte();
		TagType<?> type = Types.get(typeId);
		int length = input.readInt();
		if (length == 0) {
			return Collections.emptyList();
		}
		List<Object> list = new ArrayList<>(length);
		for (int i = 0; i < length; i++) {
			Object element = type.readValue(input);
			list.add(element);
		}
		return list;
	}

	@Override
	public void writeValue(List<?> value, DataOutput output) throws IOException {
		Object firstElement = value.size() == 0 ? null : value.get(0);
		TagType<Object> type = Types.forValue(firstElement);
		output.writeByte(type.id());
		output.writeInt(value.size());
		for (Object element : value) {
			type.writeValue(element, output);
		}
	}
}