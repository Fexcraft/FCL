package net.fexcraft.mod.uni.world;

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

}
