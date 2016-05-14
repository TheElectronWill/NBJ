package com.electronwill.nbt;

/**
 * Thrown when a problem occurs during parsing or writing NBT data.
 *
 * @author TheElectronWill
 */
public class NBTException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NBTException() {
	}

	public NBTException(String message) {
		super(message);
	}

	public NBTException(String message, Throwable cause) {
		super(message, cause);
	}

	public NBTException(Throwable cause) {
		super(cause);
	}

}
