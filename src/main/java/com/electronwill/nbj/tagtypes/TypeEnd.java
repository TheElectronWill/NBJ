package com.electronwill.nbj.tagtypes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class TypeEnd implements TagType<Void> {
	TypeEnd() {}

	@Override
	public int id() {
		return 0;
	}

	@Override
	public Void readValue(DataInput input) throws IOException {
		return null;
	}

	@Override
	public void writeValue(Void value, DataOutput output) throws IOException {}
}