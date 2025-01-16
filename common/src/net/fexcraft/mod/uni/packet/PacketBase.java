package net.fexcraft.mod.uni.packet;

import io.netty.buffer.ByteBuf;

/**
 * Originally in FVTM
 *
 * @author Ferdinand Calo' (FEX___96)
 */
public interface PacketBase<S> {

	public S fill(Object[] data);

	public void encode(ByteBuf buffer);

	public void decode(ByteBuf buffer);

	public default Object[] data(){
		return new Object[0];
	}

}
