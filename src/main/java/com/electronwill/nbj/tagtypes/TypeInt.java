package com.electronwill.nbj.tagtypes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class TypeInt implements TagType<Integer> {
	TypeInt() {}

	@Override
	public int id() {
		return 3;
	}

	@Override
	public Integer readValue(DataInput input) throws IOException {
		return input.readInt();
	}

	@Override
	public void writeValue(Integer value, DataOutput output) throws IOException {
		output.writeInt(value);
	}
}