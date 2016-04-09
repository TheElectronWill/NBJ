package com.electronwill.nbt;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * Writes NBT data.
 *
 * @author TheElectronWill
 */
public class NBTWriter {

	/**
	 * Underlying Output stream. We write all data into this stream.
	 */
	protected OutputStream out;

	/**
	 * ByteBuffer converting short/int/long/float/double to bytes.
	 */
	protected ByteBuffer bbuff = ByteBuffer.allocate(8);

	/**
	 * Underlying array of {@link #bbuff}.
	 */
	protected byte[] numberBytes = bbuff.array();

	public NBTWriter(OutputStream out) {
		this.out = out;
	}

	/**
	 * Writes a NBT Tag_Compound into the underlying OutputStream of this
	 * writer. Writes its type id (1 byte), the length of its name (2 bytes),
	 * its name (x bytes) its payload, and then the byte 0.
	 *
	 * @param compound
	 * @throws IOException
	 */
	public void write(TagCompound compound) throws IOException {
		out.write(NBT.TAG_COMPOUND);//Writes its type
		Optional<String> compoundName = compound.getName();
		if (compoundName.isPresent()) {
			writeString(compoundName.get());//Writes its name, if present
		} else {//If there is no name, we write 2 zero bytes:
			out.write(0);
			out.write(0);
		}
		writeCompound(compound);
	}

	/**
	 * Flushes the underlying OutputStream.
	 *
	 * @throws IOException
	 */
	public void flush() throws IOException {
		out.flush();
	}

	/**
	 * Closes definitely the underlying OutputStream, and release any resources
	 * associated with this NBTWriter.
	 *
	 * @throws IOException
	 */
	public void close() throws IOException {
		//out.flush();
		out.close();
		bbuff = null;
		numberBytes = null;
	}

	/**
	 * Writes the value of a Tag_Compound. Does NOT write the
	 * {@link NBT#TAG_COMPOUND} byte.
	 *
	 * @param compound
	 * @throws IOException
	 */
	protected void writeCompound(TagCompound compound) throws IOException {
		for (Entry<String, Object> e : compound.entrySet()) {
			String key = e.getKey();
			Object value = e.getValue();

			byte typeId = NBT.getTagId(value);//NBT Type of the value
			out.write(typeId);//We write its type
			writeString(key);//We write its name (4 bytes length + UTF-8 bytes).
			switch (typeId) { //We write its value:
				case NBT.TAG_BYTE://Tag_Byte
					out.write((byte) value);
					break;
				case NBT.TAG_SHORT://Tag_Short
					writeShort((short) value);
					break;
				case NBT.TAG_INT://Tag_Int
					writeInt((int) value);
					break;
				case NBT.TAG_LONG://Tag_Long
					writeLong((long) value);
					break;
				case NBT.TAG_FLOAT://Tag_Float
					writeFloat((float) value);
					break;
				case NBT.TAG_DOUBLE://Tag_Double
					writeDouble((double) value);
					break;
				case NBT.TAG_BYTE_ARRAY://Tag_Byte_Array
					writeByteArray((byte[]) value);
					break;
				case NBT.TAG_STRING://Tag_String
					writeString((String) value);
					break;
				case NBT.TAG_LIST://Tag_List
					writeList((List<?>) value);
					break;
				case NBT.TAG_COMPOUND://Tag_Compound
					writeCompound((TagCompound) value);
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
	 * Writes the value of a Tag_List. Does NOT write the {@link NBT#TAG_LIST}
	 * byte.
	 *
	 * @param list
	 * @throws IOException
	 */
	protected void writeList(List<?> list) throws IOException {
		byte typeId = list.isEmpty() ? NBT.TAG_END : NBT.getTagId(list.get(0));//Type of the elements in this list
		out.write(typeId);
		writeInt(list.size());
		switch (typeId) {
			case NBT.TAG_BYTE://Tag_Byte
				for (Object o : list) {
					out.write((byte) o);
				}
				break;
			case NBT.TAG_SHORT://Tag_Short
				for (Object o : list) {
					writeShort((short) o);
				}
				break;
			case NBT.TAG_INT://Tag_Int
				for (Object o : list) {
					writeInt((int) o);
				}
				break;
			case NBT.TAG_LONG://Tag_Long
				for (Object o : list) {
					writeLong((long) o);
				}
				break;
			case NBT.TAG_FLOAT://Tag_Float
				for (Object o : list) {
					writeFloat((float) o);
				}
				break;
			case NBT.TAG_DOUBLE://Tag_Double
				for (Object o : list) {
					writeDouble((double) o);
				}
				break;
			case NBT.TAG_BYTE_ARRAY://Tag_Byte_Array
				for (Object o : list) {
					writeByteArray((byte[]) o);
				}
				break;
			case NBT.TAG_STRING://Tag_String
				for (Object o : list) {
					writeString((String) o);
				}
				break;
			case NBT.TAG_LIST://Tag_List
				for (Object o : list) {
					writeList((List<?>) o);
				}
				break;
			case NBT.TAG_COMPOUND://Tag_Compound
				for (Object o : list) {
					writeCompound((TagCompound) o);
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
	 * Write a CharSequence nbt value.
	 *
	 * @param cs
	 * @throws IOException
	 */
	protected void writeCharSequence(final CharSequence cs) throws IOException {
		writeString(cs.toString());
	}

	/**
	 * Write a the utf8 bytes of a String.
	 *
	 * @param s
	 * @throws IOException
	 */
	protected void writeString(final String s) throws IOException {
		byte[] utf8 = s.getBytes(StandardCharsets.UTF_8);
		writeShort((short) utf8.length);
		out.write(utf8);
	}

	/**
	 * Writes the 4 bytes of an int value.
	 *
	 * @param i
	 * @throws IOException
	 */
	protected void writeInt(final int i) throws IOException {
		bbuff.putInt(0, i);
		out.write(numberBytes, 0, 4);
	}

	/**
	 * Writes the 2 bytes of a short value.
	 *
	 * @param s
	 * @throws IOException
	 */
	protected void writeShort(final short s) throws IOException {
		bbuff.putShort(0, s);
		out.write(numberBytes, 0, 2);
	}

	/**
	 * Writes the 8 bytes of a long value.
	 *
	 * @param l
	 * @throws IOException
	 */
	protected void writeLong(final long l) throws IOException {
		bbuff.putLong(0, l);
		out.write(numberBytes, 0, 8);
	}

	/**
	 * Writes the 4 bytes of a float value.
	 *
	 * @param f
	 * @throws IOException
	 */
	protected void writeFloat(final float f) throws IOException {
		bbuff.putFloat(0, f);
		out.write(numberBytes, 0, 4);
	}

	/**
	 * Writes the 8 bytes of a double value
	 *
	 * @param d
	 * @throws IOException
	 */
	protected void writeDouble(final double d) throws IOException {
		bbuff.putDouble(0, d);
		out.write(numberBytes, 0, 8);
	}

	/**
	 * Writes successive integers.
	 *
	 * @param array
	 * @throws IOException
	 */
	protected void writeIntArray(final int[] array) throws IOException {
		writeInt(array.length);
		for (int i : array) {
			writeInt(i);
		}
	}

	/**
	 * Writes the length of the array and its value.
	 *
	 * @param array
	 * @throws IOException
	 */
	protected void writeByteArray(final byte[] array) throws IOException {
		writeInt(array.length);
		for (byte b : array) {
			out.write(b);
		}
	}

}
