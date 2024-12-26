package net.fexcraft.lib.mc.utils;

import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.lib.mc.api.registry.fCommand;
import net.fexcraft.mod.fcl.UniFCL;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

@fCommand
public class Command extends CommandBase {

	@Override
	public String getName(){ return "fcl"; }

	@Override
	public String getUsage(ICommandSender sender){ return "/fcl"; }

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(sender instanceof EntityPlayer){
			UniEntity.getEntity(sender).openUI(UniFCL.SELECT_CONFIG, V3I.NULL);
			return;
		}
	}
	
}