package net.fexcraft.mod.uni.world;

import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.mod.uni.EnvInfo;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import java.util.UUID;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class MessageSenderI implements MessageSender {

	public final ICommandSender sender;

	public MessageSenderI(ICommandSender sender){
		this.sender = sender;
	}

	@Override
	public void send(String s){
		sender.sendMessage(new TextComponentString(Formatter.format(s)));
	}

	@Override
	public void send(String str, Object... args){
		sender.sendMessage(new TextComponentString(Formatter.format(str, args)));
	}

	@Override
	public void sendLink(Object root, String url){
		if(EnvInfo.CLIENT){
			net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(
				new net.minecraft.client.gui.GuiConfirmOpenLink((net.minecraft.client.gui.inventory.GuiContainer)root, url, 31102009, true));
		}
	}

	@Override
	public void bar(String s){
		if(sender instanceof EntityPlayer){
			((EntityPlayer)sender.getCommandSenderEntity()).sendStatusMessage(new TextComponentString(Formatter.format(s)), true);
		}
		else send(s);
	}

	@Override
	public void bar(String s, Object... objs){
		if(sender instanceof EntityPlayer){
			((EntityPlayer)sender.getCommandSenderEntity()).sendStatusMessage(new TextComponentString(Formatter.format(s, objs)), true);
		}
		else send(s);
	}

	@Override
	public String getName(){
		return sender.getName();
	}

	@Override
	public UUID getUUID(){
		return sender.getCommandSenderEntity().getUniqueID();
	}

}
