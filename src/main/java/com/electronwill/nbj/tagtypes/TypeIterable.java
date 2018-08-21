package com.electronwill.nbj.tagtypes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class TypeIterable implements TagType<Iterable<?>> {
	TypeIterable() {}

	@Override
	public int id() {
		return 9;
	}

	@Override
	public Iterable<?> readValue(DataInput input) throws IOException {
		return Types.LIST.readValue(input);
	}

	@Override
	public void writeValue(Iterable<?> value, DataOutput output) throws IOException {
		List<Object> list = new ArrayList<>();
		for (Object element : value) {
			list.add(element);
		}
		Types.LIST.writeValue(list, output);
	}
}