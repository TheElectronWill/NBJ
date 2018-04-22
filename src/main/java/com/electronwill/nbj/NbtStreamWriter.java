package com.electronwill.nbj;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Writes NBT data.
 *
 * @author TheElectronWill
 */
public class NbtStreamWriter extends NbtWriter {

	protected final OutputStream stream;

	public NbtStreamWriter(DataOutputStream out) {
		super(out);
		this.stream = out;
	}

	public NbtStreamWriter(OutputStream out) {
		super(new DataOutputStream(out));
		this.stream = out;
	}

	/**
	 * Flushes the underlying OutputStream.
	 */
	public void flush() throws IOException {
		stream.flush();
	}

	/**
	 * Definitely closes the underlying OutputStream.
	 */
	public void close() throws IOException {
		stream.close();
	}

}
