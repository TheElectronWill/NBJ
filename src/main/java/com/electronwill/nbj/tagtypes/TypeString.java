package com.electronwill.nbj.tagtypes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TypeString implements TagType<String> {
	TypeString() {}

	@Override
	public int id() {
		return 8;
	}

	@Override
	public String readValue(DataInput input) throws IOException {
		return input.readUTF();
	}

	@Override
	public void writeValue(String value, DataOutput output) throws IOException {
		output.writeUTF(value);
	}
}