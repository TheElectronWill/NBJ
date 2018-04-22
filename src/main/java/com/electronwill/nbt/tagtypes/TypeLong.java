package com.electronwill.nbt.tagtypes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TypeLong implements TagType<Long> {
	TypeLong() {}

	@Override
	public int id() {
		return 4;
	}

	@Override
	public Long readValue(DataInput input) throws IOException {
		return input.readLong();
	}

	@Override
	public void writeValue(Long value, DataOutput output) throws IOException {
		output.writeLong(value);
	}
}