package net.fexcraft.mod.uni.world;

import java.util.UUID;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface MessageSender {

	public void send(String s);

	public void send(String str, Object... args);

	public default void sendLink(Object root, String url){
		send(url);
	}

	public void bar(String s);

	public void bar(String str, Object... args);

	public void dismount();

	public String getName();

	/** May return null. */
	public default EntityW asEntity(){
		return this instanceof EntityW ? (EntityW)this : null;
	}

	public UUID getUUID();
}
