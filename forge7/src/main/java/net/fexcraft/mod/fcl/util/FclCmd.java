package net.fexcraft.mod.fcl.util;

import net.fexcraft.mod.fcl.UniFCL;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.Collections;
import java.util.List;

public class FclCmd implements ICommand {

	@Override
	public String getCommandName(){
		return "fcl";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_){
		return "fcl";
	}

	@Override
	public List getCommandAliases(){
		return Collections.emptyList();
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args){
		if(sender instanceof EntityPlayer == false){
			sender.addChatMessage(new ChatComponentText("player only command"));
			return;
		}
		UniEntity.getEntity(sender).openUI(UniFCL.SELECT_CONFIG, 0, 0, 0);
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_){
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_){
		return Collections.emptyList();
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_){
		return false;
	}

	@Override
	public int compareTo(Object o){
		return 0;
	}

}
