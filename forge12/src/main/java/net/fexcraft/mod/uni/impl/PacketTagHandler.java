package net.fexcraft.mod.uni.impl;

import io.netty.buffer.ByteBuf;
import net.fexcraft.mod.fcl.UniFCL;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.packet.PacketTag;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                try{
                    UniFCL.TAG_S.get(packet.lis).handle(packet.com, UniEntity.getEntity(ctx.getServerHandler().player));
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            });
            return null;
        }

    }


    public static class Client implements IMessageHandler<I12_PacketTag, IMessage> {

        @Override
        public IMessage onMessage(I12_PacketTag packet, MessageContext ctx){
            net.minecraft.client.Minecraft.getMinecraft().addScheduledTask(() -> {
                try{
                    UniFCL.TAG_C.get(packet.lis).handle(packet.com, UniEntity.getEntity(net.minecraft.client.Minecraft.getMinecraft().player));
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            });
            return null;
        }

    }

}
