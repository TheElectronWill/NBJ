package com.electronwill.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses NBT data.
 *
 * @author TheElectronWill
 */
public class NBTParser {

	/**
	 * Underlying Input stream. We read all data from this stream.
	 */
	protected InputStream in;

	/**
	 * ByteBuffer storing the data and converting short/int/long/float/double to bytes.
	 */
	protected ByteBuffer bbuff;

	/**
	 * Underlying array of {@link #bbuff}. Contains the read bytes.
	 */
	protected byte[] bytes;

	public NBTParser(InputStream in) {
		this.in = in;
		bbuff = ByteBuffer.allocate(4096);
		bytes = bbuff.array();
	}

	public NBTParser(ByteBuffer bbuff) {
		in = null;
		this.bbuff = bbuff;
		bytes = bbuff.array();
	}

	public NBTParser(byte[] bytes) {
		in = null;
		this.bbuff = ByteBuffer.wrap(bytes);
		this.bytes = bytes;
	}

	/**
	 * Parses a NBT Document, e.g. a Tag_Compound.
	 *
	 * @return a TagCompound containing the data of this NBT InputStream.
	 * @throws java.io.IOException
	 */
	public TagCompound parse() throws IOException {
		if (in != null) {
			fill();
		}
		byte typeId = nextByte();
		if (typeId != 10) {//does NOT begins with a Tag_Compound !!
			throw new NBTException("This document does begins with byte " + typeId + ".Byte A (10) expected!");
		}
		String name = nextString();
		return nextCompound(name);
	}

	/**
	 * Parses several NBT Documents, e.g. several Tag_Compound.
	 *
	 * @return one or more TagCompound containing the data of this NBT InputStream.
	 */
	public List<TagCompound> parseAll() {
		ArrayList<TagCompound> list = new ArrayList<>();
		while (true) {
			if (bbuff.remaining() == 0) {
				if (in == null) {
					return list;//Don't mind if there are no more bytes: it means we've read all the compound tags.
				}
				try {
					fill();
				} catch (IOException ex) {
					return list;//Don't mind if there are no more bytes: it means we've read all the compound tags.
				}
			}
			byte typeId = bbuff.get();
			if (typeId != NBT.TAG_COMPOUND) {
				throw new NBTException("Unexcpected type id " + typeId + ". Expected a Tag_Compound (id" + NBT.TAG_COMPOUND + ")");
			}
			String name = nextString();
			TagCompound compound = nextCompound(name);
			list.add(compound);
		}
	}

	/**
	 * Reads the next NBT "Compound" tag, and the "End" tag after it. This method does NOT read the
	 * id byte of the tag.
	 *
	 * @param name name of the Tag_Compound (may be null).
	 * @return the next Tag_Compound
	 */
	public TagCompound nextCompound(final String name) {
		TagCompound compound = new TagCompound();
		compound.setName(name);
		while (true) {
			byte typeId = nextByte();
			if (typeId == 0) {//Tag_End
				return compound;
			}
			String tagName = nextString();
			switch (typeId) {
				case 1://Tag_Byte
					compound.put(tagName, nextByte());
					break;
				case 2://Tag_Short
					compound.put(tagName, nextShort());
					break;
				case 3://Tag_Int
					compound.put(tagName, nextInt());
					break;
				case 4://Tag_Long
					compound.put(tagName, nextLong());
					break;
				case 5://Tag_Float
					compound.put(tagName, nextFloat());
					break;
				case 6://Tag_Double
					compound.put(tagName, nextDouble());
					break;
				case 7://Tag_Byte_Array
					compound.put(tagName, nextByteArray());
					break;
				case 8://Tag_String
					compound.put(tagName, nextString());
					break;
				case 9://Tag_List
					compound.put(tagName, nextList());
					break;
				case 10://Tag_Compound
					compound.put(tagName, nextCompound(tagName));
					break;
				case 11://Tag_Int_Array
					compound.put(tagName, nextIntArray());
					break;
				default:
					throw new NBTException("Invalid tag type id: " + typeId);
			}
		}
	}

	/**
	 * Reads the next NBT "List" tag. This method does NOT read the id byte of the tag.
	 *
	 * @return the next Tag_List
	 */
	public List<?> nextList() {
		byte typeId = nextByte();//Type of the elements in this list
		int length = nextInt();//Length of the list
		switch (typeId) {
			case 0://Tag_End
				ArrayList<Byte> endList = new ArrayList<>(length);
				for (int j = 0; j < length; j++) {
					endList.add((byte) 0);
				}
				return endList;
			case 1://Tag_Byte
				return nextByteList(length);
			case 2://Tag_Short
				return nextShortList(length);
			case 3://Tag_Int
				return nextIntList(length);
			case 4://Tag_Long
				return nextLongList(length);
			case 5://Tag_Float
				return nextFloatList(length);
			case 6://Tag_Double
				return nextDoubleList(length);
			case 7://Tag_Byte_Array
				return nextByteArrayList(length);
			case 8://Tag_String
				return nextStringList(length);
			case 9://Tag_List
				return nextNestedList(length);
			case 10://Tag_Compound
				return nextCompoundList(length);
			case 11://Tag_Int_Array
				return nextIntArrayList(length);
			default:
				throw new NBTException("Invalid list type id: " + typeId);
		}
	}

	/**
	 * Reads the next x integers.
	 *
	 * @param length the number of integers to read
	 * @return the next x integers
	 */
	protected List<Integer> nextIntList(final int length) {
		ArrayList<Integer> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(nextInt());
		}
		return list;
	}

	/**
	 * Reads the next x bytes.
	 *
	 * @param length the number of bytes to read
	 * @return the next x bytes
	 */
	protected List<Byte> nextByteList(final int length) {
		ArrayList<Byte> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(nextByte());
		}
		return list;
	}

	/**
	 * Reads the next x shorts.
	 *
	 * @param length the number of shorts to read
	 * @return the next x shorts
	 */
	protected List<Short> nextShortList(final int length) {
		ArrayList<Short> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(nextShort());
		}
		return list;
	}

	/**
	 * Reads the next x longs.
	 *
	 * @param length the number of longs to read
	 * @return the next x longs
	 */
	protected List<Long> nextLongList(final int length) {
		ArrayList<Long> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(nextLong());
		}
		return list;
	}

	/**
	 * Reads the next x floats.
	 *
	 * @param length the number of floats to read
	 * @return the next x floats
	 */
	protected List<Float> nextFloatList(final int length) {
		ArrayList<Float> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(nextFloat());
		}
		return list;
	}

	/**
	 * Reads the next x doubles.
	 *
	 * @param length the number of doubles to read
	 * @return the next x doubles
	 */
	protected List<Double> nextDoubleList(final int length) {
		ArrayList<Double> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(nextDouble());
		}
		return list;
	}

	/**
	 * Reads the next x byte arrays.
	 *
	 * @param length the number of byte arrays to read
	 * @return the next x byte arrays
	 */
	protected List<byte[]> nextByteArrayList(final int length) {
		ArrayList<byte[]> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(nextByteArray());
		}
		return list;
	}

	/**
	 * Reads the next x integer arrays.
	 *
	 * @param length the number of int arrays to read
	 * @return the next int int arrays
	 */
	protected List<int[]> nextIntArrayList(final int length) {
		ArrayList<int[]> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(nextIntArray());
		}
		return list;
	}

	/**
	 * Reads the next x Strings.
	 *
	 * @param length the number of Strings to read
	 * @return the next x Strings
	 */
	protected List<String> nextStringList(final int length) {
		ArrayList<String> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(nextString());
		}
		return list;
	}

	/**
	 * Reads the next x Tag_List.
	 *
	 * @param length the number of lists to read
	 * @return the next x lists
	 */
	protected List<List<?>> nextNestedList(final int length) {
		ArrayList<List<?>> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(nextList());
		}
		return list;
	}

	/**
	 * Reads the next x Tag_Compound.
	 *
	 * @param length the number of Tag_Compound to read
	 * @return the next x compounds
	 */
	protected List<TagCompound> nextCompoundList(final int length) {
		ArrayList<TagCompound> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(nextCompound(null));
		}
		return list;
	}

	/**
	 * Reads the length of the string (2 bytes) and the UTF-8 string (x bytes).
	 *
	 * @return the UTF-8 string.
	 */
	protected String nextString() {
		short length = nextShort();
		if (length == 0) {
			return new String();
		}
		byte[] sbytes = new byte[length];
		for (int j = 0; j < sbytes.length; j++) {
			sbytes[j] = nextByte();
		}
		return new String(sbytes, StandardCharsets.UTF_8);
	}

	/**
	 * Reads the next 4 bytes as a (signed) float.
	 *
	 * @return the next float
	 */
	protected float nextFloat() {
		ensureAvailable(4);
		return bbuff.getFloat();
	}

	/**
	 * Reads the next 8 bytes as a (signed) double.
	 *
	 * @return the next double
	 */
	protected double nextDouble() {
		ensureAvailable(8);
		return bbuff.getDouble();
	}

	/**
	 * Reads the next 2 bytes as a (signed) short.
	 *
	 * @return the next short
	 */
	protected short nextShort() {
		ensureAvailable(2);
		return bbuff.getShort();
	}

	/**
	 * Reads the next 4 bytes as a (signed) integer.
	 *
	 * @return the next int
	 */
	protected int nextInt() {
		ensureAvailable(4);
		return bbuff.getInt();
	}

	/**
	 * Reads the next 8 bytes as a (signed) long.
	 *
	 * @return the next long
	 */
	protected long nextLong() {
		ensureAvailable(8);
		return bbuff.getLong();

	}

	/**
	 * Reads the next byte in {@link #bytes}. If there are no more bytes, try to refill
	 * {@link #bytes} with the InputStream {@link #in}.
	 *
	 * @return the next byte
	 */
	protected byte nextByte() {
		if (!bbuff.hasRemaining()) {
			if (in == null) {
				throw new NBTException("Not enough bytes available !");
			} else {
				try {
					fill();
				} catch (IOException ex) {
					throw new NBTException("Not enough bytes available !", ex);
				}
			}
		}
		return bbuff.get();
	}

	/**
	 * Reads 4 bytes as a integer x, defining the length of the array, then read x bytes.
	 *
	 * @return the next byte array
	 */
	protected byte[] nextByteArray() {
		int length = nextInt();//Length of the array
		byte[] a = new byte[length];
		for (int j = 0; j < length; j++) {
			byte b = nextByte();
			a[j] = b;
		}
		return a;
	}

	/**
	 * Reads 4 bytes as a integer x, defining the length of the array, then read x integers.
	 *
	 * @return the next int array
	 */
	protected int[] nextIntArray() {
		int length = nextInt();//Length of the array
		int[] a = new int[length];
		for (int j = 0; j < length; j++) {
			int n = nextInt();
			a[j] = n;
		}
		return a;
	}

	/**
	 * Ensures that at least n more bytes are available.
	 *
	 * @param n the number of bytes that must be available
	 */
	protected void ensureAvailable(int n) {
		if (bbuff.remaining() < n) {
			try {
				refill();
				if (bbuff.remaining() < n) {
					throw new NBTException("Not enough bytes !");
				}
			} catch (IOException ex) {
				throw new NBTException("Not enough bytes !", ex);
			}
		}

	}

	/**
	 * Fills all the buffer by reading from the InputStream.
	 *
	 * @throws IOException
	 */
	protected void fill() throws IOException {
		int read = in.read(bytes);
		if (read == -1) {
			throw new IOException("End of stream");
		}
		bbuff.limit(read);
		bbuff.position(0);
	}

	/**
	 * Keep the remaining data, and read further bytes from the InputStream.
	 *
	 * @throws IOException
	 */
	protected void refill() throws IOException {
		int left = bbuff.remaining();//Nombre de bytes restants
		System.arraycopy(bytes, bbuff.position(), bytes, 0, left);//On décale tout au début
		int read = in.read(bytes, left, bytes.length - left);
		if (read == -1) {
			throw new IOException("End of stream");
		}
		bbuff.limit(left + read);//remaining bytes + read bytes
		bbuff.position(0);
	}

}
