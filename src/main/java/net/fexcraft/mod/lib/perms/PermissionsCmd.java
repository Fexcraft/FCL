package net.fexcraft.mod.lib.perms;

import net.fexcraft.mod.lib.api.common.fCommand;
import net.fexcraft.mod.lib.perms.PermissionNode.Type;
import net.fexcraft.mod.lib.perms.player.PlayerPerms;
import net.fexcraft.mod.lib.util.common.Print;
import net.fexcraft.mod.lib.util.json.JsonUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

@fCommand
public class PermissionsCmd extends CommandBase {

	@Override
	public String getName(){
		return "fcl.perm";
	}

	@Override
	public String getUsage(ICommandSender sender){
		return "fcl.perm_cmd.info";
	}
	
	@Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender){
    	return !(sender == null || server == null);
    }

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(sender.getCommandSenderEntity() instanceof EntityPlayer && !(args.length >= 3 && (args[2].equals("view") || args[2].equals("obj")))){
			PlayerPerms pp = PermManager.getPlayerPerms((EntityPlayer)sender);
			if(!pp.hasPermission(Permissions.FCL_PERMISSION_EDIT) && !server.isSinglePlayer()){
				Print.chat(sender, "&cNo permission to use this command.");
				return;
			}
		}
		if(args.length < 1){
			Print.chat(sender, "&7/fcl.perm &9<rank/player> &6<id/ign> &3<task> &5<null/value>");
			return;
		}
		switch(args[0]){
			case "rank":
				Rank rank = PermManager.getRank(args[1]);
				if(rank != null){
					execute(args[2], rank, server, sender, args);
				}
				else{
					Print.chat(sender, "Rank not found.");
				}
				break;
			case "player":
				EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(args[1]);
				if(player != null){
					execute(args[2], player, server, sender, args);
				}
				else{
					Print.chat(sender, "Player not found.");
				}
				break;
		}
	}

	private void execute(String string, EntityPlayer p, MinecraftServer server, ICommandSender sender, String[] args){
		PlayerPerms player = PermManager.getPlayerPerms(p);
		switch(string){
			case "give":
			case "add":
				if(args.length < 4){
					Print.chat(sender, "Missing Arguments.");
				}
				if(player.getPermissions().containsKey(args[3])){
					Print.chat(sender, "Player already has this permission node.");
				}
				else{
					player.add(args[3], Type.BOOLEAN, false);
					Print.chat(sender, "Permission added.");
				}
				break;
			case "rem":
			case "remove":
			case "del":
			case "delete":
				if(args.length < 4){
					Print.chat(sender, "Missing Arguments.");
				}
				if(player.getPermissions().containsKey(args[3])){
					player.getPermissions().remove(args[4]);
					Print.chat(sender, "Permission removed.");
				}
				else{
					Print.chat(sender, "Player doesn't have this permission node.");
				}
				break;
			case "set":
				if(args.length < 5){
					Print.chat(sender, "Missing Arguments.");
				}
				if(player.getPermissions().containsKey(args[3])){
					player.getPermissionNode(args[3]).value = args[4].equals("true");
					Print.chat(sender, "Permission set. (" + player.getPermissionNode(args[3]).value.toString() + ");");
					//fcl.perm player0 name1 task2 id3 value4 
				}
				else{
					Print.chat(sender, "Player doesn't have this permission node.");
				}
				break;
			case "view":
			case "obj":
				Print.chat(sender, "Permission data of: " + p.getName());
				Print.chat(sender, JsonUtil.setPrettyPrinting(player.toJson(p.getGameProfile().getId())));
				break;
		}
		player.save(p.getGameProfile().getId());
	}

	private void execute(String string, Rank rank, MinecraftServer server, ICommandSender sender, String[] args){
		switch(string){
			case "give":
			case "add":
				if(args.length < 4){
					Print.chat(sender, "Missing Arguments.");
				}
				if(rank.getPermissions().containsKey(args[3])){
					Print.chat(sender, "Rank already has this permission node.");
				}
				else{
					rank.add(args[3], Type.BOOLEAN, false);
					Print.chat(sender, "Permission added.");
				}
				break;
			case "rem":
			case "remove":
			case "del":
			case "delete":
				if(args.length < 4){
					Print.chat(sender, "Missing Arguments.");
				}
				if(rank.getPermissions().containsKey(args[3])){
					rank.getPermissions().remove(args[4]);
					Print.chat(sender, "Permission removed.");
				}
				else{
					Print.chat(sender, "Rank doesn't have this permission node.");
				}
				break;
			case "set":
				if(args.length < 5){
					Print.chat(sender, "Missing Arguments.");
				}
				if(rank.getPermissions().containsKey(args[3])){
					rank.getPermissionNode(args[3]).value = args[4].equals("true");
					Print.chat(sender, "Permission set. (" + rank.getPermissionNode(args[3]).value.toString() + ");");
				}
				else{
					Print.chat(sender, "Rank doesn't have this permission node.");
				}
				break;
			case "view":
			case "obj":
				Print.chat(sender, "Permission data of: " + rank.getName());
				Print.chat(sender, JsonUtil.setPrettyPrinting(rank.toJson()));
				break;
		}
		PermManager.saveRank(rank);
	}
	
}