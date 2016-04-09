package com.electronwill.nbt;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author TheElectronWill
 */
public class EmptyInputStream extends InputStream {

	@Override
	public int read() throws IOException {
		return -1;
	}

	@Override
	public int available() throws IOException {
		return 0;
	}

	@Override
	public int read(byte[] b) throws IOException {
		return -1;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return -1;
	}

	@Override
	public long skip(long n) throws IOException {
		return 0;
	}

}
