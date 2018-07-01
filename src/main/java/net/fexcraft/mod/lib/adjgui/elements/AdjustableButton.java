package net.fexcraft.mod.lib.adjgui.elements;

import java.util.List;

import com.google.gson.JsonObject;

import net.fexcraft.mod.lib.adjgui.AdjustableGuiContainer;
import net.fexcraft.mod.lib.adjgui.CustomGuiElement;
import net.fexcraft.mod.lib.adjgui.SynchronizedContainer;
import net.fexcraft.mod.lib.util.common.GenericGuiButton;
import net.fexcraft.mod.lib.util.common.Static;
import net.fexcraft.mod.lib.util.json.JsonUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AdjustableButton implements CustomGuiElement {
	
	private GenericGuiButton button; private String id;
	private SynchronizedContainer container;

	public AdjustableButton(SynchronizedContainer container, String id, JsonObject obj){
		this.id = id; this.container = container;
		if(Static.side().isServer()){ return; }
		int x = JsonUtil.getIfExists(obj, "x", 0).intValue();
		int y = JsonUtil.getIfExists(obj, "y", 0).intValue();
		int w = JsonUtil.getIfExists(obj, "width", 0).intValue();
		int h = JsonUtil.getIfExists(obj, "height", 0).intValue();
		this.button = new GenericGuiButton(-1, x, y, w, h, obj.has("text") ? obj.get("text").getAsString() : new String());
		button.setTexture(new ResourceLocation(JsonUtil.getIfExists(obj, "texture", AdjustableGuiContainer.TEXTURE)));
		button.setTextPos(JsonUtil.getIfExists(obj, "text_x", 0).intValue(), JsonUtil.getIfExists(obj, "text_y", 0).intValue());
		button.setTexturePos(0, JsonUtil.getIfExists(obj, "tx_enabled_x", 0).intValue(), JsonUtil.getIfExists(obj, "tx_enabled_y", 0).intValue());
		button.setTexturePos(1, JsonUtil.getIfExists(obj, "tx_enabled_hovered_x", 0).intValue(), JsonUtil.getIfExists(obj, "tx_enabled_hovered_y", 0).intValue());
		button.setTexturePos(2, JsonUtil.getIfExists(obj, "tx_disabled_x", 0).intValue(), JsonUtil.getIfExists(obj, "tx_disabled_y", 0).intValue());
		button.setTexturePos(3, JsonUtil.getIfExists(obj, "tx_disabled_hovered_x", 0).intValue(), JsonUtil.getIfExists(obj, "tx_disabled_hovered_y", 0).intValue());
	}
	
	@SideOnly(Side.CLIENT) @Override
	public void draw(float partial_ticks, int mouseX, int mouseY){
		//
	}

	@Override
	public void onGuiInit(List<GuiButton> buttons, int x, int y){
		button.x += x; button.y += y; button.id = buttons.size();
		buttons.add(button);
	}

	@Override
	public boolean onActionPerformed(SynchronizedContainer container, GuiButton button){
		if(button.id == this.button.id){
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("event", "button_click");
			compound.setString("element", id);
			container.send(compound, Side.SERVER);
			return true;
		}
		return false;
	}

	@Override
	public void onDataPacket(EntityPlayer player, NBTTagCompound data, Side from){
		button.enabled = data.hasKey("enabled") ? data.getBoolean("enabled") : button.enabled;
		button.visible = data.hasKey("visible") ? data.getBoolean("visible") : button.visible;
	}

	@Override
	public boolean isKeyTyped(char typedChar, int keyCode){
		return false;
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton){
		//
	}

	@Override
	public void toggleState(){
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("event", "button_update");
		compound.setString("element", id);
		compound.setBoolean("enabled", button.enabled = !button.enabled);
		container.send(compound, Side.SERVER);
	}

	@Override
	public String getId(){
		return id;
	}
	
}