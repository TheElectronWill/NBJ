package com.electronwill.nbt;

import java.util.Map;

/**
 *
 * @author TheElectronWill
 */
public final class ReadTagCompound {

	public final String name;
	public final Map<String, Object> data;

	public ReadTagCompound(String name, Map<String, Object> data) {
		this.name = name;
		this.data = data;
	}

}
