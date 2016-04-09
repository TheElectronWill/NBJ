package com.electronwill.nbt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;

/**
 * Utility class for reading and writing NBT data.
 *
 * @author TheElectronWill
 */
public final class NBT {

	private NBT() {
	}

	/**
	 * NBT Tag ID (takes place before the tag payload).
	 *
	 * @see http://wiki.vg/NBT
	 * @see http://minecraft.gamepedia.com/NBT_format
	 */
	public static final byte TAG_END = 0, TAG_BYTE = 1, TAG_SHORT = 2, TAG_INT = 3, TAG_LONG = 4, TAG_FLOAT = 5, TAG_DOUBLE = 6,
			TAG_BYTE_ARRAY = 7, TAG_STRING = 8, TAG_LIST = 9, TAG_COMPOUND = 10, TAG_INT_ARRAY = 11;

	/**
	 * Magic value byte of a compressed file.
	 */
	public static final byte GZIP_MAGIC_1 = (byte) 0x1f, GZIP_MAGIC_2 = (byte) 0x8b, ZLIB_MAGIC = (byte) 0x78;

	/**
	 * Writes a TagCompound to an OutputStream.
	 */
	public static void write(TagCompound compound, OutputStream out) throws IOException, NBTException {
		NBTWriter writer = new NBTWriter(out);
		writer.write(compound);
		writer.close();
	}

	/**
	 * Reads one NBT Tag_Compound from a file. First the encoding of the file is detected, and a decompression
	 * method is chosen if needed.
	 */
	public static TagCompound detectAndRead(File file) throws IOException, NBTException {
		FileInputStream input = new FileInputStream(file);
		return detectAndRead(input);
	}

	/**
	 * Reads one NBT Tag_Compound from an InputStream. First the encoding of the stream is detected, and a
	 * decompression method is chosen if needed. This method actually calls
	 * {@link #detectAndRead(java.io.InputStream, false)}.
	 *
	 * @param in InputStream to read data from
	 * @return the parsed nbt data, as a {@link TagCompound}.
	 * @throws IOException if a reading problem occur
	 * @throws NBTException if a parsing problem occur
	 */
	public static TagCompound detectAndRead(InputStream in) throws IOException, NBTException {
		return detectAndRead(in, false);
	}

	/**
	 * Reads one NBT Tag_Compound from an InputStream. This method does not support compressed data.
	 */
	public static TagCompound read(InputStream in) throws IOException, NBTException {
		NBTParser parser = new NBTParser(in);
		return parser.parse();
	}

	/**
	 * Reads one or more NBT Tag_Compound from an InputStream. First the encoding of the stream is detected,
	 * and a decompression method is chosen if needed.
	 */
	public static List<TagCompound> detectAndReadAll(InputStream in) throws IOException, NBTException {
		InputStream cin = CompressionDetector.getStream(in);
		NBTParser parser = new NBTParser(cin);
		return parser.parseAll();
	}

	/**
	 * Reads one or more NBT Tag_Compound from an InputStream. This method does not support compressed data.
	 */
	public static List<TagCompound> readAll(InputStream in) throws IOException, NBTException {
		NBTParser parser = new NBTParser(in);
		return parser.parseAll();
	}

	/**
	 * Reads one NBT Tag_Compound from an InputStream. First the encoding of the stream is detected, and a
	 * decompression method is chosen if needed.
	 *
	 * @param in InputStream to read data from
	 * @param debug <code>true</code> if we should use {@link NBTParserDebug} instead of
	 * {@link NBTParser}.
	 * @return the parsed nbt data, as a {@link TagCompound}.
	 * @throws IOException
	 * @throws NBTException If an unexpected problem occur.
	 */
	public static TagCompound detectAndRead(InputStream in, boolean debug) throws IOException, NBTException {
		InputStream cin = CompressionDetector.getStream(in);
		NBTParser parser = debug ? new NBTParserDebug(cin) : new NBTParser(cin);
		return parser.parse();
	}

	/**
	 * Reads a NBT Tag_Compound from a byte array containing. This method does not support compressed data.
	 */
	public static TagCompound read(byte[] input) throws IOException, NBTException {
		NBTParser parser = new NBTParser(input);
		return parser.parse();
	}

	/**
	 * Reads one NBT Tag_Compound from a ByteBuffer. This method does not support compressed data.
	 */
	public static TagCompound read(ByteBuffer input) throws IOException, NBTException {
		NBTParser parser = new NBTParser(input);
		return parser.parse();
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
		throw new NBTException("Illegal NBT object type: " + o.getClass().getCanonicalName());
	}

}
