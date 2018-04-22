package com.electronwill.nbt.tagtypes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TypeByte implements TagType<Byte> {
	TypeByte() {}

	@Override
	public int id() {
		return 1;
	}

	@Override
	public Byte readValue(DataInput input) throws IOException {
		return input.readByte();
	}

	@Override
	public void writeValue(Byte value, DataOutput output) throws IOException {
		output.writeByte(value);
	}
}