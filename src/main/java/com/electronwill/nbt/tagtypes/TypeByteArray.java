package com.electronwill.nbt.tagtypes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TypeByteArray implements TagType<byte[]> {
	TypeByteArray() {}

	@Override
	public int id() {
		return 7;
	}

	@Override
	public byte[] readValue(DataInput input) throws IOException {
		int length = input.readInt();
		byte[] bytes = new byte[length];
		input.readFully(bytes);
		return bytes;
	}

	@Override
	public void writeValue(byte[] value, DataOutput output) throws IOException {
		output.writeInt(value.length);
		output.write(value);
	}
}