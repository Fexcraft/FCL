package net.fexcraft.lib.mc.capabilities.sign;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fexcraft.lib.common.math.Time;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.lib.mc.network.Network;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ExampleListener implements SignCapability.Listener {
	
	private static final ResourceLocation regname = new ResourceLocation("fcl:update_checker");
	private boolean active;
	private long lastcheck;

	@Override
	public ResourceLocation getId(){
		return regname;
	}

	@Override
	public boolean isActive(){
		return active;
	}

	@Override
	public boolean onPlayerInteract(SignCapability cap, PlayerInteractEvent event, IBlockState state, TileEntitySign tileentity){
		if(event.getWorld().isRemote || !event.getEntityPlayer().getHeldItem(event.getEntityPlayer().getActiveHand()).isEmpty()){
			return false;
		}
		if(!active){
			if(tileentity.signText[0].getUnformattedText().equals("[fcl-uc]") && tileentity.signText[1] != null && !tileentity.signText[1].getUnformattedText().equals("")){
				tileentity.signText[0] = new TextComponentString(Formatter.format("&0[&9FCL-UC&0]"));
				tileentity.signText[1] = new TextComponentString(tileentity.signText[1].getUnformattedText().toLowerCase());
				lastcheck = Time.getDate();
				tileentity.signText[3] = new TextComponentString(Time.getAsString(Time.getDate()));
				String ver = getLatestVersion(tileentity.signText[1].getUnformattedText());
				if(ver == null || ver.equals("null")){ ver = "&6not found"; }
				tileentity.signText[2] = new TextComponentString(Formatter.format(ver));
				active = true;
				cap.setActive();
				sendUpdate(tileentity);
				return true;
			}
		}
		else{
			if(lastcheck + Time.HOUR_MS + Time.HOUR_MS < Time.getDate()){
				String ver = getLatestVersion(tileentity.signText[1].getUnformattedText());
				if(ver == null || ver.equals("null")){ ver = "&6not found"; }
				tileentity.signText[2] = new TextComponentString(Formatter.format(ver).toLowerCase());
				lastcheck = Time.getDate();
				sendUpdate(tileentity);
				return true;
			}
			else{
				Print.bar(event.getEntityPlayer(), "Last check was less than 2 hours ago.");
			}
		}
		return false;
	}

	private String getLatestVersion(String modid){
		JsonObject obj = Network.getModData(modid);
		if(obj == null || obj.toString().equals("{}")){
			return null;
		}
		if(obj.has("versions")){
			for(JsonElement elm : obj.get("versions").getAsJsonArray()){
				if(elm.getAsJsonObject().get("version").getAsString().equals(FCL.mcv)){
					return "&2" + elm.getAsJsonObject().get("latest_version").getAsString();
				}
			}
			return "&cno version data";
		}
		else if(obj.has("latest_version")){
			return "&2" + obj.get("latest_version").getAsString();
		}
		return "&cno version data";
	}

	@Override
	public NBTBase writeToNBT(Capability<SignCapability> capability, EnumFacing side){
		if(active){
			return new NBTTagLong(lastcheck);
		}
		return null;
	}

	@Override
	public void readNBT(Capability<SignCapability> capability, EnumFacing side, NBTBase nbt){
		if(nbt != null){
			active = true;
			lastcheck = ((NBTTagLong)nbt).getLong();
		}
		else{
			active = false;
		}
	}

}
