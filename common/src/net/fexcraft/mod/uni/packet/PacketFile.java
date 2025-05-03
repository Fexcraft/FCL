package net.fexcraft.mod.uni.packet;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class PacketFile implements PacketBase<PacketFile> {

    public String lis;
    public String loc;
    public byte[] img;

    @Override
    public PacketFile fill(Object... data){
        lis = data[0] == null ? "def" : (String)data[0];
        loc = (String)data[1];
        if(data.length > 2) img = (byte[])data[2];
        return this;
    }

    @Override
    public void encode(ByteBuf buffer){
        byte[] bytes = lis.getBytes(StandardCharsets.UTF_8);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
        bytes = loc.getBytes(StandardCharsets.UTF_8);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
        if(img == null){
            buffer.writeInt(0);
            return;
        }
        buffer.writeInt(img.length);
        buffer.writeBytes(img);
    }

    @Override
    public void decode(ByteBuf buffer){
        lis = buffer.readCharSequence(buffer.readInt(), StandardCharsets.UTF_8).toString();
        loc = buffer.readCharSequence(buffer.readInt(), StandardCharsets.UTF_8).toString();
        int length = buffer.readInt();
        if(length == 0) return;
        buffer.readBytes(img = new byte[length]);
    }

}
