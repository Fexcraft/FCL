package net.fexcraft.mod.lib.adjgui;

import java.util.TreeMap;

import com.google.gson.JsonObject;

import net.fexcraft.mod.lib.adjgui.elements.AdjustableButton;
import net.fexcraft.mod.lib.adjgui.elements.AdjustableTextField;
import net.fexcraft.mod.lib.network.PacketHandler;
import net.fexcraft.mod.lib.network.packet.PacketNBTTagCompound;
import net.fexcraft.mod.lib.util.common.Print;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.server.permission.PermissionAPI;

public abstract class SynchronizedContainer extends Container {
	
	public abstract ResourceLocation getId();
	protected TreeMap<String, CustomGuiElement> elements = new TreeMap<>();
	private boolean check; protected EntityPlayer player;
	
	public SynchronizedContainer(boolean check, JsonObject json, EntityPlayer player){
		json.entrySet().forEach((entry) -> {
			JsonObject obj = entry.getValue().getAsJsonObject();
			if(obj.has("type")){
				switch(obj.get("type").getAsString()){
					case "button":{
						this.elements.put(entry.getKey(), new AdjustableButton(this, entry.getKey(), obj));
						break;
					}
					case "textfield":{
						this.elements.put(entry.getKey(), new AdjustableTextField(this, entry.getKey(), obj));
						break;
					}
				}
			}
		});
		this.check = check; this.player = player;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player){
		return !check ? !player.isDead : PermissionAPI.hasPermission(player, "fcl.container." + getId().toString());
	}
	
	public void onDataPacket0(EntityPlayer player, NBTTagCompound data, Side from){
		if(!data.hasKey("event")){
			onDataPacket(player, data, from);
			return;
		}
		CustomGuiElement elm = elements.get(data.getString("element"));
		if(elm == null){ Print.debug("element with id " + data.getString("element") + " not found."); return; }
		switch(data.getString("event")){
			case "button_click":{
				this.onEvent(player, data, "button_click", elm);
				break;
			}
			case "button_update":{
				elm.onDataPacket(player, data, from);
				break;
			}
			case "textfield_click":{
				this.onEvent(player, data, "textfield_click", elm);
				break;
			}
			case "textfield_update":{
				elm.onDataPacket(player, data, from);
				break;
			}
			default:{
				Print.debug("event not found", data, elm);
				break;
			}
		}
	}

	public abstract void onDataPacket(EntityPlayer player, NBTTagCompound data, Side from);
	
	public abstract void onEvent(EntityPlayer player, NBTTagCompound data, String type, CustomGuiElement element);
	
	@Override
	public void onContainerClosed(EntityPlayer player){
		super.onContainerClosed(player);
	}
	
	public void send(NBTTagCompound compound, Side side){
		compound.setString("target_listener", "fcl:adjustable_gui");
		if(side.isClient()){
			PacketHandler.getInstance().sendTo(new PacketNBTTagCompound(compound), (EntityPlayerMP)player);
		}
		else{
			PacketHandler.getInstance().sendToServer(new PacketNBTTagCompound(compound));
		}
	}

	protected boolean isKeyTyped(char typedChar, int keyCode){
		for(CustomGuiElement elm : elements.values()){
			if(elm.isKeyTyped(typedChar, keyCode)){
				return true;
			}
		}
		return false;
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton){
		elements.values().forEach(elm -> elm.mouseClicked(mouseX, mouseY, mouseButton));
	}
	
	public void toggleElement(String elm){
		this.elements.get(elm).toggleState();
	}
	
}