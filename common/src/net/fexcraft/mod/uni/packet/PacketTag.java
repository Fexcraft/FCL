package net.fexcraft.mod.uni.packet;

import io.netty.buffer.ByteBuf;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.fcl.FCL;

import java.nio.charset.StandardCharsets;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class PacketTag implements PacketBase<PacketTag> {

    public String lis;
    public TagCW com;

    @Override
    public PacketTag fill(Object... data){
        lis = data[0].toString();
        com = TagCW.wrap(data[1]);
        return this;
    }

    @Override
    public void encode(ByteBuf buffer){
        byte[] bytes = lis.getBytes(StandardCharsets.UTF_8);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
        FCL.writeTag(buffer, com);
    }

    @Override
    public void decode(ByteBuf buffer){
		byte[] dest = new byte[buffer.readInt()];
		buffer.readBytes(dest);
        lis = new String(dest, StandardCharsets.UTF_8);
        com = FCL.readTag(buffer);
    }

}
