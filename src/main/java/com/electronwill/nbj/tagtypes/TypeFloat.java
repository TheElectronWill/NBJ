package com.electronwill.nbj.tagtypes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class TypeFloat implements TagType<Float> {
	TypeFloat() {}

	@Override
	public int id() {
		return 5;
	}

	@Override
	public Float readValue(DataInput input) throws IOException {
		return input.readFloat();
	}

	@Override
	public void writeValue(Float value, DataOutput output) throws IOException {
		output.writeFloat(value);
	}
}