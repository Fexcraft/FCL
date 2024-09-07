package net.fexcraft.mod.uni;

import net.fexcraft.mod.uni.tag.TagCW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface Appendable<T> {

	public default void save(T type, TagCW com){}

	public default void load(T type, TagCW com){}

	/** May return `null` if conditions not met. */
	public Appendable<T> create(T type);

	public String id();

}
