package com.electronwill.nbj.tagtypes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TypeIterator implements TagType<Iterator<?>> {
	TypeIterator() {}

	@Override
	public int id() {
		return 9;
	}

	@Override
	public Iterator<?> readValue(DataInput input) throws IOException {
		return Types.LIST.readValue(input).iterator();
	}

	@Override
	public void writeValue(Iterator<?> value, DataOutput output) throws IOException {
		List<Object> list = new ArrayList<>();
		while (value.hasNext()) {
			list.add(value.next());
		}
		Types.LIST.writeValue(list, output);
	}
}