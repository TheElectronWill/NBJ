package com.electronwill.nbt;

import java.util.HashMap;
import java.util.Optional;

/**
 * Represents a NBT Tag_Compound. Actually it's just a {@code HashMap<String, Object>} with a name.
 *
 * @author TheElectronWill
 */
public class TagCompound extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;
	private Optional<String> name;

	public TagCompound() {
		super();
		this.name = Optional.empty();
	}

	public TagCompound(final String name) {
		super();
		this.name = Optional.of(name);
	}

	/**
	 * @return the name
	 */
	public Optional<String> getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = Optional.ofNullable(name);
	}

	@Override
	public String toString() {
		return name.isPresent() ? "name=" + name + "; data= " + super.toString() : super.toString();
	}

}
