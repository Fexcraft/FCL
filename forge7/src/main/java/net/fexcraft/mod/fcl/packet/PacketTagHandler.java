package net.fexcraft.mod.fcl.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.fexcraft.mod.fcl.UniFCL;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.packet.PacketTag;
import net.fexcraft.mod.uni.tag.TagCW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class PacketTagHandler {

    public static class I12_PacketTag extends PacketTag implements IMessage {

        public I12_PacketTag(){}

        public I12_PacketTag(String lis, TagCW com){
            fill(lis, com);
        }

        @Override
        public void fromBytes(ByteBuf buf){
            decode(buf);
        }

        @Override
        public void toBytes(ByteBuf buf){
            encode(buf);
        }

    }


    public static class Server implements IMessageHandler<I12_PacketTag, IMessage> {

        @Override
        public IMessage onMessage(I12_PacketTag packet, MessageContext ctx){
			try{
				UniFCL.TAG_S.get(packet.lis).handle(packet.com, UniEntity.getEntity(ctx.getServerHandler().playerEntity));
			}
			catch(Exception e){
				e.printStackTrace();
			}
            return null;
        }

    }


    public static class Client implements IMessageHandler<I12_PacketTag, IMessage> {

        @Override
        public IMessage onMessage(I12_PacketTag packet, MessageContext ctx){
			try{
				UniFCL.TAG_C.get(packet.lis).handle(packet.com, UniEntity.getEntity(net.minecraft.client.Minecraft.getMinecraft().thePlayer));
			}
			catch(Exception e){
				e.printStackTrace();
			}
            return null;
        }

    }

}
