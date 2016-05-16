package com.electronwill.nbt;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Writes NBT data.
 *
 * @author TheElectronWill
 */
public final class NBTWriter {

	private final DataOutputStream out;

	public NBTWriter(DataOutputStream out) {
		this.out = out;
	}

	public NBTWriter(OutputStream out) {
		this.out = new DataOutputStream(out);
	}

	/**
	 * Writes a NBT Tag_Compound into the underlying OutputStream of this writer. Writes its type id (1 byte),
	 * the length of its name (2 bytes), its name (x bytes), its payload, and then the byte 0.
	 */
	public void write(Map<String, Object> compound, String name) throws IOException {
		out.write(NBT.TAG_COMPOUND);//Writes its type
		out.writeUTF(name);
		writeCompound(compound);
	}

	/**
	 * Flushes the underlying OutputStream.
	 */
	public void flush() throws IOException {
		out.flush();
	}

	/**
	 * Definitely closes the underlying OutputStream.
	 */
	public void close() throws IOException {
		out.close();
	}

	/**
	 * Writes the value of a Tag_Compound. Does NOT write the {@link NBT#TAG_COMPOUND} byte.
	 */
	private void writeCompound(Map<String, Object> compound) throws IOException {
		for (Entry<String, Object> e : compound.entrySet()) {
			String key = e.getKey();
			Object value = e.getValue();

			byte typeId = NBT.getTagId(value);//NBT Type of the value
			out.write(typeId);//We write its type
			out.writeUTF(key);//We write its name (4 bytes length + UTF-8 bytes).
			switch (typeId) { //We write its value:
				case NBT.TAG_BYTE://Tag_Byte
					out.write((byte) value);
					break;
				case NBT.TAG_SHORT://Tag_Short
					out.writeShort((short) value);
					break;
				case NBT.TAG_INT://Tag_Int
					out.writeInt((int) value);
					break;
				case NBT.TAG_LONG://Tag_Long
					out.writeLong((long) value);
					break;
				case NBT.TAG_FLOAT://Tag_Float
					out.writeFloat((float) value);
					break;
				case NBT.TAG_DOUBLE://Tag_Double
					out.writeDouble((double) value);
					break;
				case NBT.TAG_BYTE_ARRAY://Tag_Byte_Array
					writeByteArray((byte[]) value);
					break;
				case NBT.TAG_STRING://Tag_String
					out.writeUTF((String) value);
					break;
				case NBT.TAG_LIST://Tag_List
					writeList((List<?>) value);
					break;
				case NBT.TAG_COMPOUND://Tag_Compound
					writeCompound((Map<String, Object>) value);
					break;
				case NBT.TAG_INT_ARRAY://Tag_Int_Array
					writeIntArray((int[]) value);
					break;
				default:
					throw new NBTException("Illegal value of type " + typeId);
			}
		}
		out.write(0);
	}

	/**
	 * Writes the value of a Tag_List. Does NOT write the {@link NBT#TAG_LIST}byte.
	 */
	private void writeList(List<?> list) throws IOException {
		byte typeId = list.isEmpty() ? NBT.TAG_END : NBT.getTagId(list.get(0));//Type of the elements in this list
		out.write(typeId);
		out.writeInt(list.size());
		switch (typeId) {
			case NBT.TAG_BYTE://Tag_Byte
				for (Object o : list) {
					out.writeByte((byte) o);
				}
				break;
			case NBT.TAG_SHORT://Tag_Short
				for (Object o : list) {
					out.writeShort((short) o);
				}
				break;
			case NBT.TAG_INT://Tag_Int
				for (Object o : list) {
					out.writeInt((int) o);
				}
				break;
			case NBT.TAG_LONG://Tag_Long
				for (Object o : list) {
					out.writeLong((long) o);
				}
				break;
			case NBT.TAG_FLOAT://Tag_Float
				for (Object o : list) {
					out.writeFloat((float) o);
				}
				break;
			case NBT.TAG_DOUBLE://Tag_Double
				for (Object o : list) {
					out.writeDouble((double) o);
				}
				break;
			case NBT.TAG_BYTE_ARRAY://Tag_Byte_Array
				for (Object o : list) {
					writeByteArray((byte[]) o);
				}
				break;
			case NBT.TAG_STRING://Tag_String
				for (Object o : list) {
					out.writeUTF((String) o);
				}
				break;
			case NBT.TAG_LIST://Tag_List
				for (Object o : list) {
					writeList((List<?>) o);
				}
				break;
			case NBT.TAG_COMPOUND://Tag_Compound
				for (Object o : list) {
					writeCompound((Map<String, Object>) o);
				}
				break;
			case NBT.TAG_INT_ARRAY://Tag_Int_Array
				for (Object o : list) {
					writeIntArray((int[]) o);
				}
				break;
		}
	}

	/**
	 * Writes successive integers.
	 */
	private void writeIntArray(final int[] array) throws IOException {
		out.writeInt(array.length);
		for (int i : array) {
			out.writeInt(i);
		}
	}

	/**
	 * Writes the length of the array and its value.
	 */
	private void writeByteArray(final byte[] array) throws IOException {
		out.writeInt(array.length);
		for (byte b : array) {
			out.write(b);
		}
	}

}
