package com.electronwill.nbt;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses NBT data.
 *
 * @author TheElectronWill
 */
public final class NBTReader {

	private final DataInputStream in;

	public NBTReader(DataInputStream in) {
		this.in = in;
	}

	public NBTReader(InputStream in) {
		this.in = new DataInputStream(in);
	}

	public ReadTagCompound read() throws IOException {
		byte typeId = in.readByte();
		if (typeId != NBT.TAG_COMPOUND) {
			return null;
		}
		String name = in.readUTF();
		Map<String, Object> data = readCompound();
		return new ReadTagCompound(name, data);
	}

	/**
	 * Reads the next NBT Tag_Compound, and the Tag_End after it. This method does NOT read the
	 * id byte of the tag.
	 */
	public Map<String, Object> readCompound() throws IOException {
		Map compound = new HashMap<>();
		while (true) {
			byte typeId = in.readByte();
			if (typeId == 0) {//Tag_End, which has no name
				return compound;
			}
			String tagName = in.readUTF();
			switch (typeId) {
				case 1://Tag_Byte
					compound.put(tagName, in.readByte());
					break;
				case 2://Tag_Short
					compound.put(tagName, in.readShort());
					break;
				case 3://Tag_Int
					compound.put(tagName, in.readInt());
					break;
				case 4://Tag_Long
					compound.put(tagName, in.readLong());
					break;
				case 5://Tag_Float
					compound.put(tagName, in.readFloat());
					break;
				case 6://Tag_Double
					compound.put(tagName, in.readDouble());
					break;
				case 7://Tag_Byte_Array
					compound.put(tagName, nextByteArray());
					break;
				case 8://Tag_String
					compound.put(tagName, in.readUTF());
					break;
				case 9://Tag_List
					compound.put(tagName, nextList());
					break;
				case 10://Tag_Compound
					compound.put(tagName, readCompound());
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
	public List<?> nextList() throws IOException {
		byte typeId = in.readByte();//Type of the elements in this list
		int length = in.readInt();//Length of the list
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
	List<Integer> nextIntList(final int length) throws IOException {
		ArrayList<Integer> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(in.readInt());
		}
		return list;
	}

	/**
	 * Reads the next x bytes.
	 *
	 * @param length the number of bytes to read
	 * @return the next x bytes
	 */
	List<Byte> nextByteList(final int length) throws IOException {
		ArrayList<Byte> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(in.readByte());
		}
		return list;
	}

	/**
	 * Reads the next x shorts.
	 *
	 * @param length the number of shorts to read
	 * @return the next x shorts
	 */
	List<Short> nextShortList(final int length) throws IOException {
		ArrayList<Short> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(in.readShort());
		}
		return list;
	}

	/**
	 * Reads the next x longs.
	 *
	 * @param length the number of longs to read
	 * @return the next x longs
	 */
	List<Long> nextLongList(final int length) throws IOException {
		ArrayList<Long> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(in.readLong());
		}
		return list;
	}

	/**
	 * Reads the next x floats.
	 *
	 * @param length the number of floats to read
	 * @return the next x floats
	 */
	List<Float> nextFloatList(final int length) throws IOException {
		ArrayList<Float> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(in.readFloat());
		}
		return list;
	}

	/**
	 * Reads the next x doubles.
	 *
	 * @param length the number of doubles to read
	 * @return the next x doubles
	 */
	List<Double> nextDoubleList(final int length) throws IOException {
		ArrayList<Double> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(in.readDouble());
		}
		return list;
	}

	/**
	 * Reads the next x byte arrays.
	 *
	 * @param length the number of byte arrays to read
	 * @return the next x byte arrays
	 */
	List<byte[]> nextByteArrayList(final int length) throws IOException {
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
	List<int[]> nextIntArrayList(final int length) throws IOException {
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
	List<String> nextStringList(final int length) throws IOException {
		ArrayList<String> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(in.readUTF());
		}
		return list;
	}

	/**
	 * Reads the next x Tag_List.
	 *
	 * @param length the number of lists to read
	 * @return the next x lists
	 */
	List<List<?>> nextNestedList(final int length) throws IOException {
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
	List<Map<String, Object>> nextCompoundList(final int length) throws IOException {
		ArrayList<Map<String, Object>> list = new ArrayList<>(length);
		for (int j = 0; j < length; j++) {
			list.add(readCompound());
		}
		return list;
	}

	/**
	 * Reads 4 bytes as a integer x, defining the length of the array, then read x bytes.
	 *
	 * @return the next byte array
	 */
	byte[] nextByteArray() throws IOException {
		int length = in.readInt();//Length of the array
		byte[] array = new byte[length];
		in.readFully(array);
		return array;
	}

	/**
	 * Reads 4 bytes as a integer x, defining the length of the array, then read x integers.
	 *
	 * @return the next int array
	 */
	int[] nextIntArray() throws IOException {
		int length = in.readInt();//Length of the array
		int[] array = new int[length];
		for (int j = 0; j < length; j++) {
			int n = in.readInt();
			array[j] = n;
		}
		return array;
	}

}
