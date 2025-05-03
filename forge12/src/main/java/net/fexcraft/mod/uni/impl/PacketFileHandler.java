package net.fexcraft.mod.uni.impl;

import io.netty.buffer.ByteBuf;
import net.fexcraft.lib.mc.render.ExternalTextureHelper;
import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.fcl.UniFCL;
import net.fexcraft.mod.uni.packet.PacketFile;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.world.EntityW;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class PacketFileHandler {

    public static class I12_PacketImg extends PacketFile implements IMessage {

        @Override
        public void fromBytes(ByteBuf buf){
            decode(buf);
        }

        @Override
        public void toBytes(ByteBuf buf){
            encode(buf);
        }

    }


    public static class Server implements IMessageHandler<I12_PacketImg, IMessage> {

        @Override
        public IMessage onMessage(I12_PacketImg packet, MessageContext ctx){
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                try{
                    EntityW player = UniEntity.getEntity(ctx.getServerHandler().player);
                    if(!packet.lis.equals("def")){
                        UniFCL.SFL_S.get(packet.lis).handle(packet.loc, null, player);
                        return;
                    }
                    byte[] tex = UniFCL.getServerFile(packet.loc);
                    FCL.sendServerFile(player, packet.lis, packet.loc, tex);
                }
                catch(IOException e){
                    throw new RuntimeException(e);
                }
            });
            return null;
        }

    }


    public static class Client implements IMessageHandler<I12_PacketImg, IMessage> {

        @Override
        public IMessage onMessage(I12_PacketImg packet, MessageContext ctx){
            net.minecraft.client.Minecraft.getMinecraft().addScheduledTask(() -> {
                if(!packet.lis.equals("def")){
                    UniFCL.SFL_C.get(packet.lis).handle(packet.loc, packet.img, UniEntity.getEntity(net.minecraft.client.Minecraft.getMinecraft().player));
                    return;
                }
                ExternalTextureHelper.get(packet.loc, packet.img);
            });
            return null;
        }

    }

}
