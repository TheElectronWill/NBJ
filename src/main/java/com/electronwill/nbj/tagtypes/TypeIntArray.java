package com.electronwill.nbj.tagtypes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TypeIntArray implements TagType<int[]> {
	TypeIntArray() {}

	@Override
	public int id() {
		return 11;
	}

	@Override
	public int[] readValue(DataInput input) throws IOException {
		int length = input.readInt();
		int[] ints = new int[length];
		for (int i = 0; i < length; i++) {
			ints[i] = Types.INT.readValue(input);
		}
		return ints;
	}

	@Override
	public void writeValue(int[] value, DataOutput output) throws IOException {
		output.writeInt(value.length);
		for (int i = 0; i < value.length; i++) {
			Types.INT.writeValue(value[i], output);
		}
	}
}