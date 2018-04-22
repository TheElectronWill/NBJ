package com.electronwill.nbt.tagtypes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TypeCompound implements TagType<Map<String, Object>> {
	TypeCompound() {}

	@Override
	public int id() {
		return 10;
	}

	@Override
	public Map<String, Object> readValue(DataInput input) throws IOException {
		Map<String, Object> map = new HashMap<>();
		byte elementTypeId;
		while ((elementTypeId = input.readByte()) > 0) {
			String elementName = Types.STRING.readValue(input);
			TagType<?> elementType = Types.get(elementTypeId);
			Object element = elementType.readValue(input);
			map.put(elementName, element);
		}
		return map;
	}

	@Override
	public void writeValue(Map<String, Object> value, DataOutput output) throws IOException {
		for (Map.Entry<String, Object> entry : value.entrySet()) {
			String elementName = entry.getKey();
			Object element = entry.getValue();
			TagType<Object> elementType = Types.get(element);
			Types.STRING.writeValue(elementName, output);
			elementType.writeValue(element, output);
		}
		output.writeByte(0); // TAG_END's type
	}
}