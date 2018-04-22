package com.electronwill.nbj;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reading and writing NBT data.
 *
 * @author TheElectronWill
 */
public final class Nbt {

	private Nbt() {}

	/**
	 * NBT Tag ID (takes place before the tag payload).
	 *
	 * @see http://wiki.vg/NBT
	 * @see http://minecraft.gamepedia.com/NBT_format
	 */
	public static final byte TAG_END = 0, TAG_BYTE = 1, TAG_SHORT = 2, TAG_INT = 3, TAG_LONG = 4, TAG_FLOAT = 5, TAG_DOUBLE = 6,
			TAG_BYTE_ARRAY = 7, TAG_STRING = 8, TAG_LIST = 9, TAG_COMPOUND = 10, TAG_INT_ARRAY = 11;

	/**
	 * Writes a TagCompound to an OutputStream.
	 */
	public static void write(Map<String, Object> compound, OutputStream out) throws IOException, NbtException {
		write(compound, "", out);
	}

	/**
	 * Writes a TagCompound to an OutputStream.
	 */
	public static void write(Map<String, Object> compound, String name, OutputStream out) throws IOException, NbtException {
		NbtWriter writer = new NbtStreamWriter(out);
		writer.write(compound, name);
	}

	/**
	 * Reads one NBT Tag_Compound from an InputStream.
	 */
	public static TagCompound read(InputStream in) throws IOException, NbtException {
		NbtReader reader = new NbtReader(in);
		return reader.read();
	}

	/**
	 * Reads one NBT Tag_Compound from a byte array.
	 */
	public static TagCompound read(byte[] in) throws IOException, NbtException {
		return read(new ByteArrayInputStream(in));
	}

	/**
	 * Reads one NBT Tag_Compound from a byte array.
	 */
	public static TagCompound read(byte[] in, int offset, int length) throws IOException, NbtException {
		return read(new ByteArrayInputStream(in, offset, length));
	}

	/**
	 * Get the corresponding nbt tag id of an objet, as a single byte.
	 *
	 * @param o the object
	 * @return the NBT tag id of this object
	 */
	public static byte getTagId(final Object o) {
		if (o == null) {
			return TAG_END;
		}
		if (o instanceof int[]) {
			return TAG_INT_ARRAY;
		}
		if (o instanceof byte[]) {
			return TAG_BYTE_ARRAY;
		}
		if (o instanceof HashMap) {
			return TAG_COMPOUND;
		}
		if (o instanceof List) {
			return TAG_LIST;
		}
		Class<?> class1 = o.getClass();
		if (class1 == String.class) {
			return TAG_STRING;
		}
		if (class1 == int.class || class1 == Integer.class) {
			return TAG_INT;
		}
		if (class1 == byte.class || class1 == Byte.class) {
			return TAG_BYTE;
		}
		if (class1 == short.class || class1 == Short.class) {
			return TAG_SHORT;
		}
		if (class1 == long.class || class1 == Long.class) {
			return TAG_LONG;
		}
		if (class1 == float.class || class1 == Float.class) {
			return TAG_FLOAT;
		}
		if (class1 == double.class || class1 == Double.class) {
			return TAG_DOUBLE;
		}
		throw new NbtException("Illegal NBT object type: " + o.getClass().getCanonicalName());
	}

}
