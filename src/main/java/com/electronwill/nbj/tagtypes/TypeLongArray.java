package com.electronwill.nbj.tagtypes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TypeLongArray implements TagType<long[]> {
	TypeLongArray() {}

	@Override
	public int id() {
		return 12;
	}

	@Override
	public long[] readValue(DataInput input) throws IOException {
		int length = input.readInt();
		long[] longs = new long[length];
		for (int i = 0; i < length; i++) {
			longs[i] = Types.LONG.readValue(input);
		}
		return longs;
	}

	@Override
	public void writeValue(long[] value, DataOutput output) throws IOException {
		output.writeLong(value.length);
		for (int i = 0; i < value.length; i++) {
			Types.LONG.writeValue(value[i], output);
		}
	}
}