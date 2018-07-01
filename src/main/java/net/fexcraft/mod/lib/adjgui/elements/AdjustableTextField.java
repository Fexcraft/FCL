package net.fexcraft.mod.lib.adjgui.elements;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.google.gson.JsonObject;

import net.fexcraft.mod.lib.adjgui.CustomGuiElement;
import net.fexcraft.mod.lib.adjgui.SynchronizedContainer;
import net.fexcraft.mod.lib.util.common.Static;
import net.fexcraft.mod.lib.util.json.JsonUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class AdjustableTextField implements CustomGuiElement {
	
	private String id;
	private GuiTextField field;
	private SynchronizedContainer container;
	
	public AdjustableTextField(SynchronizedContainer container, String id, JsonObject obj){
		this.id = id;
		this.container = container;
		if(Static.side().isServer()){ return; }
		int x = JsonUtil.getIfExists(obj, "x", 0).intValue();
		int y = JsonUtil.getIfExists(obj, "y", 0).intValue();
		int w = JsonUtil.getIfExists(obj, "width", 0).intValue();
		int h = JsonUtil.getIfExists(obj, "height", 0).intValue();
		this.field = new GuiTextField(-1, net.minecraft.client.Minecraft.getMinecraft().fontRenderer, x, y, w, h);
		this.field.setMaxStringLength(JsonUtil.getIfExists(obj, "max_text_length", 256).intValue());
	}

	@Override
	public void draw(float partial_ticks, int mouseX, int mouseY){
		field.drawTextBox();
	}

	@Override
	public void onGuiInit(List<GuiButton> buttons, int guiLeft, int guiTop){
		field.x += guiLeft; field.y += guiTop;
	}

	@Override
	public boolean onActionPerformed(SynchronizedContainer container, GuiButton button){
		return false;
	}

	@Override
	public void onDataPacket(EntityPlayer player, NBTTagCompound data, Side from){
		//
	}

	@Override
	public boolean isKeyTyped(char typedChar, int keyCode){
		if(field.isFocused() && keyCode == Keyboard.KEY_RETURN){
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("event", "textfield_click");
			compound.setString("element", id);
			compound.setString("text", field.getText());
			container.send(compound, Side.SERVER);
			return true;
		}
		return field.textboxKeyTyped(typedChar, keyCode);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton){
		field.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void toggleState(){
		//Nothing to toggle.
	}

	public GuiTextField getTextField(){
		return field;
	}

	@Override
	public String getId(){
		return id;
	}
	
}