package com.electronwill.nbj.tagtypes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface TagType<V> {
	int id();

	V readValue(DataInput input) throws IOException;

	void writeValue(V value, DataOutput output) throws IOException;
}