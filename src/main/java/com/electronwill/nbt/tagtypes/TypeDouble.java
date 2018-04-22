package com.electronwill.nbt.tagtypes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TypeDouble implements TagType<Double> {
	TypeDouble() {}

	@Override
	public int id() {
		return 6;
	}

	@Override
	public Double readValue(DataInput input) throws IOException {
		return input.readDouble();
	}

	@Override
	public void writeValue(Double value, DataOutput output) throws IOException {
		output.writeDouble(value);
	}
}