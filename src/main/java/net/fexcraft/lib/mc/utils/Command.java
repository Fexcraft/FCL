package net.fexcraft.lib.mc.utils;

import net.fexcraft.lib.mc.api.registry.fCommand;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@fCommand
public class Command extends CommandBase {

	@Override
	public String getName(){ return "fcl"; }

	@Override
	public String getUsage(ICommandSender sender){ return "/fcl"; }

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		Print.chat(sender, "Command is currently disabled.");
	}
	
}