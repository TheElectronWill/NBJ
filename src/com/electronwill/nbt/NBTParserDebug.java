package com.electronwill.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * Log informations messages while parsing NBT data.
 *
 * @author TheElectronWill
 */
public class NBTParserDebug extends NBTParser {

	private static final Logger debug = Logger.getLogger("lightnbt.debug.parser");

	/**
	 * Absolute position of the parser in the read file.
	 */
	protected int track;

	public NBTParserDebug(InputStream in) {
		super(in);
		debug.setUseParentHandlers(false);
		debug.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
	}

	/**
	 * Reads the next NBT "Compound" tag, and the "End" tag after it. This method does NOT read the
	 * id byte of the tag.
	 *
	 * @param name name of the Tag_Compound (may be null).
	 * @return the next Tag_Compound
	 */
	@Override
	public TagCompound nextCompound(final String name) {
		debug.entering(getClass().getName(), "nextCompound");
		TagCompound compound = new TagCompound();
		compound.setName(name);
		debug.log(Level.INFO, "name: {0}", name);
		while (true) {
			byte typeId = nextByte();
			debug.log(Level.INFO, "typeId: {0}", typeId);
			if (typeId == 0) {//Tag_End
				debug.log(Level.INFO, "Ending that Tag_Compound ({0} entries)", compound.size());
				return compound;
			}
			String tagName = nextString();
			debug.log(Level.INFO, "tagName: {0}", tagName);
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
					throw new NBTException("Invalid tag type id: " + typeId + " at position " + track);
			}
		}
	}

	/**
	 * Reads the next NBT "List" tag. This method does NOT read the id byte of the tag.
	 *
	 * @return the next Tag_List
	 */
	@Override
	public List<?> nextList() {
		debug.entering(getClass().getName(), "nextList");
		byte typeId = nextByte();//Type of the elements in this list
		int length = nextInt();//Length of the list
		debug.log(Level.INFO, "list typeId: {0}; length: {1}", new Object[] {typeId, length});
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
				throw new NBTException("Invalid list type id: " + typeId + " at position " + track);
		}
	}

	/**
	 * Reads the length of the string (2 bytes) and the UTF-8 string (x bytes).
	 *
	 * @return the UTF-8 string.
	 */
	@Override
	protected String nextString() {
		short length = nextShort();
		debug.log(Level.INFO, "String length: {0}", length);
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
	@Override
	protected float nextFloat() {
		ensureAvailable(4);
		track += 4;
		return bbuff.getFloat();
	}

	/**
	 * Reads the next 8 bytes as a (signed) double.
	 *
	 * @return the next double
	 */
	@Override
	protected double nextDouble() {
		ensureAvailable(8);
		track += 8;
		return bbuff.getDouble();
	}

	/**
	 * Reads the next 2 bytes as a (signed) short.
	 *
	 * @return the next short
	 */
	@Override
	protected short nextShort() {
		ensureAvailable(2);
		track += 2;
		return bbuff.getShort();
	}

	/**
	 * Reads the next 4 bytes as a (signed) integer.
	 *
	 * @return the next int
	 */
	@Override
	protected int nextInt() {
		ensureAvailable(4);
		track += 4;
		return bbuff.getInt();
	}

	/**
	 * Reads the next 8 bytes as a (signed) long.
	 *
	 * @return the next long
	 */
	@Override
	protected long nextLong() {
		ensureAvailable(8);
		track += 8;
		return bbuff.getLong();

	}

	/**
	 * Reads the next byte in {@link #bytes}. If there are no more bytes, try to refill
	 * {@link #bytes} with the InputStream {@link #in}.
	 *
	 * @return the next byte
	 */
	@Override
	protected byte nextByte() {
		if (!bbuff.hasRemaining()) {
			try {
				fill();
			} catch (IOException ex) {
				throw new NBTException("Not enough bytes !", ex);
			}
		}
		track++;
		return bbuff.get();
	}

	/**
	 * Reads 4 bytes as a integer x, defining the length of the array, then read x bytes.
	 *
	 * @return the next byte array
	 */
	@Override
	protected byte[] nextByteArray() {
		int length = nextInt();//Length of the array
		debug.log(Level.INFO, "byte array length: {0}", length);
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
		debug.log(Level.INFO, "int array length: {0}", length);
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
	@Override
	protected void ensureAvailable(int n) {
		debug.entering(getClass().getName(), "ensureAvailable");
		super.ensureAvailable(n);
	}

	/**
	 * Fills all the buffer by reading from the InputStream.
	 *
	 * @throws IOException
	 */
	@Override
	protected void fill() throws IOException {
		debug.entering(getClass().getName(), "fill");
		int read = in.read(bytes);
		if (read == -1) {
			throw new IOException("End of stream");
		}
		debug.log(Level.INFO, "{0} bytes read", read);
		bbuff.limit(read);//read bytes + 1
		bbuff.position(0);
	}

	/**
	 * Keep the remaining data, and read further bytes from the InputStream.
	 *
	 * @throws IOException
	 */
	@Override
	protected void refill() throws IOException {
		debug.entering(getClass().getName(), "refill");
		int left = bbuff.remaining();//Nombre de bytes restants
		debug.log(Level.INFO, "{0} bytes remaining", left);
		System.arraycopy(bytes, bbuff.position(), bytes, 0, left);//On décale tout au début
		int read = in.read(bytes, left, bytes.length - left);
		if (read == -1) {
			throw new IOException("End of stream");
		}
		debug.log(Level.INFO, "{0} bytes read", read);
		bbuff.limit(left + read);//remaining bytes + read bytes
		bbuff.position(0);
	}

}
