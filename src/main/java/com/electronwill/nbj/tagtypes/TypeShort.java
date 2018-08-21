package com.electronwill.nbj.tagtypes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class TypeShort implements TagType<Short> {
	TypeShort() {}

	@Override
	public int id() {
		return 2;
	}

	@Override
	public Short readValue(DataInput input) throws IOException {
		return input.readShort();
	}

	@Override
	public void writeValue(Short value, DataOutput output) throws IOException {
		output.writeByte(value);
	}
}